package com.ru.gpsfit.utils;

/**
 * Created by chaminda on 17/06/30.
 */

public class Conversions {

    /***
     * Convert seconds to xh xm xs, readable time
     * @param totalSecs
     * @return xh xm xs format time in string
     */
    public static String SecondsToString(int totalSecs){

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return hours + "h " + minutes + "m " + seconds + "s";
    }

}
