package com.example.jaishree.attendance.Teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jaishree.attendance.Adapter.AttendanceAdapter;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class TakeAttendance extends Fragment {
    ListView attendanceListView;
    ArrayList<SubjectPojo> attendanceArrayList=new ArrayList<>();
    AttendanceAdapter attendanceAdapter;
    int teacherId,subId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.takeattendance,null);
        init(view);
        methodListener();

        fetchFromDatabase();

        return view;
    }

    private void init(View view) {
        attendanceListView= (ListView) view.findViewById(R.id.attendanceListView);
    }

    private void fetchFromDatabase() {
        attendanceArrayList.clear();
        getTeacherId();

        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Log.d("123", "teacherId: "+teacherId);
        String selection= Teacher.ID + "='" + teacherId + "'";
        attendanceArrayList=Teacher.getTeacherSubjects(db,selection);

        Log.d("123", "fetchFromDatabase: "+attendanceArrayList);
        attendanceAdapter=new AttendanceAdapter(getActivity(),R.layout.attendance_list_item,attendanceArrayList);
        attendanceListView.setAdapter(attendanceAdapter);
    }

    private void methodListener() {
        attendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                subId=attendanceArrayList.get(position).getId();
                Intent i=new Intent(getActivity(),TeacherTakeAttendance.class);
                i.putExtra("subjectId",subId);
                Log.d("123", "onItemClick: "+subId);
                startActivity(i);
            }
        });
    }

    private void getTeacherId() {
        SharedPreferences preference=getActivity().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        teacherId= preference.getInt("teacherId",0);
    }
}
