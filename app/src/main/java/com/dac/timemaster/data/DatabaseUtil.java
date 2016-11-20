package com.dac.timemaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 凯齐 on 2016/11/7.
 */

public class DatabaseUtil {

    private static SQLiteDatabase db=null;
    private static DatabaseHelper helper=null;

    //向表中插入字段
    public static long insert(Context context,String tableName,String id,ContentValues values){

        helper=new DatabaseHelper(context);
        db=helper.getWritableDatabase();
        long rows=db.insert(tableName, id, values);
        closeDatabase();
        return rows;
    }

    //向表中删除字段
    public static int delete(Context context,String tableName,String where,String[] whereArgs){
        helper=new DatabaseHelper(context);
        db=helper.getWritableDatabase();
        int rows=db.delete(tableName, where, whereArgs);
        closeDatabase();
        return rows;
    }

    //向表中修改字段
    public static int update(Context context, String tableName, ContentValues values, String where, String[] whereArgs){
        helper=new DatabaseHelper(context);
        db=helper.getWritableDatabase();
        int rows=db.update(tableName, values, where, whereArgs);
        closeDatabase();
        return rows;
    }

    //向表中查找字段
    public static Cursor query(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        helper=new DatabaseHelper(context);
        db=helper.getWritableDatabase();
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    private static void closeDatabase() {
        // TODO Auto-generated method stub
        db.close();
    }

}