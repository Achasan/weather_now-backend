package com.weathernow.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FcstVO {
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String FcstValue;
    private int nx;
    private int ny;
}