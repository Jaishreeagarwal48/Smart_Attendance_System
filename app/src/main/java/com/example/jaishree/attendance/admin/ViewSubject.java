package com.example.jaishree.attendance.admin;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Adapter.SubjectAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

/**
 * Created by Jaishree on 01-07-2017.
 */

public class ViewSubject extends Fragment {
    FloatingActionButton fab;
    private ListView listViewSubject;
    private ArrayList<SubjectPojo> arrayList=new ArrayList<>();
    private ArrayList<BranchPojo> arrayListBranch=new ArrayList<>();
    private SubjectAdapter subjectAdapter;
    private BranchSpinnerAdapter branchSpinnerAdapter;
    Spinner branchSpinnerSubject,semesterSpinnerSubject;
    EditText subjectEditText,subjectSearch;
    Button addSubjectBtn;
    int branch_id;
    String enteredBranch="",enteredSemester="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewsubject,null);
        init(view);
        methodListener();
        fetchFromDatabase();
        return view;
    }

    private void init(View view) {
        fab= (FloatingActionButton) view.findViewById(R.id.fab);
        listViewSubject= (ListView) view.findViewById(R.id.listViewSubject);
        subjectSearch= (EditText) view.findViewById(R.id.subjectSearch);
    }

    private void methodListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(getActivity(),R.style.NewDialog);
                LayoutInflater inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v=inflater.inflate(R.layout.subject_dialog,null);
                 branchSpinnerSubject= (Spinner) v.findViewById(R.id.branchSpinnerSubject);
                 semesterSpinnerSubject= (Spinner) v.findViewById(R.id.semesterSpinnerSubject);
                 subjectEditText= (EditText) v.findViewById(R.id.subjectEditText);
                 addSubjectBtn= (Button) v.findViewById(R.id.addSubjectBtn);
                dialog.setContentView(v);
                dialog.setTitle("Add Subject");
                dialog.show();
                branchSpinnerAdapter=new BranchSpinnerAdapter(getActivity(),R.layout.text_list_item,arrayListBranch,ViewSubject.this);
                branchSpinnerSubject.setAdapter(branchSpinnerAdapter);
                getBranchValue();
                branchSpinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        BranchPojo pojo=arrayListBranch.get(position);
                        enteredBranch=pojo.getName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                addSubjectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enteredSemester =semesterSpinnerSubject.getSelectedItem().toString();
                        addSubject();
                        dialog.cancel();
                        fetchFromDatabase();
                    }
                });

            }
        });

        subjectSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text=charSequence.toString();
                subjectAdapter.getFilter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void getBranchValue() {
        arrayListBranch.clear();
        BranchPojo pojo1=new BranchPojo();
        pojo1.setId(-1);
        pojo1.setName("Select Branch");
        arrayListBranch.add(pojo1);
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor= branch.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                BranchPojo pojo=new BranchPojo();
                pojo.setId(id);
                pojo.setName(name);
                Log.d("1234", "getBranchValue: "+name);
                Log.d("1234", "getBranchValue: "+pojo);
                arrayListBranch.add(pojo);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }


    }

    private void addSubject() {
        String enteredSubject = subjectEditText.getText().toString();
        if (enteredBranch.equals("Select Branch") | enteredSemester.equals("Select Semester") | enteredSubject.equals("")) {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {


            MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
            String selection= branch.NAME + "='" + enteredBranch + "'";
            Cursor cursor = branch.select(db, selection);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    branch_id = cursor.getInt(0);
                }
            }

            MySqliteOpenHelper mySqliteOpenHelper1=new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db1=mySqliteOpenHelper1.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Subject.NAME, enteredSubject);
            cv.put(Subject.BRANCH_ID, branch_id);
            cv.put(Subject.SEMESTER, enteredSemester);
            Log.d("1234", "addSubject: " + enteredSubject + enteredSemester + branch_id);
            long l = Subject.insert(db1, cv);
            if (l > 0) {
                Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db1.close();
            db.close();

        }
    }

    public void fetchFromDatabase() {
        arrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor=Subject.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                int b_id=cursor.getInt(2);
                String semester=cursor.getString(3);

                SubjectPojo pojo=new SubjectPojo();
                Cursor branchCursor=branch.select(db, branch.ID + "='" + b_id + "'");
                if(branchCursor!=null && branchCursor.moveToNext()){
                    pojo.setBranch_name(branchCursor.getString(1));
                }
                branchCursor.close();
                pojo.setId(id);
                pojo.setName(name);
                pojo.setBranch_id(b_id);
                pojo.setSemester(semester);
                arrayList.add(pojo);
            }
            subjectAdapter=new SubjectAdapter(getActivity(),R.layout.subject_list_item,arrayList,ViewSubject.this);
            listViewSubject.setAdapter(subjectAdapter);
            cursor.close();
            db.close();
        }
    }
}
