package com.example.jaishree.attendance.admin;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.StudentAdapter;
import com.example.jaishree.attendance.Pojo.StudentPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

/**
 * Created by Jaishree on 28-06-2017.
 */

public class ViewStudent extends Fragment {

    private StudentAdapter studentAdapter;
    private ArrayList<StudentPojo> arrayList=new ArrayList<>();
    ListView listViewStudent;
    int branch_id;
    String branch_name="",image="";
    EditText studentSearch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewstudent,null);
        init(view);
        methodListener();
        fetchFromDatabase();
        return view;

    }

    private void init(View view) {
        listViewStudent= (ListView) view.findViewById(R.id.listViewStudent);
        studentSearch= (EditText) view.findViewById(R.id.studentSearch);
    }

    private void methodListener() {
        listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showDialog(position);
            }
        });
        studentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text=charSequence.toString();
                studentAdapter.filter(text);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void fetchFromDatabase() {
        arrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor= Student.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                StudentPojo pojo=new StudentPojo();
                pojo.setId(cursor.getInt(0));
                pojo.setName(cursor.getString(1));
                pojo.setMobile(cursor.getString(2));
                pojo.setEmail(cursor.getString(3));
                pojo.setParent_name(cursor.getString(4));
                pojo.setParent_mobile(cursor.getString(5));
                pojo.setParent_email(cursor.getString(6));
                branch_id=cursor.getInt(7);
                pojo.setBranch_id(branch_id);
                pojo.setSemester(cursor.getString(8));
                pojo.setPassword(cursor.getString(9));
                pojo.setParent_password(cursor.getString(10));
                image=cursor.getString(11);
                pojo.setImage(image);

                String selection= branch.ID + "='" + branch_id + "'";
                Cursor cursor1=branch.select(db,selection);
                if(cursor1!=null && cursor1.moveToNext()){
                    branch_name=cursor1.getString(1);
                    pojo.setBranch_name(branch_name);
                }
                cursor1.close();
                arrayList.add(pojo);

            }
            studentAdapter = new StudentAdapter(getActivity(),R.layout.viewstudent,arrayList,ViewStudent.this);
            listViewStudent.setAdapter(studentAdapter);
            cursor.close();
            db.close();
        }
    }

    private void showDialog(int position) {
        StudentPojo pojo=arrayList.get(position);
        Dialog dialog=new Dialog(getActivity());
        dialog.setTitle("Student Details");
        LayoutInflater inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.student_dialog,null);

        TextView studentDialogName= (TextView) view.findViewById(R.id.studentDialogName);
        TextView studentDialogMobile= (TextView) view.findViewById(R.id.studentDialogMobile);
        TextView studentDialogEmail= (TextView) view.findViewById(R.id.studentDialogEmail);
        TextView studentDialogBranch= (TextView) view.findViewById(R.id.studentDialogBranch);
        TextView studentDialogSemester= (TextView) view.findViewById(R.id.studentDialogSemester);
        TextView studentDialogPName= (TextView) view.findViewById(R.id.studentDialogPName);
        TextView studentDialogPMobile= (TextView) view.findViewById(R.id.studentDialogPMobile);
        TextView studentDialogPEmail= (TextView) view.findViewById(R.id.studentDialogPEmail);

        studentDialogName.setText("Student Name: "+pojo.getName());
        studentDialogMobile.setText("Student Mobile: "+pojo.getMobile());
        studentDialogEmail.setText("Student Email: "+pojo.getEmail());
        studentDialogBranch.setText("Branch: "+pojo.getBranch_name());
        studentDialogSemester.setText("Semester: "+pojo.getSemester());
        studentDialogPName.setText("Parent Name: "+pojo.getParent_name());
        studentDialogPMobile.setText("Parent Mobile: "+pojo.getParent_mobile());
        studentDialogPEmail.setText("Parent Email: "+pojo.getParent_email());

        dialog.setContentView(view);
        dialog.show();
    }

}
