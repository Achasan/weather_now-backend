package com.weathernow.server.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.weathernow.server.api.WeatherAPI.*;

public class WeatherApiTest {

    private WeatherAPI api = new WeatherAPI();

    /**
     *  ncst connect test
     *  초단기실황 카테고리 총 8개, array size는 8이 나와야 성공
     */
    @Test
    @DisplayName("ncst_connect_test")
    public void ncstConnectTest() throws IOException {

        //when
        JsonArray array = api.connect(ncst);

        //then
        Assertions.assertThat(array.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("fcst_connect_test")
    public void fcstConnectTest() throws IOException {

        //when
        JsonArray array = api.connect(fcst);

        //then
        Assertions.assertThat(array.size()).isGreaterThanOrEqualTo(60);
    }

    @Test
    @DisplayName("vilage_connect_test")
    public void vilageConnectTest() throws IOException {

        //when
        JsonArray array = api.connect(vilage);

        //then
        Assertions.assertThat(array.size()).isGreaterThan(700);
    }

    @Test
    @DisplayName("version_connect_test")
    public void versionConnectTest() throws IOException {

        //when
        JsonArray array = api.connect(version);

        JsonObject jsonObject = (JsonObject) array.get(0);
        String version = jsonObject.get("version").getAsString();

        //then
        Assertions.assertThat(array.size()).isEqualTo(1);
        Assertions.assertThat(version.length()).isEqualTo(14);
    }

}
