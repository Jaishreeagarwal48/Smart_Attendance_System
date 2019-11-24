package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 08-07-2017.
 */

public class Admin {
    public static final String TABLE_NAME="admin_tbl";
    public static String createQuery="CREATE TABLE `admin_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`mobile`\tTEXT,\n" +
            "\t`gender`\tTEXT,\n" +
            "\t`address`\tTEXT,\n" +
            "\t`image`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`password`\tTEXT\n" +
            ");";

    public static final String ID="id";
    public static final String NAME="name";
    public static final String MOBILE="mobile";
    public static final String GENDER="gender";
    public static final String ADDRESS="address";
    public static final String IMAGE="image";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
        ContentValues cv=new ContentValues();
        cv.put(EMAIL,"admin@admin");
        cv.put(PASSWORD,"admin");
        cv.put(NAME,"");
        cv.put(MOBILE,"");
        cv.put(IMAGE,"");
        cv.put(GENDER,"");
        cv.put(ADDRESS,"");
        insert(db,cv);
    }
    public static void updateTable(SQLiteDatabase db){
        String updateQuery=" drop table if exists " + TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }
    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db,String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }

}
