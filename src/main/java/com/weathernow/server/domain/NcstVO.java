package com.weathernow.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NcstVO {
    private String baseTime;
    private String category;
    private String obsrValue;
    private int nx;
    private int ny;
}
