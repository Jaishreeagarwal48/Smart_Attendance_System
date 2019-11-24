package com.example.jaishree.attendance.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jaishree.attendance.Adapter.TakeAttendanceAdapter;
import com.example.jaishree.attendance.Adapter.ViewAttendanceAdapter;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ViewStudentAttendance extends AppCompatActivity {

    int subjectId, studentId, sId;
    ListView viewStudentListView;
    ArrayList<TakeAttendancePojo> arrayListStudent = new ArrayList<>();
    ViewAttendanceAdapter viewAttendanceAdapter;
    String status = "", selectedDate = "", number = "",enteredMessage="";
    int pCount = 0, aCount = 0, lCount = 0;
    TextView leaveTextView, presentTextView, absentTextView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);

        subjectId = getIntent().getIntExtra("subjectId", 0);
        Log.d("123", "onCreate: " + subjectId);
        init();
        viewAttendanceAdapter = new ViewAttendanceAdapter(this, R.layout.viewattendance_list_item, arrayListStudent);
        viewStudentListView.setAdapter(viewAttendanceAdapter);
        methodListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.attendanceFilter) {
            selectDate();
        }
        if (id == R.id.attendanceMessage) {
            selectNumber();


        }
        return super.onOptionsItemSelected(item);
    }

    private void selectNumber() {
        number = "";
        for (TakeAttendancePojo array : arrayListStudent) {
            if (array.getStatus().equals("absent")) {
                number += array.getParent_mobile() + ",";
                dialog();
            }
            else{
                Toast.makeText(this, "No Student is absent", Toast.LENGTH_SHORT).show();
            }
            Log.d("123", "inside: " + number);
        }
        Log.d("123", "selectNumber: " + number);
    }

    private void dialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.message_dialog, null);
        final EditText message = (EditText) view.findViewById(R.id.message);
        Button sendMessage = (Button) view.findViewById(R.id.sendMessage);
        dialog.setTitle("Send Message");
        dialog.setContentView(view);
        dialog.show();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredMessage=message.getText().toString();
                sendMsg(enteredMessage);
            }
        });

    }

    private void sendMsg(String enteredMsg) {
        number = number.substring(0, number.length() - 1);
        StringRequest request=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + number + "&message= " + enteredMsg + "&sender=ABCDEF&route=4&country=91", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.cancel();
                Toast.makeText(ViewStudentAttendance.this, "Message is send", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewStudentAttendance.this, "error in network", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
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
                String selection = Attendance.DATE + " = '" + selectedDate + "' AND " + Attendance.SUBJECT_ID + "='" + subjectId + "'";
                showList(selection);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void init() {
        viewStudentListView = (ListView) findViewById(R.id.viewStudentListView);
        leaveTextView = (TextView) findViewById(R.id.leaveTextView);
        absentTextView = (TextView) findViewById(R.id.absentTextView);
        presentTextView = (TextView) findViewById(R.id.presentTextView);
    }

    private void methodListener() {
        String selection = Attendance.SUBJECT_ID + "='" + subjectId + "'";
        showList(selection);
    }

    private void showList(String selection) {
        arrayListStudent.clear();
        pCount = 0;
        lCount = 0;
        aCount = 0;
        Log.d("123", "showList: hi");
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = Attendance.select(db, selection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TakeAttendancePojo pojo = new TakeAttendancePojo();
                studentId = cursor.getInt(1);
                pojo.setStudent_id(studentId);
                status = cursor.getString(4);
                pojo.setStatus(status);
                pojo.setDate(cursor.getString(5));

                switch (pojo.getStatus()) {
                    case "present":
                        pCount += 1;
                        break;
                    case "absent":
                        aCount += 1;
                        break;
                    case "leave":
                        lCount += 1;
                        break;
                    default:
                        break;
                }

                presentTextView.setText("Present:" + pCount);
                absentTextView.setText("Absent:" + aCount);
                leaveTextView.setText("Leave:" + lCount);

                String sel = Student.ID + "='" + studentId + "'";
                Cursor cur = Student.select(db, sel);
                if (cur != null && cur.moveToNext()) {
                    pojo.setName(cur.getString(1));
                    pojo.setMobile(cur.getString(2));
                    pojo.setParent_mobile(cur.getString(5));
                    arrayListStudent.add(pojo);
                }

            }
            viewAttendanceAdapter.notifyDataSetChanged();
        }
    }


}
