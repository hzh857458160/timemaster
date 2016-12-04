package com.dac.timemaster.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static int DATABASE_VERSION=1;
    private static String DATABASE_NAME="TimeMaster.db";
    private Context mContext;

    //创建黑名单表
    private static final String CREATE_BLACKLIST="CREATE TABLE IF NOT EXISTS "+
            BlackListBean.TABLE_NAME+"("+
            BlackListBean.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            BlackListBean.PACKAGENAME+" TEXT)";
    //删除黑名单表
    private static  String DROP_BLACKLIST="DROP TABLE IF EXISTS "+BlackListBean.TABLE_NAME;
    //创建时间记录表
    private static String CREATE_COUNTTIME="CREATE TABLE IF NOT EXISTS "+
            CountTime.TABLE_NAME+"("+
            CountTime.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            CountTime.PACKAGENAME+" TEXT,"+
            CountTime.TOTALTIME+" INTEGER,"+
            CountTime.DATE+" TEXT)";

    //删除时间记录表
    private static String DROP_COUNTTIME="DROP TABLE IF EXISTS"+ CountTime.TABLE_NAME;



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_BLACKLIST);
        db.execSQL(CREATE_COUNTTIME);
        Log.v("DatabaseHelper","建表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL(DROP_BLACKLIST_TABLE_SQL);
        db.execSQL(DROP_CLOCK_TABLE_SQL);
        onCreate(db);*/

        // TODO Auto-generated method stub
        //switch (oldVersion) {
           // case 1:

           // default:
        //}
    }

}
