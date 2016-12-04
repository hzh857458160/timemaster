package com.dac.timemaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by GeniusHe on 2016/11/27.
 */

public class CountTimeDB {
    //数据库
    public static final String DB_NAME = "CountTime";

    //数据库版本
    public static final int VERSION = 1;

    private static CountTimeDB countTimeDB;

    private SQLiteDatabase db;

    private List<CountTime> tempList;

    //构造方法私有化
    private CountTimeDB(Context context){
        DatabaseHelper dbHelper = new DatabaseHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    //获取CountTimeDB的实例
    public synchronized static CountTimeDB getInstance(Context context){
        if (countTimeDB == null){
            countTimeDB =new CountTimeDB(context);
        }
        return countTimeDB;
    }

    //将CountTime实例存储到数据库
    public void saveCountTime(CountTime countTime){
        boolean isExsit = false;//tempList中是否有该countTime
        if (countTime != null){
            Log.d("CountTimeDB",countTime.getNowdate());
            tempList =loadCountTime();
            if (tempList.size()>0) {
                for (CountTime countTemp :tempList){
                    if (countTime.getPkgname().equals(countTemp.getPkgname())){
                        isExsit = true;
                        ContentValues values = new ContentValues();
                        values.put("totaltime",(countTemp.getTotaltime()+ countTime.getTotaltime()));
                        db.update(DB_NAME ,values, "pkgname = ?" ,new String[]{countTemp.getPkgname()});
                    }
                }
            }
            if (!isExsit){
                ContentValues values = new ContentValues();
                values.put("pkgname",countTime.getPkgname());
                values.put("totaltime",countTime.getTotaltime());
                values.put("nowdate",countTime.getNowdate());
                db.insert("CountTime",null,values);

            }

        }
    }


    //从数据库读取所有的CountTime信息
    public List<CountTime> loadCountTime(){
        List<CountTime> list = new ArrayList<CountTime>();
        Cursor cursor = db.query("CountTime",null,null,null,null,null,null);
        db.delete(DB_NAME, "nowdate != ?", new String[]{getTodayDate()});
        if (cursor.moveToFirst()){
            do {
                CountTime countTime = new CountTime();
                countTime.setId(cursor.getInt(cursor.getColumnIndex("id")));
                countTime.setPkgname(cursor.getString(cursor.getColumnIndex("pkgname")));
                countTime.setTotaltime(cursor.getInt(cursor.getColumnIndex("totaltime")));
                countTime.setNowdate(cursor.getString(cursor.getColumnIndex("nowdate")));
                list.add(countTime);
            }while(cursor.moveToNext());

        }
        if (cursor != null){
            cursor.close();
        }
        return list;
    }

    //每天清空一次数据库
    public void DeleteData(){
        db.execSQL("DELETE FROM CUSTOMERS");
    }

    //得到当前日期，格式 yyyy-mm-dd
    public String getTodayDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String nowDate = dateFormat.format(now);
        return nowDate;
    }


}
