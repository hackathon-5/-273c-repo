package com.sparcedge.n273c.charlestontech;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Evea on 8/29/2015.
 */
public class Events {
    private String name;
    private Date date;
    private String website;
    private String desc;

    public Events(String name, Date date, String website, String desc) {
        this.name = name;
        this.date = date;
        this.website = website;
        this.desc = desc;

    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date.toLocaleString();
    }

    public String getWebsite() {
        return website;
    }

    public String getDesc() {
        return desc;
    }

    public String toString (){
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        String data = name + " " + df.format(date);
//        return data;
        return name + " " + cal.countdown(date);
    }

    public String getDetails (){
        String data = name + "\n" + desc + "\n" + website;
        return data;
    }


}

class cal {
    public static int SECONDS_IN_A_DAY = 24 * 60 * 60;
    public static String countdown(Date date) {
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(date);
        thatDay.get(Calendar.DAY_OF_MONTH);
        thatDay.get(Calendar.MONTH);
        thatDay.get(Calendar.YEAR);

        Calendar today = Calendar.getInstance();
        long diff =  thatDay.getTimeInMillis() - today.getTimeInMillis();
        long diffSec = diff / 1000;

        long days = diffSec / SECONDS_IN_A_DAY;
//        long secondsDay = diffSec % SECONDS_IN_A_DAY;
//        long seconds = secondsDay % 60;
//        long minutes = (secondsDay / 60) % 60;
//        long hours = (secondsDay / 3600); // % 24 not needed

        if (days < 0) return "past";

        return days + " days";
    }
}