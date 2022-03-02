package com.weathernow.server.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.weathernow.server.api.WeatherAPI;
import com.weathernow.server.model.dto.LiveDTO;
import com.weathernow.server.model.dto.VilageDTO;
import com.weathernow.server.model.vo.FcstVO;
import com.weathernow.server.model.vo.NcstVO;
import com.weathernow.server.model.vo.VersionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.weathernow.server.api.WeatherAPI.*;

@Slf4j
@Repository
public class WeatherDAOImpl implements WeatherDAO {

    private WeatherAPI api = new WeatherAPI();

    @Override
    public LiveDTO getLive() throws IOException {

        JsonArray array = api.connect(ncst);

        Map<String, String> map = new HashMap<>();
        for(int i=0; i<array.size(); i++) {

            JsonElement jsonElement = array.get(i);

            Gson gson = new Gson();
            NcstVO ncstVO = gson.fromJson(jsonElement, NcstVO.class);

            map.put(ncstVO.getCategory(), ncstVO.getObsrValue());
        }

        LiveDTO dto = new LiveDTO();

        dto.setPTY(map.get("PTY"));
        dto.setREH(map.get("REH"));
        dto.setRN1(map.get("RN1"));
        dto.setT1H(map.get("T1H"));
        dto.setUUU(map.get("UUU"));
        dto.setVEC(map.get("VEC"));
        dto.setVVV(map.get("VVV"));
        dto.setWSD(map.get("WSD"));

        return dto;
    }


    @Override
    public String getSky() throws IOException {

        JsonArray array = api.connect(fcst);

        String skyValue = null;
        for(int i=0; i<array.size(); i++) {

            JsonElement jsonElement = array.get(i);

            Gson gson = new Gson();
            FcstVO fcstVO = gson.fromJson(jsonElement, FcstVO.class);

            if(fcstVO.getCategory().equals("SKY")) {
                skyValue = fcstVO.getFcstValue();
                break;
            }
        }

        return skyValue;
    }


    @Override
    public String getVersion() throws IOException {

        JsonArray array = api.connect(version);

        JsonElement jsonElement = array.get(0);

        Gson gson = new Gson();
        VersionVO versionDTO = gson.fromJson(jsonElement, VersionVO.class);

        return versionDTO.getVersion();
    }


    @Override
    public List<VilageDTO> getVilage() throws IOException {

        JsonArray array = api.connect(vilage);

        List<VilageDTO> voList = new ArrayList<>();

        String fcstTime = null;

        VilageDTO testDTO = null;

        for(int i=0; i<array.size(); i++) {
            JsonObject jsonObject = (JsonObject) array.get(i);

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

            if(i != array.size() - 1) {

                JsonObject nextElement = (JsonObject) array.get(i + 1);
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
}
