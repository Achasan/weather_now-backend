package com.weathernow.server.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class ForecastApiServiceTest {

    @Autowired
    private ForecastApiService apiService;

    @Test
    public void connect_테스트() throws IOException {

        //given

        //when
        Map<String, Map> map = apiService.weatherCall();

        //then
        Assertions.assertThat(map.get("ncst"))
                .containsKey("ODAM")
                .containsKey("SKY");

        Assertions.assertThat(map.get("vilage"))
                .containsKey("pop")
                .containsKey("pty")
                .containsKey("sky")
                .containsKey("tmp");
    }
}
