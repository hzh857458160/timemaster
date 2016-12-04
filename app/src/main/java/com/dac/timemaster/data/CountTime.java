package com.dac.timemaster.data;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class CountTime {
    public static String TABLE_NAME = "CountTime";
    public static String ID = "id";
    public static String PACKAGENAME = "pkgname";
    public static String TOTALTIME="totaltime";
    public static String DATE="nowdate";


    private int id;
    private String pkgname;
    private int totaltime;
    private String nowdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPkgname() {
        return pkgname;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }

    public int getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(int totaltime) {
        this.totaltime = totaltime;
    }


    public String getNowdate() {
        return nowdate;
    }

    public void setNowdate(String nowdate) {
        this.nowdate = nowdate;
    }
}
