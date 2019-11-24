package com.example.jaishree.attendance.Teacher;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.TakeAttendanceAdapter;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Subject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TeacherTakeAttendance extends AppCompatActivity {
    int subjectId, branch_id,teacher_Id,student_id;
    ListView listViewAttendance;
    String semester = "",status="",d="";
    CheckBox checkBoxPresent, checkBoxAbsent, checkBoxLeave;
    ArrayList<TakeAttendancePojo> arrayList = new ArrayList<>();
    TakeAttendanceAdapter takeattendanceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_take_attendance);

        subjectId = getIntent().getIntExtra("subjectId", 0);
        Log.d("123", "onCreate: " + subjectId);

        init();
        takeattendanceAdapter = new TakeAttendanceAdapter(this, R.layout.take_attendance_list_item, arrayList);
        listViewAttendance.setAdapter(takeattendanceAdapter);
        getValues();
        fetchStudents();
        getTeacherId();
        getCurrentTimeStamp();
        methodListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendance_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.attendanceSubmit){
            storeAttendanceInDatabase();
        }
        return super.onOptionsItemSelected(item);
    }

    private void methodListener() {
        checkBoxPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxLeave.setChecked(false);
                    checkBoxAbsent.setChecked(false);
                    changeStatus("present");
                }
            }
        });
        checkBoxAbsent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxLeave.setChecked(false);
                    checkBoxPresent.setChecked(false);
                    changeStatus("absent");
                }

            }
        });
        checkBoxLeave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxPresent.setChecked(false);
                    checkBoxAbsent.setChecked(false);
                    changeStatus("leave");
                }

            }
        });

    }

    private void changeStatus(String status) {
        for (TakeAttendancePojo pojo : arrayList) {
            pojo.setStatus(status);
            Log.d("123", "changeStatus: " + pojo.getStatus());
        }
        takeattendanceAdapter.notifyDataSetChanged();
    }

    private void init() {
        listViewAttendance = (ListView) findViewById(R.id.listViewAttendance);
        checkBoxPresent = (CheckBox) findViewById(R.id.checkBoxPresent);
        checkBoxAbsent = (CheckBox) findViewById(R.id.checkBoxAbsent);
        checkBoxLeave = (CheckBox) findViewById(R.id.checkBoxLeave);
    }

    private void getValues() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        String selection = Subject.ID + "='" + subjectId + "'";
        Cursor cursor = Subject.select(db, selection);
        if (cursor != null && cursor.moveToNext()) {
            branch_id = cursor.getInt(2);
            semester = cursor.getString(3);
        }
    }

    private void fetchStudents() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        String sel = Student.BRANCH_ID + "='" + branch_id + "' AND " + Student.SEMESTER + "='" + semester + "'";
        Cursor cursorStudent = Student.select(db, sel);
        if (cursorStudent != null) {
            while (cursorStudent.moveToNext()) {
                student_id=cursorStudent.getInt(0);
                TakeAttendancePojo pojo = new TakeAttendancePojo();
                pojo.setStudent_id(student_id);
                pojo.setName(cursorStudent.getString(1));
                pojo.setMobile(cursorStudent.getString(2));
                arrayList.add(pojo);
            }
            takeattendanceAdapter.notifyDataSetChanged();
        }
    }

    private void getTeacherId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
       teacher_Id= preference.getInt("teacherId",0);
    }

    private void getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Timestamp timeStamp=new Timestamp(System.currentTimeMillis());
      //  Date date=new Date();
        d= String.valueOf(simpleDateFormat.format(timeStamp));

    }

    private void storeAttendanceInDatabase() {
        for(TakeAttendancePojo pojo : arrayList) {
            status = pojo.getStatus();
            student_id=pojo.getStudent_id();
            Log.d("123", "storeAttendanceInDatabase: " + pojo.getStatus());

            MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
            SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Attendance.STUDENT_ID, student_id);
            cv.put(Attendance.TEACHER_ID, teacher_Id);
            cv.put(Attendance.SUBJECT_ID, subjectId);
            cv.put(Attendance.STATUS,status);
            cv.put(Attendance.DATE,d);
            long l=Attendance.insert(db,cv);
            Log.d("123", "Content values: "+ Arrays.asList(cv));
            if(l>0){
                Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(TeacherTakeAttendance.this,TeacherPanel.class);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
