package com.weathernow.server.model;

import com.weathernow.server.util.FcstValueConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Gson 매핑 객체(단기예보)
 */
@Getter
@Setter
@ToString
public class FcstVO extends ForecastVO {
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;

    public String getFcstValue() {

        if(this.getCategory().equals("SKY")) {
            return FcstValueConverter.SKYConverter(this.fcstValue);
        }

        return this.fcstValue;
    }
}
