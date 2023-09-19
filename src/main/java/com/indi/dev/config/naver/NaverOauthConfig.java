package com.indi.dev.config.naver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ConfigurationProperties(prefix = "oauth.naver")
public class NaverOauthConfig {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String[] scope;
    private String state;

    public NaverOauthConfig(){}

}
