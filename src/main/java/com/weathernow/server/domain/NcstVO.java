package com.weathernow.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NcstVO extends ForecastDTO {
    private String obsrValue;
}
