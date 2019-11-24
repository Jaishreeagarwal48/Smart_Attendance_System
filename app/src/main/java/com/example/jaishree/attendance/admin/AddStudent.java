package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jaishree.attendance.Adapter.BranchAdapter;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class AddStudent extends Fragment {
    Button buttonAddStudent;
    EditText editTextSName, ediTextSMobile, editTextSEmail, ediTextSParentName, editTextSParentMobile, editTextSParentEmail;
    Spinner spinnerBranchStudent, spinnerSemesterStudent;
    int branch_id, count = 8;
    private static final String PASSWORD = "@#$%^&*abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String enteredName = "", enteredMobile = "", enteredEmail = "", enteredBranch = "", enteredSemester = "", enteredParentName = "", enteredParentMobile = "", enteredParentEmail = "", password = "", parent_password = "";
    private ArrayList<BranchPojo> arrayListBranch = new ArrayList<>();
    private BranchSpinnerAdapter branchSpinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addstudent, null);
        init(view);
        branchSpinnerAdapter = new BranchSpinnerAdapter(getActivity(), R.layout.text_list_item, arrayListBranch, AddStudent.this);
        spinnerBranchStudent.setAdapter(branchSpinnerAdapter);
        methodListener();
        return view;
    }

    private void init(View view) {
        editTextSName = (EditText) view.findViewById(R.id.editTextSName);
        ediTextSMobile = (EditText) view.findViewById(R.id.ediTextSMobile);
        editTextSEmail = (EditText) view.findViewById(R.id.editTextSEmail);
        ediTextSParentName = (EditText) view.findViewById(R.id.ediTextSParentName);
        editTextSParentMobile = (EditText) view.findViewById(R.id.editTextSParentMobile);
        editTextSParentEmail = (EditText) view.findViewById(R.id.editTextSParentEmail);
        spinnerBranchStudent = (Spinner) view.findViewById(R.id.spinnerBranchStudent);
        spinnerSemesterStudent = (Spinner) view.findViewById(R.id.spinnerSemesterStudent);
        buttonAddStudent = (Button) view.findViewById(R.id.buttonAddStudent);
    }

    private void methodListener() {
        getBranchValue();
        spinnerBranchStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                BranchPojo pojo = arrayListBranch.get(position);
                branch_id = pojo.getId();
                enteredBranch = pojo.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSemesterStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredName = editTextSName.getText().toString();
                enteredMobile = ediTextSMobile.getText().toString();
                enteredEmail = editTextSEmail.getText().toString();
                enteredParentName = ediTextSParentName.getText().toString();
                enteredParentMobile = editTextSParentMobile.getText().toString();
                enteredParentEmail = editTextSParentEmail.getText().toString();
                if (enteredName.equals("") | enteredMobile.equals("") | enteredEmail.equals("") | enteredParentName.equals("") | enteredParentMobile.equals("") | enteredParentEmail.equals("") | enteredBranch.equals("Select Branch") | enteredSemester.equals("Select Semester")) {
                    Toast.makeText(getActivity(), "enter all fields", Toast.LENGTH_SHORT).show();
                } else {
                    generatePassword(count);
                    sendMessage();
                    StoreInDatabase();
                    sendParentMessage();
                }
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

    private void generatePassword(int count) {
        StringBuffer buffer = new StringBuffer();
        StringBuffer buffer1 = new StringBuffer();
        while (count != 0) {
            int c = (int) (Math.random() * PASSWORD.length());
            int d = (int) (Math.random() * PASSWORD.length());
            buffer.append(PASSWORD.charAt(c));
            buffer1.append(PASSWORD.charAt(d));
            count--;
        }
        password = buffer.toString();
        parent_password = buffer1.toString();
        Log.d("123", "Password: "+password);
        Log.d("123", "generatePassword: "+parent_password);
    }

    private void sendMessage() {

        String url = "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + enteredMobile + "&message= Email:" + enteredEmail + "Password:" + password + "&sender=ABCDEF&route=4&country=91";

        Log.d("1234", "sendMessage: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), " student message sent", Toast.LENGTH_SHORT).show();

              /*  final Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.postDelayed(this, 30000);
                        Log.d("123", "run: delay");
                    }
                }, 0);
                sendParentMessage();*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error in network"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void sendParentMessage() {
        StringRequest request1 = new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=163118A9mzh8HCaq595493f5&mobiles=" + enteredParentMobile + "&message= Email:" + enteredParentEmail + " Password:" + parent_password + "&sender=ABCDEF&route=4&country=91", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "parent message sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue1 = Volley.newRequestQueue(getActivity());
        queue1.add(request1);
    }

    private void StoreInDatabase() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Student.NAME, enteredName);
        cv.put(Student.MOBILE, enteredMobile);
        cv.put(Student.EMAIL, enteredEmail);
        cv.put(Student.PASSWORD, password);
        cv.put(Student.BRANCH_ID, branch_id);
        cv.put(Student.SEMESTER, enteredSemester);
        cv.put(Student.PARENT_NAME, enteredParentName);
        cv.put(Student.PARENT_MOBILE, enteredParentMobile);
        cv.put(Student.PARENT_EMAIL, enteredParentEmail);
        cv.put(Student.PARENT_PASSWORD, parent_password);
        cv.put(Student.IMAGE,"");
        long l = Student.insert(db, cv);
        if (l > 0) {
            Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
            editTextSName.setText("");
            ediTextSMobile.setText("");
            editTextSEmail.setText("");
            ediTextSParentName.setText("");
            editTextSParentMobile.setText("");
            editTextSParentEmail.setText("");
            spinnerSemesterStudent.setSelection(0);
            spinnerBranchStudent.setSelection(0);
        } else {
            Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
        }
    }

}
