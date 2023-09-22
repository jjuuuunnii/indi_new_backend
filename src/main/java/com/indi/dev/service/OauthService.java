package com.indi.dev.service;

import com.indi.dev.composite.AuthCodeRequestUriProviderComposite;
import com.indi.dev.composite.OauthUserClientComposite;
import com.indi.dev.dto.oauth.UserAfterLoginDto;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import com.indi.dev.jwt.service.JwtService;
import com.indi.dev.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUriProviderComposite authCodeRequestUriProviderComposite;
    private final OauthUserClientComposite oauthUserClientComposite;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String getAuthCodeRequestUrl(SocialType socialType) {
        return authCodeRequestUriProviderComposite.provide(socialType);
    }

    @Transactional
    public UserAfterLoginDto login(HttpServletResponse response, SocialType socialType, String authCode) {
        User fetchedUser = fetchUser(socialType, authCode);
        boolean isRequiredMoreInfo = processUser(response, fetchedUser);
        log.info("User '{}' logged in with '{}'", fetchedUser.getName(), socialType);
        return buildUserAfterLoginDto(fetchedUser, isRequiredMoreInfo);
    }

    private User fetchUser(SocialType socialType, String authCode) {
        return oauthUserClientComposite.fetch(socialType, authCode);
    }

    private boolean processUser(HttpServletResponse response, User fetchedUser) {
        boolean isRequiredMoreInfo = findOrSaveUser(fetchedUser);
        setJwtTokens(response, fetchedUser);
        return isRequiredMoreInfo;
    }

    private boolean findOrSaveUser(User fetchedUser) {
        Optional<User> existingUser = userRepository.findBySocialTypeAndEmail(fetchedUser.getSocialType(), fetchedUser.getEmail());

        if(existingUser.isPresent()) {
            log.info("socialType = {}, email = {}",existingUser.get().getSocialType().toString(), existingUser.get().getEmail());
            return false;
        }
        userRepository.save(fetchedUser);
        return true;
    }

    private void setJwtTokens(HttpServletResponse response, User user) {
        String accessToken = jwtService.createAccessToken(user.getEmail(), user.getSocialType(), user.getSocialId());
        String refreshToken = jwtService.createRefreshToken();
        log.info("User = {} AccessToken = Bearer {}", user.getName(), accessToken);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        user.updateRefreshToken(refreshToken);
    }

    private UserAfterLoginDto buildUserAfterLoginDto(User user, boolean isRequiredMoreInfo) {
        return UserAfterLoginDto.builder()
                .isRequiredMoreInfo(isRequiredMoreInfo)
                .nickName(user.getNickName())
                .build();
    }
}
