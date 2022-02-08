package com.weathernow.server.mapper;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
}
