package com.weathernow.server.domain;

/**
 * 초단기예보 Enum
 */
public enum UltraSrtFcst {
    T1H("기온"),
    RN1("1시간 강수량"),
    SKY("하늘상태"),
    UUU("풍속(동서)"),
    VVV("풍속(남북)"),
    REH("습도"),
    PTY("강수형태"),
    LGT("낙뢰"),
    VEC("풍향"),
    WSD("풍속");

    private final String name;

    UltraSrtFcst(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
