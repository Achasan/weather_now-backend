package com.weathernow.server.dao;

import com.weathernow.server.model.dto.LiveDTO;
import com.weathernow.server.repository.WeatherDAO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class WeatherDAOTest {

    @Autowired
    private WeatherDAO dao;

    @Test
    @DisplayName("getLiveTest")
    public void getLiveTest() throws IOException {
        LiveDTO live = dao.getLive();

        Assertions.assertThat(live.getODAM()).isNull();
        Assertions.assertThat(live.getSKY()).isNull();
    }

    @Test
    @DisplayName("getSkyTest")
    public void getSkyTest() throws IOException {
        String sky = dao.getSky();

        String[] array = {"맑음", "구름많음", "흐림"};

        Assertions.assertThat(sky).isIn(array);
    }
}
