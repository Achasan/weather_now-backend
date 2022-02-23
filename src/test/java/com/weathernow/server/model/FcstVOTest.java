package com.weathernow.server.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FcstVOTest {

    @Test
    @DisplayName("FcstVO_SKYConvert_테스트")
    public void FcstVOTest() {
        //given
        FcstVO fcst1 = new FcstVO();
        fcst1.setCategory("SKY");
        fcst1.setFcstValue("1");

        FcstVO fcst2 = new FcstVO();
        fcst2.setCategory("SKY");
        fcst2.setFcstValue("3");

        FcstVO fcst3 = new FcstVO();
        fcst3.setCategory("SKY");
        fcst3.setFcstValue("4");

        //when

        //then
        Assertions.assertThat(fcst1.getFcstValue()).isEqualTo("맑음");
        Assertions.assertThat(fcst2.getFcstValue()).isEqualTo("구름많음");
        Assertions.assertThat(fcst3.getFcstValue()).isEqualTo("흐림");
    }
}
