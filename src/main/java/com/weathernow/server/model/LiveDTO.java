package com.weathernow.server.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 실황예보 리턴객체
 */
@Getter
@Setter
public class LiveDTO {
    private String PTY;
    private String REH;
    private String RN1;
    private String SKY;
    private String T1H;
    private String UUU;
    private String VEC;
    private String VVV;
    private String WSD;
    private String ODAM;
}
