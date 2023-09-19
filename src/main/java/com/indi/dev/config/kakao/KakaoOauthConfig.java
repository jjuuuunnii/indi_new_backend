package com.indi.dev.config.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth.kakao")
public class KakaoOauthConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scope;

    public KakaoOauthConfig(){}
}
