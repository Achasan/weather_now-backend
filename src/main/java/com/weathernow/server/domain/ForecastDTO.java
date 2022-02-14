package com.weathernow.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForecastDTO {
    private String baseTime;
    private String category;
    int nx;
    int ny;
}
