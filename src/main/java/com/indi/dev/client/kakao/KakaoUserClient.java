package com.indi.dev.client.kakao;


import com.indi.dev.client.OauthUserClient;
import com.indi.dev.config.S3Config;
import com.indi.dev.config.kakao.KakaoOauthConfig;
import com.indi.dev.dto.oauth.kakao.KakaoTokenDto;
import com.indi.dev.dto.oauth.kakao.KakaoUserResponseDto;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements OauthUserClient {

    private final KakaoOauthConfig kakaoOauthConfig;
    private final KakaoApiClient kakaoApiClient;
    private final S3Config s3Config;

    @Override
    public SocialType supportServer() {
        return SocialType.KAKAO;
    }

    @Override
    public User fetch(String authCode) {
        KakaoTokenDto kakaoTokenDto = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoUserResponseDto kakaoUserResponseDto = kakaoApiClient.fetchMember("Bearer " + kakaoTokenDto.getAccessToken());
        return User.oauthUserToEntity(
                kakaoUserResponseDto.getKakaoAccount().getProfile().getNickname(),
                kakaoUserResponseDto.getKakaoAccount().getEmail(),
                SocialType.KAKAO,
                kakaoUserResponseDto.getId().toString(),
                s3Config.getDefaultImgPath()
        );
    }

    private LinkedMultiValueMap<String, String> tokenRequestParams(String authCode) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.getClientId());
        params.add("redirect_uri", kakaoOauthConfig.getRedirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.getClientSecret());
        return params;
    }
}
