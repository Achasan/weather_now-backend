package com.weathernow.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FcstVO extends ForecastDTO {
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
}
