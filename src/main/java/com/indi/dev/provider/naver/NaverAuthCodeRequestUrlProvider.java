package com.indi.dev.provider.naver;


import com.indi.dev.config.naver.NaverOauthConfig;
import com.indi.dev.entity.SocialType;
import com.indi.dev.provider.AuthCodeRequestUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final NaverOauthConfig naverOauthConfig;


    @Override
    public SocialType supportServer() {
        return SocialType.NAVER;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOauthConfig.getClientId())
                .queryParam("redirect_uri",naverOauthConfig.getRedirectUri())
                .queryParam("state", naverOauthConfig.getState()).build()
                .toUriString();
    }
}
