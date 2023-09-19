package com.indi.dev.entity;

import lombok.Getter;

import java.util.Locale;

import static java.util.Locale.*;

@Getter
public enum Genre {
    /**
     * 인디뮤직
     * 락/메탈
     * RNB soul
     * POP
     * 발라드
     * 랩/힙합
     */
    INDIMUSIC,
    ROCKMETAL,
    RNBSOUL,
    POP,
    BALLAD,
    RAPHIPHOP,
    ;
    public static Genre fromName(String type){ return Genre.valueOf(type.toUpperCase(ENGLISH)); }
}
