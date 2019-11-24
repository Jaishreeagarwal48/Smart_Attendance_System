package com.example.jaishree.attendance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jaishree.attendance.table.Admin;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.News;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.Teacher;
import com.example.jaishree.attendance.table.branch;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    public MySqliteOpenHelper(Context context){
        super(context,"attendance.db",null,1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        branch.createTable(sqLiteDatabase);
        News.createTable(sqLiteDatabase);
        Teacher.createTable(sqLiteDatabase);
        Subject.createTable(sqLiteDatabase);
        Student.createTable(sqLiteDatabase);
        Admin.createTable(sqLiteDatabase);
        Attendance.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        branch.upgradeTable(sqLiteDatabase);
        News.upgradeTable(sqLiteDatabase);
        Teacher.upgradeTable(sqLiteDatabase);
        Subject.upgradeTabe(sqLiteDatabase);
        Student.updateTable(sqLiteDatabase);
        Admin.updateTable(sqLiteDatabase);
        Attendance.updateTable(sqLiteDatabase);
    }
}
