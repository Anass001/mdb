package com.zeneo.tmdb.util;

public class NumToTime {

    public String converter(int runtime){

        int hours = (int)runtime/60;
        int minutes = (int)runtime%60;

        String time = hours + "h "+minutes+"m";

        return time;
    }

}
