package com.indi.dev.composite;


import com.indi.dev.entity.SocialType;
import com.indi.dev.provider.AuthCodeRequestUrlProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class AuthCodeRequestUriProviderComposite {

    private final Map<SocialType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUriProviderComposite(Set<AuthCodeRequestUrlProvider> providers){
        mapping = providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        identity()
                        )
                );
    }

    public String provide(SocialType socialType){
        return getProvider(socialType).provide();
    }

    public AuthCodeRequestUrlProvider getProvider(SocialType socialType){
        return Optional.ofNullable(mapping.get(socialType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 타입입니다"));
    }
}
