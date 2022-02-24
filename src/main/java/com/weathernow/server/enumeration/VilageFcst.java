package com.weathernow.server.enumeration;

/**
 * 단기예보
 */
public enum VilageFcst {

    POP("강수확률", "%"),
    PTY("강수형태", ""),
    PCP("1시간 강수량", "mm"),
    REH("습도", "%"),
    SNO("1시간 신적설", "cm"),
    SKY("하늘상태", ""),
    TMP("1시간 기온", "°"),
    TMN("일 최저기온", "°"),
    TMX("일 최고기온", "°"),
    UUU("풍속(동서)", "m/s"),
    VVV("풍속(남북)", "m/s"),
    WAV("파고", "M"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s");

    private final String name;
    private final String unit;

    VilageFcst(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }
    public String getUnit() { return unit;}
}
