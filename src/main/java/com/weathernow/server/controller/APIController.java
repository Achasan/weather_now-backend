package com.weathernow.server.controller;

import com.google.gson.*;
import com.weathernow.server.domain.FcstVO;
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
import java.util.HashMap;
import java.util.Map;

/**
 * UltraSrtNcst : 초단기 실황
 * UltraSrtFcst : 초단기 예보
 */

@Slf4j
@RestController
@RequestMapping("/api/")
public class APIController {

    private final String ncst = "getUltraSrtNcst";
    private final String fcst = "getUltraSrtFcst";

    @GetMapping("UltraSrtNcst")
    public Map<String, String> forecast() throws IOException {

        String ncstData = connect(ncst);
        String fcstData = connect(fcst);

        Map<String, String> ncstMap = parsingNcst(ncstData);
        Map<String, String> skyMap = parsingFcst(fcstData);

        if(!skyMap.isEmpty()) {
            ncstMap.put("SKY", skyMap.get("SKY"));
        }

        return ncstMap;
    }

    private Map<String, String> parsingNcst(String ncstData) {

        JsonArray items = convertItemArray(ncstData);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

            ConvertController(ncstVO);
            ncstVO.setObsrValue(ncstVO.getObsrValue() +
                    UltraSrt.valueOf(ncstVO.getCategory()).getUnit());
            // ncstVO.setCategory(UltraSrt.valueOf(ncstVO.getCategory()).getName());

            map.put(ncstVO.getCategory(), ncstVO.getObsrValue());
        }

        return map;
    }

    private Map<String, String> parsingFcst(String fcstData) {

        JsonArray items = convertItemArray(fcstData);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            FcstVO fcstVO = gson.fromJson(jsonElement, FcstVO.class);

            String skyValue = null;
            if(fcstVO.getCategory().equals("SKY")) {
                SKYConverter(fcstVO);
                map.put(fcstVO.getCategory(), fcstVO.getFcstValue());
                break;
            }
        }

        return map;
    }

    private JsonArray convertItemArray(String data) {

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(data);
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");

        return (JsonArray) parse_items.get("item");
    }

    private String connect(String fcstType) throws IOException {
        URL url = buildURL(fcstType);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        log.info("Response code: {}", conn.getResponseCode());

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();

        String temp = "";
        while((temp = br.readLine()) != null) {
            sb.append(temp);
        }

        br.close();
        conn.disconnect();

        return sb.toString();
    }


    private URL buildURL(String fcstType) throws MalformedURLException {
        String endPointURI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + fcstType;

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
                .queryParam("base_time", hour + "00") // Ncst : 30분 발표
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


    private String SKYConverter(FcstVO fcstVO) {
        int value = Integer.parseInt(fcstVO.getFcstValue());

        if(value >= 0 && value <= 5) {

            fcstVO.setFcstValue("맑음");

        } else if(value >= 6 && value <= 8) {

            fcstVO.setFcstValue("구름많음");

        } else if(value >= 9 && value <= 10) {

            fcstVO.setFcstValue("흐림");
        }

        return fcstVO.getFcstValue();
    }

}
