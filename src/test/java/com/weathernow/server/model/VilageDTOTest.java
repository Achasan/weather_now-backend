package com.weathernow.server.model;

import com.weathernow.server.model.dto.VilageDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class VilageDTOTest {

    @Test
    @DisplayName("VilageDTO_Setter_테스트")
    public void VilageDTOTest() {
        VilageDTO dto = new VilageDTO();

        dto.setTmp("5");
        dto.setPop("40");
        dto.setSky("3");
        dto.setPty("4");
        dto.setFcstTime("02251100");

        Assertions.assertThat(dto.getTmp()).isEqualTo("5°");
        Assertions.assertThat(dto.getPop()).isEqualTo("40%");
        Assertions.assertThat(dto.getSky()).isEqualTo("구름많음");
        Assertions.assertThat(dto.getPty()).isEqualTo("소나기");
    }
}
