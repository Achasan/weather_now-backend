package com.weathernow.server.service;


import java.io.IOException;
import java.util.Map;

public interface WeatherService {

    Map<String, Object> getWeather() throws IOException;

}
