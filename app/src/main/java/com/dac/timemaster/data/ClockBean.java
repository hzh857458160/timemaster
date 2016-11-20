package com.dac.timemaster.data;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class ClockBean {
    public static String TABLE_NAME = "clock";
    public static String ID = "clock_id";
    public static String PACKAGENAME = "packagename";
    public static String TIME="clock_time";
    public static String DATE="clock_date";


    private int clockId;
    private String packagename;
    private int clockTime;
    private String clockDate;

    public void ClockBean() {

    }

    public void ClockBean(int clockId, String packagename,int clockTime,String clockDate) {
        this.clockId = clockId;
        this.packagename = packagename;
        this.clockTime=clockTime;
        this.clockDate=clockDate;
    }

    public int getClockId() {
        return clockId;
    }

    public void setBlacklistId(int blacklistId) {
        this.clockId = clockId;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public int getClockTime(){
        return clockTime;
    }

    public void setClockTime(int clockTime){
        this.clockTime=clockTime;
    }

    public String getClockDate(){
        return clockDate;
    }

    public void setClockDate(String clockDate){
        this.clockDate=clockDate;
    }
}
