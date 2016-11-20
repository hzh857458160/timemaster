package com.dac.timemaster.data;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class BlackListBean {

    public static String TABLE_NAME = "blacklist";
    public static String ID = "blacklist_id";
    public static String PACKAGENAME = "packagename";


    private int blacklistId;
    private String packagename;

    public void BlackListBean() {

    }

    public void BlackListBean(int blacklistId, String packagename) {
        this.blacklistId = blacklistId;
        this.packagename = packagename;
    }

    public int getblacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(int blacklistId) {
        this.blacklistId = blacklistId;
    }
    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

}

