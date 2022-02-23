package com.weathernow.server.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NcstVOTest {

    @Test
    @DisplayName("NcstVO_T1HConvert_테스트")
    public void T1HTest() {
        //given
        NcstVO ncst1 = new NcstVO();
        ncst1.setCategory("T1H");
        ncst1.setObsrValue("4.5");

        NcstVO ncst2 = new NcstVO();
        ncst2.setCategory("T1H");
        ncst2.setObsrValue("-9.1");

        //when

        //then
        Assertions.assertThat(ncst1.getObsrValue()).isEqualTo("5°");
        Assertions.assertThat(ncst2.getObsrValue()).isEqualTo("-9°");
    }

    @Test
    @DisplayName("NcstVO_VECConvert_테스트")
    public void VECTest() {
        //given
        NcstVO ncst1 = new NcstVO();
        ncst1.setCategory("VEC");
        ncst1.setObsrValue("41");

        NcstVO ncst2 = new NcstVO();
        ncst2.setCategory("VEC");
        ncst2.setObsrValue("107");

        NcstVO ncst3 = new NcstVO();
        ncst3.setCategory("VEC");
        ncst3.setObsrValue("198");

        NcstVO ncst4 = new NcstVO();
        ncst4.setCategory("VEC");
        ncst4.setObsrValue("300");

        //when

        //then
        Assertions.assertThat(ncst1.getObsrValue()).isEqualTo("북동풍");
        Assertions.assertThat(ncst2.getObsrValue()).isEqualTo("남동풍");
        Assertions.assertThat(ncst3.getObsrValue()).isEqualTo("남서풍");
        Assertions.assertThat(ncst4.getObsrValue()).isEqualTo("북서풍");
    }

    @Test
    @DisplayName("NcstVO_PTYConvert_테스트")
    public void PTYTest() {
        //given
        NcstVO ncst1 = new NcstVO();
        ncst1.setCategory("PTY");
        ncst1.setObsrValue("0");

        NcstVO ncst2 = new NcstVO();
        ncst2.setCategory("PTY");
        ncst2.setObsrValue("2");

        NcstVO ncst3 = new NcstVO();
        ncst3.setCategory("PTY");
        ncst3.setObsrValue("4");

        NcstVO ncst4 = new NcstVO();
        ncst4.setCategory("PTY");
        ncst4.setObsrValue("6");

        //when

        //then
        Assertions.assertThat(ncst1.getObsrValue()).isEqualTo("없음");
        Assertions.assertThat(ncst2.getObsrValue()).isEqualTo("비/눈");
        Assertions.assertThat(ncst3.getObsrValue()).isEqualTo("소나기");
        Assertions.assertThat(ncst4.getObsrValue()).isEqualTo("빗방울눈날림");
    }
}
