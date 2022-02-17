package com.weathernow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NcstVO extends ForecastDTO {
    private String obsrValue;
}
