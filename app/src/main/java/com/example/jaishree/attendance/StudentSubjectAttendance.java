package com.example.jaishree.attendance;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;

import com.example.jaishree.attendance.Adapter.SubjectAttendanceAdapter;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Attendance;

import java.util.ArrayList;
import java.util.Calendar;

public class StudentSubjectAttendance extends AppCompatActivity {

    int sub_id,studentId;
    String sub_name="";
    SubjectAttendanceAdapter subjectAttendanceAdapter;
    ArrayList<TakeAttendancePojo> arrayListAttendance=new ArrayList<>();
    ListView subjectAttendanceListView;
    String status = "", selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_attendance);
        sub_id=getIntent().getIntExtra("subId",0);
        sub_name=getIntent().getStringExtra("subName");
        Log.d("123", "onCreate: "+sub_id);
        Log.d("123", "onCreate: "+sub_name);

        init();
        subjectAttendanceAdapter=new SubjectAttendanceAdapter(this,R.layout.subject_attendance_list_item,arrayListAttendance);
        subjectAttendanceListView.setAdapter(subjectAttendanceAdapter);
        getId();
        methodListener();
    }
    private void getId() {
        SharedPreferences pref = getSharedPreferences("myFile",MODE_PRIVATE);
        studentId = pref.getInt("studentId", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewattendance_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.attendanceFilter){
            selectDate();
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                if ((month + 1) < 10) {
                    selectedDate = "" + day + "-0" + (month + 1) + "-" + year;
                } else {
                    selectedDate = "" + day + "-" + (month + 1) + "-" + year;
                }
                String selection = Attendance.DATE + " = '" + selectedDate + "' AND " + Attendance.SUBJECT_ID + "='" + sub_id + "' AND " + Attendance.STUDENT_ID + "='" + studentId + "'";
                showList(selection);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showList(String selection) {
        arrayListAttendance.clear();
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = Attendance.select(db, selection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TakeAttendancePojo pojo = new TakeAttendancePojo();
                pojo.setSubject_name(sub_name);
                status = cursor.getString(4);
                pojo.setStatus(status);
                pojo.setDate(cursor.getString(5));

                arrayListAttendance.add(pojo);
            }
            subjectAttendanceAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        subjectAttendanceListView= (ListView) findViewById(R.id.subjectAttendanceListView);
    }

    private void methodListener() {
        String selection = Attendance.SUBJECT_ID + "='" + sub_id + "' AND " + Attendance.STUDENT_ID + "='" + studentId + "'";
        showList(selection);

    }
}
