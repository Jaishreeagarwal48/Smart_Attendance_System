package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class Subject {
    public static final String TABLE_NAME="subject_tbl";

    public static String createQuery="CREATE TABLE `subject_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`branch_id`\tINTEGER,\n" +
            "\t`semester`\tTEXT\n" +
            ");";

    public static final String ID="id";
    public static final String NAME="name";
    public static final String BRANCH_ID="branch_id";
    public static final String SEMESTER="semester";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
    }
    public static void upgradeTabe(SQLiteDatabase db){
        String updateQuery=" drop table if exists "+TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }
    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db,String selection){
        String sem=Subject.SEMESTER;
        return db.query(TABLE_NAME,null,selection,null,null,null,sem+" ASC ",null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }

}
