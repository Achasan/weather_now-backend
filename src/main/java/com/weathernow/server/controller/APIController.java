package com.weathernow.server.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.*;
import com.weathernow.server.domain.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/")
public class APIController {

    @GetMapping("forecast")
    public int forecast() throws IOException {

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

        System.out.println(sb);

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(sb.toString());
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");
        JsonArray array = (JsonArray) parse_items.get("item");

        for(int i=0; i<array.size(); i++) {
            JsonObject item = (JsonObject) array.get(i);

            System.out.println("category : " + item.get("category"));
        }

        System.out.println("parse_body = " + array.size());

        br.close();
        conn.disconnect();

        return responseCode;
    }

    private URL buildURL() throws MalformedURLException {
        String endPointURI = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endPointURI)
                .queryParam("serviceKey", "WIhsB06waJJMJZMm/M4SkVOW7q/e0dtIWgG/jNK9eovNSpJl2jaCpkaUOpX6SSgDd4CbGTXZNEeYzl0RZ9e2Sg==")
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "20")
                .queryParam("dataType", "JSON")
                .queryParam("base_date", "20220204")
                .queryParam("base_time", "0500")
                .queryParam("nx", "36")
                .queryParam("ny", "127");

        return builder.build().encode().toUri().toURL();
    }
}
