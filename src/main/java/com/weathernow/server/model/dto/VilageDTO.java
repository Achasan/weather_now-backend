package com.weathernow.server.model.dto;

import com.weathernow.server.enumeration.VilageFcst;
import com.weathernow.server.util.FcstValueConverter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VilageDTO {
    private String fcstTime;
    private String pop;
    private String pty;
    private String sky;
    private String tmp;

    public void setPop(String pop) {
        this.pop = pop + VilageFcst.valueOf("POP").getUnit();
    }

    public void setPty(String pty) {
        this.pty = FcstValueConverter.PTYConverter(pty) + VilageFcst.valueOf("PTY").getUnit();
    }

    public void setSky(String sky) {
        this.sky = FcstValueConverter.SKYConverter(sky) + VilageFcst.valueOf("SKY").getUnit();
    }

    public void setTmp(String tmp) {
        this.tmp = tmp + VilageFcst.valueOf("TMP").getUnit();
    }

}
