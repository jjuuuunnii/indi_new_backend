package com.indi.dev.jwt.filter;


import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.exception.jwt.InvalidAccessTokenException;
import com.indi.dev.jwt.service.JwtService;
import com.indi.dev.repository.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL_OAUTH_LOGIN = "/api/oauth/**";


    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AntPathMatcher antPathMatcher;

    public JwtAuthenticationProcessingFilter(JwtService jwtService, UserRepository userRepository, AntPathMatcher antPathMatcher) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.antPathMatcher = antPathMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (antPathMatcher.match(NO_CHECK_URL_OAUTH_LOGIN, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }


        String refreshToken = getValidRefreshToken(request);
        if (refreshToken != null) {
            reIssueAccessTokenAndRefreshToken(refreshToken, response);
            return;
        }

        checkAccessTokenAndAuthentication(request);
        filterChain.doFilter(request, response);
    }

    private String getValidRefreshToken(HttpServletRequest request) {
        return jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request) {
        jwtService.extractAccessToken(request)
                .ifPresent(accessToken -> {
                            String socialId = jwtService.extractSocialId(accessToken).orElseThrow(() -> new InvalidAccessTokenException("유효하지 않은 엑세스 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode()));
                            SocialType socialType = jwtService.extractSocialType(accessToken).orElseThrow(() -> new InvalidAccessTokenException("유효하지 않은 엑세스 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode()));
                            userRepository.findBySocialIdAndSocialType(socialId, socialType)
                                    .ifPresent(
                                            this::saveAuthentication
                                    );
                        }
                );
    }

    private void reIssueAccessTokenAndRefreshToken(String refreshToken, HttpServletResponse response) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(
                        user -> {
                            String reIssuedAccessToken = jwtService.createAccessToken(user.getEmail(), user.getSocialType(), user.getSocialId());
                            String reIssuedRefreshToken = jwtService.createRefreshToken();
                            jwtService.updateRefreshToken(user.getSocialId(), user.getSocialType(), reIssuedRefreshToken);
                            jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
                            saveAuthentication(user);
                        }
                );
    }

    private void saveAuthentication(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
