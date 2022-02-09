package com.weathernow.server.mapper;

import com.weathernow.server.domain.VilageFcst;
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
}
