package com.weathernow.server.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.weathernow.server.domain.NcstVO;
import com.weathernow.server.enumeration.UltraSrt;
import com.weathernow.server.enumeration.VilageFcst;
import com.weathernow.server.domain.FcstVO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class APIControllerTest {

    private final String ncst = "getUltraSrtNcst";
    private final String fcst = "getUltraSrtFcst";

    @Test
    public void buildURLTest() {
        // baseTime 로직

        //given
        String hour = "10";
        String min = "02";

        //when
        if(Integer.parseInt(min) < 30) {
            int modifiedHour = Integer.parseInt(hour) - 1;
            hour = String.format("%02d", modifiedHour);
        }

        //then
        Assertions.assertThat(hour + min).isEqualTo("0902");
    }

    @Test
    public Map<String, String> parsingNcst(String ncstData) {

        JsonArray items = convertItemArray(ncstData);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

            ConvertController(ncstVO);
            ncstVO.setObsrValue(ncstVO.getObsrValue() +
                    UltraSrt.valueOf(ncstVO.getCategory()).getUnit());
            // ncstVO.setCategory(UltraSrt.valueOf(ncstVO.getCategory()).getName());

            map.put(ncstVO.getCategory(), ncstVO.getObsrValue());
        }

        return map;
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
        FcstVO fcstVO = new FcstVO();
        fcstVO.setCategory("VEC");
        fcstVO.setFcstDate("20220209");
        fcstVO.setBaseTime("0830");
        fcstVO.setFcstTime("1100");
        fcstVO.setFcstValue("227");
        fcstVO.setNx(36);
        fcstVO.setNy(127);

        //when
        int value = Integer.parseInt(fcstVO.getFcstValue());

        if(value >= 0 && value <= 45) {
            fcstVO.setFcstValue("북-북동풍");
        } else if(value >= 45 && value <= 90) {
            fcstVO.setFcstValue("북동-동풍");
        } else if(value >= 90 && value <= 135) {
            fcstVO.setFcstValue("동-남동풍");
        } else if(value >= 135 && value <= 180) {
            fcstVO.setFcstValue("남동-남풍");
        } else if(value >= 180 && value <= 225) {
            fcstVO.setFcstValue("남-남서풍");
        } else if(value >= 225 && value <= 270) {
            fcstVO.setFcstValue("남서-서풍");
        } else if(value >= 270 && value <= 315) {
            fcstVO.setFcstValue("서-북서풍");
        } else if(value >= 315 && value <= 360) {
            fcstVO.setFcstValue("북서-북풍");
        }

        //then
        Assertions.assertThat(fcstVO.getFcstValue()).isEqualTo("남서-서풍");
    }





}
