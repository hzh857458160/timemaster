package com.dac.timemaster.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION=1;
    private static String DATABASE_NAME="TimeMaster.db";

    //创建黑名单表
    private static String CREATE_BLACKLIST_TABLE_SQL="CREATE TABLE IF NOT EXISTS "+
            BlackListBean.TABLE_NAME+"("+
            BlackListBean.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            BlackListBean.PACKAGENAME+" TEXT)";
    //删除黑名单表
    private static String DROP_BLACKLIST_TABLE_SQL="DROP TABLE IF EXISTS "+BlackListBean.TABLE_NAME;
    //创建时间记录表
    private static String CREATE_CLOCK_TABLE_SQL="CREATE TABLE IF NOT EXISTS "+
            ClockBean.TABLE_NAME+"("+
            ClockBean.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ClockBean.PACKAGENAME+" TEXT,"+
            ClockBean.TIME+" INTEGER,"+
            ClockBean.DATE+" TEXT)";
    //删除时间记录表
    private static String DROP_CLOCK_TABLE_SQL="DROP TABLE IF EXISTS"+ClockBean.TABLE_NAME;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_BLACKLIST_TABLE_SQL);
        db.execSQL(CREATE_CLOCK_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //switch (oldVersion) {
           // case 1:

           // default:
        //}
    }

}
