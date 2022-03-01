package com.weathernow.server.util;

import com.weathernow.server.enumeration.UltraSrt;

public class FcstValueConverter {

    public static String T1HConverter(String t1hValue) {
        return Long.toString(Math.round(Double.parseDouble(t1hValue)))
                + UltraSrt.valueOf("T1H").getUnit();
    }

    public static String SKYConverter(String skyValue) {

        int value = Integer.parseInt(skyValue);
        String convertValue = null;

        switch(value) {
            case 1:
                convertValue = "맑음";
                break;
            case 3:
                convertValue = "구름많음";
                break;
            case 4:
                convertValue = "흐림";
                break;
        }

        return convertValue + UltraSrt.valueOf("SKY").getUnit();
    }

    public static String PTYConverter(String fcstValue) {

        String convertValue = null;

        switch(fcstValue) {
            case "0":
                convertValue = "없음";
                break;
            case "1":
                convertValue = "비";
                break;
            case "2":
                convertValue = "비/눈";
                break;
            case "3":
                convertValue = "눈";
                break;
            case "4":
                convertValue = "소나기";
                break;
            case "5":
                convertValue = "빗방울";
                break;
            case "6":
                convertValue = "빗방울눈날림";
                break;
            case "7":
                convertValue = "눈날림";
                break;
        }

        return convertValue + UltraSrt.valueOf("PTY").getUnit();
    }

    public static String VECConverter(String fcstValue) {

        int value = Integer.parseInt(fcstValue);
        String convertValue = null;

        if(value >= 0 && value <= 90) {

            convertValue = "북동풍";

        } else if(value >= 90 && value <= 180) {

            convertValue = "남동풍";

        } else if(value >= 180 && value <= 270) {

            convertValue = "남서풍";

        } else if(value >= 270 && value <= 360) {

            convertValue = "북서풍";

        }

        return convertValue + UltraSrt.valueOf("VEC").getUnit();
    }
}
