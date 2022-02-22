package com.weathernow.server.service;


import java.io.IOException;
import java.util.Map;

public interface ForecastApiService {

    Map<String, Map> weatherCall() throws IOException;
}
