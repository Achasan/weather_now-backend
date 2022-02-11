package com.weathernow.server.controller;

import com.google.gson.*;
import com.weathernow.server.domain.NcstVO;
import com.weathernow.server.enumeration.UltraSrt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * UltraSrtNcst : 초단기 실황
 * UltraSrtFcst : 초단기 예보
 */

@Slf4j
@RestController
@RequestMapping("/api/")
public class APIController {

    @GetMapping("UltraSrtNcst")
    public List<NcstVO> forecast() throws IOException {

        URL url = buildURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        int responseCode = conn.getResponseCode();

        log.info("Response code: {}", responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String temp = "";
        while((temp = br.readLine()) != null) {
            sb.append(temp);
        }

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(sb.toString());
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        // JsonElement parse_header = (JsonObject) jsonObject.get("header");

        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");
        JsonArray array = (JsonArray) parse_items.get("item");

        List<NcstVO> weathers = new ArrayList<>();
        for(int i=0; i<array.size(); i++) {
            JsonElement jsonElement = array.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

            ConvertController(ncstVO);
            ncstVO.setObsrValue(ncstVO.getObsrValue() +
                    UltraSrt.valueOf(ncstVO.getCategory()).getUnit());
            ncstVO.setCategory(UltraSrt.valueOf(ncstVO.getCategory()).getName());

            weathers.add(ncstVO);
        }

        br.close();
        conn.disconnect();

        return weathers;
    }

    private URL buildURL() throws MalformedURLException {
        String endPointURI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
        String min = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm"));

        if(Integer.parseInt(min) < 30) {
            int modifiedHour = Integer.parseInt(hour) - 1;
            hour = String.format("%02d", modifiedHour);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endPointURI)
                .queryParam("serviceKey", "WIhsB06waJJMJZMm/M4SkVOW7q/e0dtIWgG/jNK9eovNSpJl2jaCpkaUOpX6SSgDd4CbGTXZNEeYzl0RZ9e2Sg==")
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "60")
                .queryParam("dataType", "JSON")
                .queryParam("base_date", date)
                .queryParam("base_time", hour + "00") // 30분 발표
                .queryParam("nx", "57")
                .queryParam("ny", "128");

        log.info("Forcast Request URL = {}", builder.build().encode().toUri().toURL());

        return builder.build().encode().toUri().toURL();
    }

    private void ConvertController(NcstVO ncstVO) {
        switch(ncstVO.getCategory()) {
            case "VEC":
                VECConverter(ncstVO);
                break;
            case "SKY":
                SKYConverter(ncstVO);
                break;
            case "PTY":
                PTYConverter(ncstVO);
                break;
        }
    }

    private String VECConverter(NcstVO ncstVO) {
        int value = Integer.parseInt(ncstVO.getObsrValue());

        if(value >= 0 && value <= 45) {

            ncstVO.setObsrValue("북-북동풍");

        } else if(value >= 45 && value <= 90) {

            ncstVO.setObsrValue("북동-동풍");

        } else if(value >= 90 && value <= 135) {

            ncstVO.setObsrValue("동-남동풍");

        } else if(value >= 135 && value <= 180) {

            ncstVO.setObsrValue("남동-남풍");

        } else if(value >= 180 && value <= 225) {

            ncstVO.setObsrValue("남-남서풍");

        } else if(value >= 225 && value <= 270) {

            ncstVO.setObsrValue("남서-서풍");

        } else if(value >= 270 && value <= 315) {

            ncstVO.setObsrValue("서-북서풍");

        } else if(value >= 315 && value <= 360) {

            ncstVO.setObsrValue("북서-북풍");

        }

        return ncstVO.getObsrValue();
    }

    private String SKYConverter(NcstVO ncstVO) {
        int value = Integer.parseInt(ncstVO.getObsrValue());

        if(value >= 0 && value <= 5) {

            ncstVO.setObsrValue("맑음");

        } else if(value >= 6 && value <= 8) {

            ncstVO.setObsrValue("구름많음");

        } else if(value >= 9 && value <= 10) {

            ncstVO.setObsrValue("흐림");
        }

        return ncstVO.getObsrValue();
    }

    private String PTYConverter(NcstVO ncstVO) {
        int value = Integer.parseInt(ncstVO.getObsrValue());

        switch(value) {
            case 0:
                ncstVO.setObsrValue("없음");
                break;
            case 1:
                ncstVO.setObsrValue("비");
                break;
            case 2:
                ncstVO.setObsrValue("비/눈");
                break;
            case 3:
                ncstVO.setObsrValue("눈");
                break;
            case 4:
                ncstVO.setObsrValue("소나기");
                break;
            case 5:
                ncstVO.setObsrValue("빗방울");
                break;
            case 6:
                ncstVO.setObsrValue("빗방울눈날림");
                break;
            case 7:
                ncstVO.setObsrValue("눈날림");
                break;
        }

        return ncstVO.getObsrValue();
    }




}
