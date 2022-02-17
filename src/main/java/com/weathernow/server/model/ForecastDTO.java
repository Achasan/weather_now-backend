package com.weathernow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ForecastDTO {
    private String baseTime;
    private String category;
}
