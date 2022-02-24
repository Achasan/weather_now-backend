package com.weathernow.server.model;

import com.weathernow.server.enumeration.VilageFcst;
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
        this.pty = pty + VilageFcst.valueOf("PTY").getUnit();
    }

    public void setSky(String sky) {
        this.sky = sky + VilageFcst.valueOf("SKY").getUnit();
    }
    public void getTmp(String tmp) {
        this.tmp = tmp + VilageFcst.valueOf("TMP").getUnit();
    }

}
