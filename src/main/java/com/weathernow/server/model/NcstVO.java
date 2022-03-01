package com.weathernow.server.model;

import com.weathernow.server.enumeration.UltraSrt;
import com.weathernow.server.util.FcstValueConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Gson 매핑객체(초단기실황)
 */
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
                value = FcstValueConverter.VECConverter(this.obsrValue);
                break;
            case "PTY":
                value = FcstValueConverter.PTYConverter(this.obsrValue);
                break;

        }

        if(value != null) {
            return value + UltraSrt.valueOf(this.getCategory()).getUnit();
        }

        return this.obsrValue + UltraSrt.valueOf(this.getCategory()).getUnit();
    }
}
