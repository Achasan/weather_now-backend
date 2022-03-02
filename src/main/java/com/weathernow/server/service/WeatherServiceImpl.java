package com.weathernow.server.service;

import com.weathernow.server.model.dto.LiveDTO;
import com.weathernow.server.model.dto.VilageDTO;
import com.weathernow.server.repository.WeatherDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherDAO dao;

    @Override
    public Map<String, Object> getWeather() throws IOException {

        LiveDTO live = dao.getLive();
        live.setSKY(dao.getSky());
        live.setODAM(dao.getVersion());

        List<VilageDTO> vilage = dao.getVilage();

        Map<String, Object> map = new HashMap<>();

        map.put("live", live);
        map.put("vilage", vilage);

        return map;
    }
}
