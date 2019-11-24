package com.example.jaishree.attendance.admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.AttendanceAdapter;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class ViewAttendanceReport extends Fragment {

    Button buttonSubmit;
    Spinner branchSpinner,semesterSpinner;
    BranchSpinnerAdapter branchSpinnerAdapter;
    AttendanceAdapter attendanceAdapter;
    ListView viewAttendanceListView;
    ArrayList<BranchPojo> arrayListBranch=new ArrayList<>();
    ArrayList<SubjectPojo> arrayListSubject=new ArrayList<>();
    String enteredSemester="",enteredBranch="";
    int branch_id,subject_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewattendancereport, null);
        init(view);
        branchSpinnerAdapter=new BranchSpinnerAdapter(getActivity(),R.layout.text_list_item,arrayListBranch,ViewAttendanceReport.this);
        branchSpinner.setAdapter(branchSpinnerAdapter);
        attendanceAdapter=new AttendanceAdapter(getActivity(),R.layout.attendance_list_item,arrayListSubject);
        viewAttendanceListView.setAdapter(attendanceAdapter);
        methodListener();
        return view;
    }


    private void init(View view) {
        buttonSubmit= (Button) view.findViewById(R.id.buttonSubmit);
        branchSpinner= (Spinner) view.findViewById(R.id.branchSpinner);
        semesterSpinner= (Spinner) view.findViewById(R.id.semesterSpinner);
        viewAttendanceListView= (ListView) view.findViewById(R.id.viewAttendanceListView);
    }

    private void methodListener() {
       getBranchValue();
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BranchPojo pojo=arrayListBranch.get(i);
                branch_id=pojo.getId();
                enteredBranch=pojo.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enteredSemester.equals("Select Semester") || enteredBranch.equals("Select Branch")){
                    Toast.makeText(getActivity(), "Select all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    showSubjectList();
                }
            }
        });
        viewAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //SubjectPojo pojo=arrayListSubject.get(i);
                int subject_id=arrayListSubject.get(i).getId();
                Intent intent=new Intent(getActivity(), ViewStudentAttendance.class);
                intent.putExtra("subjectId",subject_id);
                Log.d("123", "onItemClick: "+subject_id);
                startActivity(intent);
            }
        });
    }

    private void getBranchValue() {
        arrayListBranch.clear();
        BranchPojo pojo1 = new BranchPojo();
        pojo1.setId(-1);
        pojo1.setName("Select Branch");
        arrayListBranch.add(pojo1);

        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = branch.select(db, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                BranchPojo pojo = new BranchPojo();
                pojo.setId(cursor.getInt(0));
                pojo.setName(cursor.getString(1));
                arrayListBranch.add(pojo);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }
    }

    private void showSubjectList() {
        arrayListSubject.clear();
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection= Subject.BRANCH_ID + "='" + branch_id + "' AND " + Subject.SEMESTER + "='" + enteredSemester + "'";
        Cursor cursor=Subject.select(db,selection);
        if(cursor!=null){
            while (cursor.moveToNext()){
                SubjectPojo pojo=new SubjectPojo();
                pojo.setId(cursor.getInt(0));
                pojo.setName(cursor.getString(1));
                pojo.setSemester(enteredSemester);
                pojo.setBranch_name(enteredBranch);
                pojo.setBranch_id(branch_id);
                arrayListSubject.add(pojo);
                Log.d("123", "showSubjectList: "+ Arrays.asList(arrayListSubject));
            }
            attendanceAdapter.notifyDataSetChanged();
        }
    }


}

