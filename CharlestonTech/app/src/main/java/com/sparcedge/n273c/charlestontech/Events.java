package com.sparcedge.n273c.charlestontech;

import java.util.Date;

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
        String data = name + " " + date.toLocaleString();
        return data;
    }

    public String getDetails (){
        String data = name + "\n" + desc + "\n" + website;
        return data;
    }
}