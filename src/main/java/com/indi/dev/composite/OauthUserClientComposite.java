package com.indi.dev.composite;


import com.indi.dev.client.OauthUserClient;
import com.indi.dev.entity.SocialType;
import com.indi.dev.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class OauthUserClientComposite {

    private final Map<SocialType, OauthUserClient> mapping;

    public OauthUserClientComposite(Set<OauthUserClient> clients) {
            mapping = clients.stream()
                    .collect(toMap(
                            OauthUserClient::supportServer,
                            identity()
                    ));
    }

    public User fetch(SocialType socialType, String authCode) {
        return getClient(socialType).fetch(authCode);
    }

    public OauthUserClient getClient(SocialType socialType){
        return Optional.ofNullable(mapping.get(socialType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다"));
    }
}
