package com.weathernow.server.enumeration;

/**
 * UltraSrt (초단기 예보, 초단기 실황 같이 사용)
 */
public enum UltraSrt {
    T1H("기온", "℃"),
    RN1("1시간 강수량", "mm"),
    SKY("하늘상태", ""),
    UUU("풍속(동서)", "m/s"),
    VVV("풍속(남북)", "m/s"),
    REH("습도", "%"),
    PTY("강수형태", ""),
    LGT("낙뢰", "KA/㎢"),
    VEC("풍향", ""),
    WSD("풍속", "m/s");

    private final String name;
    private final String unit;

    UltraSrt(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }
    public String getUnit() { return unit; }
}
