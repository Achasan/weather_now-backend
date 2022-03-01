package com.weathernow.server.controller;

import com.weathernow.server.service.ForecastApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.*;

/**
 * UltraSrtNcst : 초단기 실황
 * UltraSrtFcst : 초단기 예보
 * getVilageFcst : 단기예보조회
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class APIController {

    private final ForecastApiService apiService;

    @GetMapping("weather")
    public Map<String, Object> forecast() throws IOException {
        return apiService.weatherCall();
    }
}
