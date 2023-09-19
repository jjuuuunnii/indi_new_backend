package com.indi.dev.provider.kakao;


import com.indi.dev.config.kakao.KakaoOauthConfig;
import com.indi.dev.entity.SocialType;
import com.indi.dev.provider.AuthCodeRequestUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public SocialType supportServer() {
        return SocialType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type","code")
                .queryParam("client_id",kakaoOauthConfig.getClientId())
                .queryParam("redirect_uri",kakaoOauthConfig.getRedirectUri())
                .queryParam("scope",String.join(",", kakaoOauthConfig.getScope()))
                .toUriString();
    }
}
