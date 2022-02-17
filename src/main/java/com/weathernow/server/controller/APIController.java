package com.weathernow.server.controller;

import com.google.gson.*;
import com.weathernow.server.model.FcstVO;
import com.weathernow.server.model.NcstVO;
import com.weathernow.server.enumeration.UltraSrt;
import com.weathernow.server.model.VersionDTO;
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
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * UltraSrtNcst : 초단기 실황
 * UltraSrtFcst : 초단기 예보
 * getVilageFcst : 단기예보조회
 */

@Slf4j
@RestController
@RequestMapping("/api/")
public class APIController {

    private final String ncst = "getUltraSrtNcst";
    private final String fcst = "getUltraSrtFcst";
    private final String vilage = "getVilageFcst";
    private final String version = "getFcstVersion";

    @GetMapping("weather")
    public Map<String, Map> forecast() throws IOException {

        Map<String, Map> finalMap = new HashMap<>();

        String ncstData = connect(ncst);
        String fcstData = connect(fcst);
        String vilageData = connect(vilage);
        String versionData = connect(version);

        Map<String, String> ncstMap = parsingNcst(ncstData);
        Map<String, Map> vilageMap = parsingVilage(vilageData);
        String skyValue = parsingFcst(fcstData);
        String version = parsingVersion(versionData);

        if(skyValue != null) {
            ncstMap.put("SKY", skyValue);
        }

        if(version != null) {
            ncstMap.put("ODAM", version);
        }

        if(!vilageMap.get("sky").isEmpty()) {
            finalMap.put("vilageSky", vilageMap.get("sky"));
        }

        if(!vilageMap.get("tmp").isEmpty()) {
            finalMap.put("vilageTmp", vilageMap.get("tmp"));
        }

        finalMap.put("ncst", ncstMap);

        return finalMap;
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


    private String parsingFcst(String fcstData) {

        JsonArray items = convertItemArray(fcstData);

        String skyValue = null;
        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            FcstVO fcstVO = gson.fromJson(jsonElement, FcstVO.class);

            if(fcstVO.getCategory().equals("SKY")) {
                SKYConverter(fcstVO);
                skyValue = fcstVO.getFcstValue();
                break;
            }
        }

        return skyValue;
    }

    private Map<String, Map> parsingVilage(String fcstData) {

        JsonArray items = convertItemArray(fcstData);

        Map<String, Map> packageMap = new HashMap<>();
        Map<String, String> skyMap = new HashMap<>();
        Map<String, String> tmpMap = new HashMap<>();

        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            FcstVO fcstVO = gson.fromJson(jsonElement, FcstVO.class);

            String dateTime = fcstVO.getFcstDate() + fcstVO.getFcstTime();
            if(fcstVO.getCategory().equals("SKY")) {
                skyMap.put(dateTime.substring(4), fcstVO.getFcstValue());
            } else if (fcstVO.getCategory().equals("TMP")) {
                tmpMap.put(dateTime.substring(4), fcstVO.getFcstValue() + "°");
            }
        }

        packageMap.put("sky", skyMap);
        packageMap.put("tmp", tmpMap);

        return packageMap;
    }

    private String parsingVersion(String versionData) {
        JsonArray item = convertItemArray(versionData);

        JsonElement jsonElement = item.get(0);

        Gson gson = new Gson();
        VersionDTO versionDTO = gson.fromJson(jsonElement, VersionDTO.class);

        return versionDTO.getVersion();
    }

    private JsonArray convertItemArray(String data) {

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(data);
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");

        return (JsonArray) parse_items.get("item");
    }


    public String connect(String fcstType) throws IOException {
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

        if (Integer.parseInt(min) < 30) {
            int modifiedHour = Integer.parseInt(hour) - 1;
            hour = String.format("%02d", modifiedHour);
        }

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endPointURI)
                .queryParam("serviceKey", "WIhsB06waJJMJZMm/M4SkVOW7q/e0dtIWgG/jNK9eovNSpJl2jaCpkaUOpX6SSgDd4CbGTXZNEeYzl0RZ9e2Sg==")
                .queryParam("pageNo", "1")
                .queryParam("dataType", "JSON");

        if (fcstType.equals(ncst) || fcstType.equals(fcst)) {

            builder.queryParam("base_date", date)
                    .queryParam("base_time", hour + min) // Ncst : 30분 발표
                    .queryParam("numOfRows", "60")
                    .queryParam("nx", "57")
                    .queryParam("ny", "128");

        } else if (fcstType.equals(vilage)) {

            builder.queryParam("base_date", date)
                    .queryParam("base_time", "1100") // Ncst : 30분 발표
                    .queryParam("numOfRows", "1000")
                    .queryParam("nx", "57")
                    .queryParam("ny", "128");

        } else {

            builder.queryParam("ftype", "ODAM")
                    .queryParam("numOfRows", "60")
                    .queryParam("basedatetime", date + hour + min);

        }

        log.info("Forcast Request URL = {}", builder.build().encode().toUri().toURL());

        return builder.build().encode().toUri().toURL();
    }

    private void ConvertController(NcstVO ncstVO) {

        switch(ncstVO.getCategory()) {
            case "T1H":
                ncstVO.setObsrValue(Long.toString(T1HConverter(ncstVO)));
                break;
            case "VEC":
                VECConverter(ncstVO);
                break;
            case "PTY":
                PTYConverter(ncstVO);
                break;
        }
    }

    private long T1HConverter(NcstVO ncstVO) {
        return Math.round(Double.parseDouble(ncstVO.getObsrValue()));
    }

    private String VECConverter(NcstVO ncstVO) {
        int value = Integer.parseInt(ncstVO.getObsrValue());

        if(value >= 0 && value <= 90) {

            ncstVO.setObsrValue("북동풍");

        } else if(value >= 90 && value <= 180) {

            ncstVO.setObsrValue("남동풍");

        } else if(value >= 180 && value <= 270) {

            ncstVO.setObsrValue("남서풍");

        } else if(value >= 270 && value <= 360) {

            ncstVO.setObsrValue("북서풍");

        }

        return ncstVO.getObsrValue();
    }


    private String PTYConverter(NcstVO ncstVO) {
        int value = Integer.parseInt(ncstVO.getObsrValue());

        switch(value) {
            case 0:
                ncstVO.setObsrValue("맑음");
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
