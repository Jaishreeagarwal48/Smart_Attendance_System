package com.example.jaishree.attendance.Parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.ParentAttendanceAdapter;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Subject;

import java.util.ArrayList;

/**
 * Created by Jaishree on 17-07-2017.
 */

public class ParentViewAttendance extends Fragment {

    TextView attendanceTextView;
    ListView parentAttendanceListView;
    int studentId, branchId, subjectId;
    String semester = "", subject_name = "", status = "";

    ArrayList<TakeAttendancePojo> arrayList = new ArrayList<>();
    ParentAttendanceAdapter parentAdapter;


    int pCount, aCount, lCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parent_view_attendance, null);
        init(view);
        getParentId();
        parentAdapter = new ParentAttendanceAdapter(getActivity(), R.layout.parent_attendance_list_item, arrayList);
        parentAttendanceListView.setAdapter(parentAdapter);
        methodListener();
        return view;
    }

    private void init(View view) {
        attendanceTextView = (TextView) view.findViewById(R.id.attendanceTextView);
        parentAttendanceListView = (ListView) view.findViewById(R.id.parentAttendanceListView);
    }

    private void getParentId() {
        SharedPreferences pref = getActivity().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        studentId = pref.getInt("parentId", 0);
    }

    private void methodListener() {
        showList();
        parentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               int listId= arrayList.get(i).getSubject_id();
               String sub_name=arrayList.get(i).getSubject_name();
                Intent intent=new Intent(getActivity(), ParentSubjectAttendance.class);
                intent.putExtra("subId",listId);
                intent.putExtra("subName",sub_name);
                startActivity(intent);
            }
        });
    }

    private void showList() {
        arrayList.clear();
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mysqliteHelper.getReadableDatabase();
        String sel = Student.ID + "='" + studentId + "'";
        Cursor cur = Student.select(db, sel);
        if (cur != null && cur.moveToNext()) {
            branchId = cur.getInt(7);
            semester = cur.getString(8);
            attendanceTextView.setText(cur.getString(1) + "'s Attendance");

            String sele = Subject.BRANCH_ID + "='" + branchId + "' AND " + Subject.SEMESTER + "='" + semester + "'";
            Cursor c = Subject.select(db, sele);
            if (c != null) {
                while (c.moveToNext()) {
                    subjectId = c.getInt(0);
                    subject_name = c.getString(1);

                    pCount = 0;
                    aCount = 0;
                    lCount = 0;


                    String selectionp = Attendance.SUBJECT_ID + "='" + subjectId + "' AND " + Attendance.STUDENT_ID + "='" + studentId + "' AND " + Attendance.STATUS + " = 'present'";
                    Cursor cursorP = Attendance.select(db, selectionp);
                    if (cursorP != null) {
                        pCount = cursorP.getCount();
                    }
                    String selectionA = Attendance.SUBJECT_ID + "='" + subjectId + "' AND " + Attendance.STUDENT_ID + "='" + studentId + "' AND " + Attendance.STATUS + " = 'absent'";
                    Cursor cursorA = Attendance.select(db, selectionA);
                    if (cursorA != null) {
                        aCount = cursorA.getCount();
                    }
                    String selectionL = Attendance.SUBJECT_ID + "='" + subjectId + "' AND " + Attendance.STUDENT_ID + "='" + studentId + "' AND " + Attendance.STATUS + " = 'leave'";
                    Cursor cursorL = Attendance.select(db, selectionL);
                    if (cursorL != null) {
                        lCount = cursorL.getCount();
                    }

                    TakeAttendancePojo pojo = new TakeAttendancePojo();
                    pojo.setSubject_name(subject_name);
                    pojo.setSubject_id(subjectId);
                    pojo.setA_count(aCount);
                    pojo.setL_count(lCount);
                    pojo.setP_count(pCount);

                    arrayList.add(pojo);
                }

            }
            parentAdapter.notifyDataSetChanged();
        }
    }
}


