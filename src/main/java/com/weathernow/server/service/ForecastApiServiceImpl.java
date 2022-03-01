package com.weathernow.server.service;

import com.google.gson.*;
import com.weathernow.server.api.WeatherAPI;
import com.weathernow.server.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.*;

@Slf4j
@Service
public class ForecastApiServiceImpl implements ForecastApiService {

    @Override
    public Map<String, Object> weatherCall() throws IOException {

        WeatherAPI api = new WeatherAPI();
        Map<String, JsonArray> liveMap = api.getLiveData();

        LiveDTO liveDto = parsingLive(liveMap);
        List<VilageDTO> vilageList = parsingVilage(api.getVilageData());

        Map<String, Object> weatherData = new HashMap<>();

        weatherData.put("live", liveDto);
        weatherData.put("vilage", vilageList);

        return weatherData;
    }

    private LiveDTO parsingLive(Map<String, JsonArray> liveMap) {

        LiveDTO dto = new LiveDTO();

        Map<String, String> ncstMap = parsingNcst(liveMap.get("ncst"));

        dto.setPTY(ncstMap.get("PTY"));
        dto.setREH(ncstMap.get("REH"));
        dto.setRN1(ncstMap.get("RN1"));
        dto.setT1H(ncstMap.get("T1H"));
        dto.setUUU(ncstMap.get("UUU"));
        dto.setVEC(ncstMap.get("VEC"));
        dto.setVVV(ncstMap.get("VVV"));
        dto.setWSD(ncstMap.get("WSD"));
        dto.setSKY(parsingFcst(liveMap.get("fcst")));
        dto.setODAM(parsingVersion(liveMap.get("version")));

        return dto;
    }

    private Map<String, String> parsingNcst(JsonArray jsonArray) {

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<jsonArray.size(); i++) {

            JsonElement jsonElement = jsonArray.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

            map.put(ncstVO.getCategory(), ncstVO.getObsrValue());
        }

        return map;
    }


    private String parsingFcst(JsonArray jsonArray) {

        String skyValue = null;
        for(int i=0; i<jsonArray.size(); i++) {

            JsonElement jsonElement = jsonArray.get(i);

            Gson gson = new Gson();
            FcstVO fcstVO = gson.fromJson(jsonElement, FcstVO.class);

            if(fcstVO.getCategory().equals("SKY")) {
                skyValue = fcstVO.getFcstValue();
                break;
            }
        }

        return skyValue;
    }


    private List<VilageDTO> parsingVilage(JsonArray jsonArray) {

        List<VilageDTO> voList = new ArrayList<>();

        String fcstTime = null;

        VilageDTO testDTO = null;

        for(int i=0; i<jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);

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

            if(i != jsonArray.size() - 1) {

                JsonObject nextElement = (JsonObject) jsonArray.get(i + 1);
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


    private String parsingVersion(JsonArray jsonArray) {

        JsonElement jsonElement = jsonArray.get(0);

        Gson gson = new Gson();
        VersionVO versionDTO = gson.fromJson(jsonElement, VersionVO.class);

        return versionDTO.getVersion();
    }

}
