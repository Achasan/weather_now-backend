package com.weathernow.server.service;

import com.google.gson.*;
import com.weathernow.server.model.FcstVO;
import com.weathernow.server.model.NcstVO;
import com.weathernow.server.model.VersionVO;
import com.weathernow.server.model.VilageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ForecastApiServiceImpl implements ForecastApiService {

    public static final String ncst = "getUltraSrtNcst";
    public static final String fcst = "getUltraSrtFcst";
    public static final String vilage = "getVilageFcst";
    public static final String version = "getFcstVersion";

    @Override
    public Map<String, Object> weatherCall() throws IOException {

        String jsonData = connect(ncst);
        Map<String, String> ncstMap = parsingNcst(jsonData);

        jsonData = connect(fcst);
        String skyValue = parsingFcst(jsonData);

        jsonData = connect(vilage);
        List<VilageDTO> vilageList = parsingVilage(jsonData);

        jsonData = connect(version);
        String versionValue = parsingVersion(jsonData);

        ncstMap.put("SKY", skyValue);
        ncstMap.put("ODAM", versionValue);

        Map<String, Object> weatherData = new HashMap<>();

        weatherData.put("ncst", ncstMap);
        weatherData.put("vilage", vilageList);

        return weatherData;
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


    private JsonArray convertItemArray(String data) {

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(data);
        JsonObject parse_response = (JsonObject) jsonObject.get("response");
        JsonObject parse_body = (JsonObject) parse_response.get("body");
        JsonObject parse_items = (JsonObject) parse_body.get("items");

        return (JsonArray) parse_items.get("item");
    }


    private Map<String, String> parsingNcst(String ncstData) {

        JsonArray items = convertItemArray(ncstData);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<items.size(); i++) {
            JsonElement jsonElement = items.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

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
                skyValue = fcstVO.getFcstValue();
                break;
            }
        }

        return skyValue;
    }


    private List<VilageDTO> parsingVilage(String fcstData) {

        JsonArray items = convertItemArray(fcstData);

        List<VilageDTO> voList = new ArrayList<>();

        String fcstTime = null;

        VilageDTO testDTO = null;

        for(int i=0; i<items.size(); i++) {
            JsonObject jsonObject = (JsonObject) items.get(i);

            String fcstDate = jsonObject.get("fcstDate").getAsString().substring(4);
            String fcstTimeElement = jsonObject.get("fcstTime").getAsString();
            String category = jsonObject.get("category").getAsString();
            String fcstValue = jsonObject.get("fcstValue").getAsString();

            if(fcstTime == null) {
                fcstTime = fcstDate + fcstTimeElement;
                testDTO = new VilageDTO();
            }

            if((fcstDate + fcstTimeElement).equals(fcstTime)) {

                if(category.equals("SKY")) {

                    testDTO.setSky(fcstValue);

                } else if (category.equals("TMP")) {

                    testDTO.setTmp(fcstValue);

                } else if (category.equals("PTY")) {

                    testDTO.setPty(fcstValue);

                } else if (category.equals("POP")) {

                    testDTO.setPop(fcstValue);

                }
            }

            if(i != items.size() - 1) {

                JsonObject nextElement = (JsonObject) items.get(i + 1);
                String nextFcstDate = nextElement.get("fcstDate").getAsString().substring(4);
                String nextFcstTime = nextElement.get("fcstTime").getAsString();

                if(!(nextFcstDate + nextFcstTime).equals(fcstTime)) {

                    testDTO.setFcstTime(fcstTime);
                    fcstTime = nextFcstDate + nextFcstTime;
                    voList.add(testDTO);
                    testDTO = new VilageDTO();
                }

            } else {

                testDTO.setFcstTime(fcstTime);
                voList.add(testDTO);

            }

        }

        return voList;
    }


    private String parsingVersion(String versionData) {
        JsonArray item = convertItemArray(versionData);

        JsonElement jsonElement = item.get(0);

        Gson gson = new Gson();
        VersionVO versionDTO = gson.fromJson(jsonElement, VersionVO.class);

        return versionDTO.getVersion();
    }

}
