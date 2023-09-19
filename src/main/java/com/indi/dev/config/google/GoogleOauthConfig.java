package com.indi.dev.config.google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth.google")
public class GoogleOauthConfig {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String scope;
}
