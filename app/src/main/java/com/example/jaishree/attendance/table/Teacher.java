package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jaishree.attendance.Pojo.SubjectPojo;

import java.util.ArrayList;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class Teacher {
    public static final String TABLE_NAME="teacher_tbl";
    public static int b_id;

    public static String createQuery="CREATE TABLE `teacher_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`mobile`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`subject_id`\tTEXT,\n" +
            "\t`qualification`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`image`\tTEXT\n" +
            ");";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String MOBILE="mobile";
    public static final String EMAIL="email";
    public static final String SUBJECT_ID="subject_id";
    public static final String QUALIFICATION="qualification";
    public static final String PASSWORD="password";
    public static final String IMAGE="image";

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
    public static Cursor select(SQLiteDatabase db,String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }

    public static ArrayList<SubjectPojo> getTeacherSubjects(SQLiteDatabase db, String selection)
    {
        ArrayList<SubjectPojo> arrayList = new ArrayList<>();
        Cursor cursor = Teacher.select(db, selection);
        if (cursor != null)
        {
            if (cursor.moveToNext())
            {
                String ids = cursor.getString(4);
                String[] idArray = ids.split(",");
                for (String strId : idArray)
                {
                    String whereClause = Subject.ID + " = '" + strId + "'";
                    SubjectPojo pojo = new SubjectPojo();
                    Cursor cursorSub = Subject.select(db, whereClause);

                    if(cursorSub!=null && cursorSub.moveToNext()) {
                        int id = cursorSub.getInt(0);
                        String name = cursorSub.getString(1);
                        b_id = cursorSub.getInt(2);
                        String sem = cursorSub.getString(3);
                        pojo.setId(id);
                        pojo.setName(name);
                        pojo.setBranch_id(b_id);
                        pojo.setSemester(sem);
                    }

                    String sel= branch.ID + "='" + b_id + "'";
                    Cursor cursorBranch= branch.select(db,sel);
                    if(cursorBranch!=null && cursorBranch.moveToNext()) {
                        String branch_name = cursorBranch.getString(1);
                        pojo.setBranch_name(branch_name);
                    }


                    arrayList.add(pojo);
                }
            }
        }

        return arrayList;
    }


}
