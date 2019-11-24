package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 14-07-2017.
 */

public class Attendance {

    public static final String TABLE_NAME="attendance_tbl";

    public static final String ID="id";
    public static final String STUDENT_ID="student_id";
    public static final String TEACHER_ID="teacher_id";
    public static final String SUBJECT_ID="subject_id";
    public static final String STATUS="status";
    public static final String DATE="date";

    public static String createQuery="CREATE TABLE `attendance_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`student_id`\tINTEGER,\n" +
            "\t`teacher_id`\tINTEGER,\n" +
            "\t`subject_id`\tINTEGER,\n" +
            "\t`status`\tTEXT,\n" +
            "\t`date`\tTEXT\n" +
            ");";

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
    public static Cursor select(SQLiteDatabase db, String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }
}
