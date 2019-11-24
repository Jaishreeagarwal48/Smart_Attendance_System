package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class branch {
    public static final String TABLE_NAME="branch_tbl";

    public static final String ID="id";
    public static final String NAME="name";

    public static final String create_Query="CREATE TABLE `branch_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT\n" +
            ");";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(create_Query);
    }
    public static void upgradeTable(SQLiteDatabase db){
        String updateQuery= " drop table if exists " + TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }
    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db,String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }
}
