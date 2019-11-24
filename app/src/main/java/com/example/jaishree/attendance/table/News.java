package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 03-07-2017.
 */

public class News {

    public  static final String TABLE_NAME="news_tbl";

    public static final String ID="id";
    public static final String NAME="name";
    public static final String DATE="date";
    public static final String TIME="time";

    public static final String createQuery="CREATE TABLE `news_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT\n" +
            ");";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
    }
    public static void upgradeTable(SQLiteDatabase db){
        String updateQuery=" drop table if exists " + TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }
    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db, String selection){
        String order= News.ID;
       return db.query(TABLE_NAME,null,selection,null,null,null,order+" desc ",null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return  db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME, cv,selection,null);
    }
}

