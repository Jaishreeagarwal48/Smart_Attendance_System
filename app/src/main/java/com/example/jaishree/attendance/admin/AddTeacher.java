package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Adapter.SubjectSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.Teacher;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class AddTeacher extends Fragment {

    Button buttonAddTeacher;
    EditText editTextTName, editTextTMobile, editTextTEmail;
    Spinner qualificationSpinnerTeacher, semesterSpinnerTeacher, branchSpinnerTeacher;
    private ArrayList<BranchPojo> arrayListBranch = new ArrayList<>();
    private ArrayList<SubjectPojo> arrayListSubject = new ArrayList<>();
    TextView subjectTextViewTeacher;
    private BranchSpinnerAdapter branchSpinnerAdapter;
    private SubjectSpinnerAdapter subjectSpinnerAdapter;
    private static final String PASSWORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@#$%^&*0123456789";
    private int count = 8;
    int branch_id, subject_id;
    String password = "", enteredSemester = "", enteredName = "", enteredMobile = "", enteredEmail = "", enteredSubject = "", enteredBranch = "", enteredQualification = "";
    String selectedSubjectId="",databaseSubjectId="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addteacher, null);
        init(view);
        branchSpinnerAdapter = new BranchSpinnerAdapter(getActivity(), R.layout.text_list_item, arrayListBranch, AddTeacher.this);
        branchSpinnerTeacher.setAdapter(branchSpinnerAdapter);
        subjectSpinnerAdapter = new SubjectSpinnerAdapter(getActivity(), R.layout.text_list_item, arrayListSubject, AddTeacher.this);
        // subjectTextViewTeacher.setAdapter(subjectSpinnerAdapter);
        methodListener();

        return view;
    }

    private void init(View view) {
        buttonAddTeacher = (Button) view.findViewById(R.id.buttonAddTeacher);
        editTextTName = (EditText) view.findViewById(R.id.editTextTName);
        editTextTEmail = (EditText) view.findViewById(R.id.editTextTEmail);
        editTextTMobile = (EditText) view.findViewById(R.id.editTextTMobile);
        qualificationSpinnerTeacher = (Spinner) view.findViewById(R.id.qualificationSpinnerTeacher);
        semesterSpinnerTeacher = (Spinner) view.findViewById(R.id.semesterSpinnerTeacher);
        branchSpinnerTeacher = (Spinner) view.findViewById(R.id.branchSpinnerTeacher);
        subjectTextViewTeacher = (TextView) view.findViewById(R.id.subjectTextViewTeacher);
    }

    private void methodListener() {
        getBranchValue();
        branchSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                BranchPojo pojo = arrayListBranch.get(position);
                branch_id = pojo.getId();
                enteredBranch = pojo.getName();
                Log.d("123", "onItemSelected: " + pojo);
                getSubjectValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        semesterSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester = adapterView.getItemAtPosition(i).toString();
                Log.d("123", "semester: " + enteredSemester);
                getSubjectValue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        qualificationSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredQualification = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredName = editTextTName.getText().toString();
                enteredMobile = editTextTMobile.getText().toString();
                enteredEmail = editTextTEmail.getText().toString();
                databaseSubjectId=selectedSubjectId.substring(0,selectedSubjectId.length()-1);
                Log.d("123", "databaseSubjectId: "+databaseSubjectId);
                if (enteredName.equals("") | enteredMobile.equals("") | enteredEmail.equals("") | enteredSemester.equals("Select Semester") | enteredBranch.equals("Select Branch") | enteredSubject.equals("Select Subject") | enteredQualification.equals("Select Qualification")) {
                    Toast.makeText(getActivity(), "enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    generatePassword(count);
                    sendMessage();
                    storeInDatabase();

                }

            }
        });

        subjectTextViewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] subNameArray = new String[arrayListSubject.size()];
                final boolean[] selectedState = new boolean[arrayListSubject.size()];
                final int[] subId=new  int[arrayListSubject.size()];
                for (int i = 0; i < arrayListSubject.size(); i++) {
                    subNameArray[i] = arrayListSubject.get(i).getName();
                    selectedState[i] = false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Subjects").setMultiChoiceItems(subNameArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        selectedState[i] = b;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int po) {
                        boolean flag = false;
                        for (int i = 0; i < selectedState.length; i++) {
                            if (selectedState[i]) {
                                subjectTextViewTeacher.append(subNameArray[i] + ",");
                                subId[i]=arrayListSubject.get(i).getId();
                                Log.d("123", "subId[i]: "+subId[i]);
                                    selectedSubjectId+=subId[i]+",";
                                Log.d("123", "selectedSubjectId: "+selectedSubjectId);
                                flag = true;
                            }
                        }
                      //  if (flag)
                        //    subjectTextViewTeacher.setText(subjectTextViewTeacher.getText().toString().substring(0, subjectTextViewTeacher.getText().toString().length() - 1));
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

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
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                BranchPojo pojo = new BranchPojo();
                pojo.setId(id);
                pojo.setName(name);
                arrayListBranch.add(pojo);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }
    }

    private void getSubjectValue() {
        arrayListSubject.clear();
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();

        String selection = Subject.BRANCH_ID + "='" + branch_id + "' AND " + Subject.SEMESTER + "='" + enteredSemester + "'";
        Cursor cursor = Subject.select(db, selection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int b_id = cursor.getInt(2);
                String sem = cursor.getString(3);

                SubjectPojo pojo = new SubjectPojo();
                pojo.setId(id);
                pojo.setName(name);
                pojo.setBranch_id(b_id);
                pojo.setSemester(sem);
                arrayListSubject.add(pojo);
            }
            subjectSpinnerAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }

    }

    private void generatePassword(int count) {
        StringBuffer buffer = new StringBuffer();
        while (count != 0) {
            int c = (int) (Math.random() * PASSWORD.length());
            Log.d("123", "generatePassword: " + c);
            buffer.append(PASSWORD.charAt(c));
            count--;
        }
        password = buffer.toString();
        Log.d("123", "generatePassword: " + password);
    }

    private void sendMessage() {
        StringRequest request = new StringRequest(Request.Method.GET, " https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + enteredMobile + "&message= Email:" + enteredEmail + "Password:" + password + "&sender=ABCDEF&route=4&country=91", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("123", "onResponse: sent");
                Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void storeInDatabase() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Teacher.NAME, enteredName);
        cv.put(Teacher.MOBILE, enteredMobile);
        cv.put(Teacher.EMAIL, enteredEmail);
        cv.put(Teacher.QUALIFICATION, enteredQualification);
        cv.put(Teacher.SUBJECT_ID, databaseSubjectId);
        cv.put(Teacher.PASSWORD, password);
        cv.put(Teacher.IMAGE,"");

        Log.d("1234", "storeInDatabase: "+ Arrays.asList(cv));

        long l = Teacher.insert(db, cv);
        if (l > 0) {
            Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
            Log.d("123", "storeInDatabase: inserted");
        } else {
            Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
        }
        editTextTName.setText("");
        editTextTMobile.setText("");
        editTextTEmail.setText("");
        subjectTextViewTeacher.setText("");
        semesterSpinnerTeacher.setSelection(0);
        branchSpinnerTeacher.setSelection(0);
        qualificationSpinnerTeacher.setSelection(0);

    }
}
