package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 06-07-2017.
 */

public class Student {
    public static final String TABLE_NAME="student_tbl";

    public static String createQuery="CREATE TABLE `student_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`mobile`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`parent_name`\tTEXT,\n" +
            "\t`parent_mobile`\tTEXT,\n" +
            "\t`parent_email`\tTEXT,\n" +
            "\t`branch_id`\tINTEGER,\n" +
            "\t`semester`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`parent_password`\tTEXT,\n" +
            "\t`image`\tTEXT\n" +
            ");";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String MOBILE="mobile";
    public static final String EMAIL="email";
    public static final String PARENT_NAME="parent_name";
    public static final String PARENT_MOBILE="parent_mobile";
    public static final String PARENT_EMAIL="parent_email";
    public static final String BRANCH_ID="branch_id";
    public static final String SEMESTER="semester";
    public static final String PASSWORD="password";
    public static final String PARENT_PASSWORD="parent_password";
    public static final String IMAGE="image";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
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
