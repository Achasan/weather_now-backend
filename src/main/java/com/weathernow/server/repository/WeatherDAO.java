package com.weathernow.server.repository;

import com.weathernow.server.model.dto.LiveDTO;
import com.weathernow.server.model.dto.VilageDTO;

import java.io.IOException;
import java.util.List;

public interface WeatherDAO {
    LiveDTO getLive() throws IOException;
    String getSky() throws IOException;
    String getVersion() throws IOException;
    List<VilageDTO> getVilage() throws IOException;
}
