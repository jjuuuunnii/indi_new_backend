package com.indi.dev.jwt.service;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import com.indi.dev.exception.custom.ErrorCode;
import com.indi.dev.exception.jwt.InvalidAccessTokenException;
import com.indi.dev.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;



@Service
@Slf4j
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String SOCIAL_TYPE_CLAIM = "socialType";
    private static final String BEARER = "Bearer ";
    private static final String SOCIAL_ID_CLAIM = "socialId";

    private final UserRepository userRepository;

    public String createAccessToken(String email, SocialType socialType, String socialId){
        Date nowDate = new Date();


        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(nowDate.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .withClaim(SOCIAL_ID_CLAIM, socialId)
                .withClaim(SOCIAL_TYPE_CLAIM, socialType.toString())
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken(){
        Date nowDate = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(nowDate.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }


    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, BEARER + accessToken);
        response.setHeader(refreshHeader, BEARER + refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Optional<String> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER,""));
    }


    public Optional<String> extractSocialId(String accessToken) throws InvalidAccessTokenException {
        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(SOCIAL_ID_CLAIM)
                    .asString());
        } catch (TokenExpiredException e) {
            log.info("엑세스 토큰이 만료되었습니다.");
            throw new InvalidAccessTokenException("엑세스 토큰이 만료되었습니다.", ErrorCode.TOKEN_EXPIRED.getCode());
        } catch (SignatureVerificationException e) {
            log.info("엑세스 토큰 서명이 유효하지 않습니다.");
            throw new InvalidAccessTokenException("엑세스 토큰 서명이 유효하지 않습니다.", ErrorCode.INVALID_SIGNATURE.getCode());
        } catch (Exception e){
            log.info("유효하지 않은 엑세스 토큰입니다.");
            throw new InvalidAccessTokenException("유효하지 않은 엑세스 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode());
        }
    }

    public Optional<SocialType> extractSocialType(String accessToken){
        try {
            String socialTypeStr = JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(SOCIAL_TYPE_CLAIM)
                    .asString();

            return Optional.of(SocialType.valueOf(socialTypeStr));
        } catch (TokenExpiredException e) {
            log.info("엑세스 토큰이 만료되었습니다.");
            throw new InvalidAccessTokenException("엑세스 토큰이 만료되었습니다.", ErrorCode.TOKEN_EXPIRED.getCode());
        } catch (SignatureVerificationException e) {
            log.info("엑세스 토큰 서명이 유효하지 않습니다.");
            throw new InvalidAccessTokenException("엑세스 토큰 서명이 유효하지 않습니다.", ErrorCode.INVALID_SIGNATURE.getCode());
        } catch (Exception e) {
            log.info("유효하지 않은 엑세스 토큰입니다.");
            throw new InvalidAccessTokenException("유효하지 않은 엑세스 토큰입니다.", ErrorCode.INVALID_TOKEN.getCode());
        }
    }



    @Transactional
    public void updateRefreshToken(String socialId, SocialType socialType ,String refreshToken) {
        User user = userRepository.findBySocialIdAndSocialType(socialId, socialType)
                .orElseThrow(() -> {throw new InvalidAccessTokenException("유효하지 않은 리프레시 토큰입니다",ErrorCode.INVALID_TOKEN.getCode());});

        user.setRefreshToken(refreshToken);
    }


    public boolean isTokenValid(String token){
        try{
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        }catch(InvalidAccessTokenException e){
            log.info("유효하지 않은 토큰입니다.");
            throw new InvalidAccessTokenException("유효하지 않은 토큰입니다.",ErrorCode.INVALID_TOKEN.getCode());
        }
    }



}
