package com.indi.dev.entity;

import static java.util.Locale.ENGLISH;

public enum SocialType {

    KAKAO,
    NAVER,
    GOOGLE,
    ;

    public static SocialType fromName(String type) {
        return SocialType.valueOf(type.toUpperCase(ENGLISH));
    }
}
