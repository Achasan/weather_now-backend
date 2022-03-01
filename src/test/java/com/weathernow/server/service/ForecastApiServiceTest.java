package com.weathernow.server.service;

import com.weathernow.server.model.LiveDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import com.weathernow.server.model.VilageDTO;

@SpringBootTest
public class ForecastApiServiceTest {

    @Autowired
    private ForecastApiService apiService;

    @Test
    public void connect_테스트() throws IOException {

        //given

        //when
        Map<String, Object> map = apiService.weatherCall();
        List<VilageDTO> list = (List<VilageDTO>) map.get("vilage");

        //then
        Assertions.assertThat(map.get("live")).isInstanceOf(LiveDTO.class);
        Assertions.assertThat(list.get(0)).isInstanceOf(VilageDTO.class);
    }
}
