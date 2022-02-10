package com.weathernow.server.mapper;

import com.weathernow.server.enumeration.VilageFcst;
import com.weathernow.server.domain.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class APIControllerTest {


    @Test
    public void buildURLTest() {
        String hour = "10";
        String min = "02";

        if(Integer.parseInt(min) < 30) {
            int modifiedHour = Integer.parseInt(hour) - 1;
            hour = String.format("%02d", modifiedHour);
        }

        Assertions.assertThat(hour + min).isEqualTo("0902");
    }

    @Test
    public void weatherCodeTest() {

        //given
        String name = "REH";

        //then
        Assertions.assertThat(VilageFcst.PCP.getName()).isEqualTo("1시간 강수량");
        Assertions.assertThat(VilageFcst.valueOf(name).getName()).isEqualTo("습도");
    }

    @Test
    public void VECConverterTest() {
        //given
        WeatherData weatherData = new WeatherData();
        weatherData.setCategory("VEC");
        weatherData.setBaseDate("20220209");
        weatherData.setFcstDate("20220209");
        weatherData.setBaseTime("0830");
        weatherData.setFcstTime("1100");
        weatherData.setFcstValue("227");
        weatherData.setNx(36);
        weatherData.setNy(127);

        //when
        int value = Integer.parseInt(weatherData.getFcstValue());

        if(value >= 0 && value <= 45) {
            weatherData.setFcstValue("북-북동풍");
        } else if(value >= 45 && value <= 90) {
            weatherData.setFcstValue("북동-동풍");
        } else if(value >= 90 && value <= 135) {
            weatherData.setFcstValue("동-남동풍");
        } else if(value >= 135 && value <= 180) {
            weatherData.setFcstValue("남동-남풍");
        } else if(value >= 180 && value <= 225) {
            weatherData.setFcstValue("남-남서풍");
        } else if(value >= 225 && value <= 270) {
            weatherData.setFcstValue("남서-서풍");
        } else if(value >= 270 && value <= 315) {
            weatherData.setFcstValue("서-북서풍");
        } else if(value >= 315 && value <= 360) {
            weatherData.setFcstValue("북서-북풍");
        }

        //then
        Assertions.assertThat(weatherData.getFcstValue()).isEqualTo("남서-서풍");
    }
}
