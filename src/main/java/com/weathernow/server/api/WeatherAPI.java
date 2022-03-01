package com.weathernow.server.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeatherAPI {

    public static final String ncst = "getUltraSrtNcst";
    public static final String fcst = "getUltraSrtFcst";
    public static final String vilage = "getVilageFcst";
    public static final String version = "getFcstVersion";

    public Map<String, JsonArray> getLiveData() throws IOException {

        Map<String, JsonArray> map = new HashMap<>();

        map.put("ncst", connect(ncst));
        map.put("fcst", connect(fcst));
        map.put("version", connect(version));

        return map;
    }

    public JsonArray getVilageData() throws IOException {
        return connect(vilage);
    }

    private JsonArray connect(String fcstType) throws IOException {

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

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(sb.toString());
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");

        return (JsonArray) parse_items.get("item");
    }


    private URL buildURL(String fcstType) throws MalformedURLException {

        String endPointURI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/" + fcstType;

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH"));
        String min = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm"));

        if(!fcstType.equals(vilage) && Integer.parseInt(min) < 30) {
            int modifiedHour = Integer.parseInt(hour) - 1;
            hour = String.format("%02d", modifiedHour);
        }

        if(fcstType.equals(vilage)) {
            int modifiedHour = Integer.parseInt(hour);
            modifiedHour = ((modifiedHour - 2) / 3 * 3) + 2;
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
                    .queryParam("base_time", hour + min) // Ncst : 30분 발표
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
}
