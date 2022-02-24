package com.weathernow.server.model;

import com.weathernow.server.enumeration.UltraSrt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NcstVO extends ForecastVO {
    private String obsrValue;

    public String getObsrValue() {

        String value = null;

        switch(this.getCategory()) {

            case "T1H":
                value = Long.toString(Math.round(Double.parseDouble(this.obsrValue)));
                break;
            case "VEC":
                value = VECConverter();
                break;
            case "PTY":
                value = PTYConverter();
                break;

        }

        if(value != null) {
            return value + UltraSrt.valueOf(this.getCategory()).getUnit();
        }

        return this.obsrValue + UltraSrt.valueOf(this.getCategory()).getUnit();
    }

    private String VECConverter() {

        int value = Integer.parseInt(this.obsrValue);
        String convertValue = null;

        if(value >= 0 && value <= 90) {

            convertValue = "북동풍";

        } else if(value >= 90 && value <= 180) {

            convertValue = "남동풍";

        } else if(value >= 180 && value <= 270) {

            convertValue = "남서풍";

        } else if(value >= 270 && value <= 360) {

            convertValue = "북서풍";

        }

        return convertValue;
    }

    private String PTYConverter() {

        String convertValue = null;

        switch(this.obsrValue) {
            case "0":
                convertValue = "없음";
                break;
            case "1":
                convertValue = "비";
                break;
            case "2":
                convertValue = "비/눈";
                break;
            case "3":
                convertValue = "눈";
                break;
            case "4":
                convertValue = "소나기";
                break;
            case "5":
                convertValue = "빗방울";
                break;
            case "6":
                convertValue = "빗방울눈날림";
                break;
            case "7":
                convertValue = "눈날림";
                break;
        }

        return convertValue;
    }
}
