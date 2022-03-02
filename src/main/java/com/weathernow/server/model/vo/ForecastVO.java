package com.weathernow.server.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForecastVO {
    private String baseTime;
    private String category;
}
