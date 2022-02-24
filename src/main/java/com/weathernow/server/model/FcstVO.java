package com.weathernow.server.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FcstVO extends ForecastVO {
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;

    public String getFcstValue() {

        if(this.getCategory().equals("SKY")) {
            return SKYConverter();
        }

        return this.fcstValue;
    }

    private String SKYConverter() {

        int value = Integer.parseInt(this.fcstValue);
        String convertValue = null;

        switch(value) {
            case 1:
                convertValue = "맑음";
                break;
            case 3:
                convertValue = "구름많음";
                break;
            case 4:
                convertValue = "흐림";
                break;
        }

        return convertValue;
    }
}
