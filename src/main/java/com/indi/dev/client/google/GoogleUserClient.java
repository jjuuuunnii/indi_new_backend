package com.indi.dev.client.google;


import com.indi.dev.client.OauthUserClient;
import com.indi.dev.config.S3Config;
import com.indi.dev.config.google.GoogleOauthConfig;
import com.indi.dev.dto.oauth.google.GoogleTokenDto;
import com.indi.dev.dto.oauth.google.GoogleUserResponseDto;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleUserClient implements OauthUserClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleOauthConfig googleOauthConfig;
    private final S3Config s3Config;


    @Override
    public SocialType supportServer() {
        return SocialType.GOOGLE;
    }

    @Override
    public User fetch(String authCode) {
        GoogleTokenDto tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode));
        GoogleUserResponseDto googleUserResponseDto = googleApiClient.fetchUser("Bearer " + tokenInfo.getAccessToken());
        return User.oauthUserToEntity(
                googleUserResponseDto.getName(),
                googleUserResponseDto.getEmail(),
                SocialType.GOOGLE,
                googleUserResponseDto.getId(),
                s3Config.getDefaultImgPath()
        );
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",googleOauthConfig.getClientId());
        params.add("client_secret", googleOauthConfig.getClientSecret());
        params.add("code", authCode);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", googleOauthConfig.getRedirectUri());
        return params;
    }

}
