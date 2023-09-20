package com.indi.dev.client.naver;


import com.indi.dev.client.OauthUserClient;
import com.indi.dev.config.S3Config;
import com.indi.dev.config.naver.NaverOauthConfig;

import com.indi.dev.dto.oauth.naver.NaverTokenDto;
import com.indi.dev.dto.oauth.naver.NaverUserResponseDto;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class NaverUserClient implements OauthUserClient {

    private final NaverApiClient naverApiClient;
    private final NaverOauthConfig naverOauthConfig;
    private final S3Config s3Config;

    @Override
    public SocialType supportServer() {
        return SocialType.NAVER;
    }

    @Override
    public User fetch(String authCode) {
        NaverTokenDto tokenInfo = naverApiClient.fetchToken(tokenRequestParam(authCode));
        NaverUserResponseDto naverUserResponseDto = naverApiClient.fetchUser("Bearer "+ tokenInfo.getAccessToken());
        return User.oauthUserToEntity(
                naverUserResponseDto.getResponse().getName(),
                naverUserResponseDto.getResponse().getEmail(),
                SocialType.KAKAO,
                naverUserResponseDto.getResponse().getId(),
                s3Config.getDefaultImgPath()
        );
    }

    private MultiValueMap<String, String> tokenRequestParam(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverOauthConfig.getClientId());
        params.add("client_secret", naverOauthConfig.getClientSecret());
        params.add("code", authCode);
        params.add("state", naverOauthConfig.getState());
        return params;
    }

}
