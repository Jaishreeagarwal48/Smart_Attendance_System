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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.TeacherAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.Teacher;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

/**
 * Created by Jaishree on 28-06-2017.
 */

public class ViewTeacher extends Fragment {

    private TeacherAdapter teacherAdapter;
    private ArrayList<TeacherPojo> arrayList=new ArrayList<>();
    private ListView listViewTeacher;
    int branch_id;
     EditText teacherSearch;
    String image="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewteacher,null);
        init(view);
        methodListener();

        fetchFromDatabase();
        return view;
    }

    private void init(View view) {
        listViewTeacher= (ListView) view.findViewById(R.id.listViewTeacher);
        teacherSearch= (EditText) view.findViewById(R.id.teacherSearch);
    }

    private void methodListener() {
        listViewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                showList(position);
            }
        });
        teacherSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text=charSequence.toString();
                teacherAdapter.filter(text);
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
        Cursor cursor= Teacher.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                String mobile=cursor.getString(2);
                String email=cursor.getString(3);
                image=cursor.getString(7);
                String subject_names = "";
                String selection= Teacher.ID + "='" + id + "'";
                ArrayList<SubjectPojo> arrayListSubPojoGTeacher = Teacher.getTeacherSubjects(db,selection);

                for (SubjectPojo pojo : arrayListSubPojoGTeacher)
                {
                    subject_names += pojo.getName()+",";

                }
                subject_names=subject_names.substring(0,subject_names.length()-1);
                String qualification=cursor.getString(5);
                String password=cursor.getString(6);

                TeacherPojo pojo=new TeacherPojo();
              /*  String selection= Subject.ID + "='" + subject_id + "'";
                Cursor subjectCursor= Subject.select(db,selection);
                if(subjectCursor!=null && subjectCursor.moveToNext()){
                    pojo.setSubject_name(subjectCursor.getString(1));
                     branch_id=subjectCursor.getInt(2);
                    pojo.setBranch_id(branch_id);
                    pojo.setSemester(subjectCursor.getString(3));
                }
                subjectCursor.close();

                String sel= branch.ID + "='" + branch_id + "'";
                Cursor branchCursor= branch.select(db,sel);
                if(branchCursor!=null && branchCursor.moveToNext()){
                    pojo.setBranch_name(branchCursor.getString(1));
                }
                branchCursor.close();
                */
                pojo.setId(id);
                pojo.setName(name);
                pojo.setMobile(mobile);
                pojo.setEmail(email);
                pojo.setSubject_name(subject_names);
                pojo.setQualification(qualification);
                pojo.setPassword(password);
                pojo.setImage(image);

                Log.d("1234", "fetchFromDatabase: teacher"+pojo.toString());

                arrayList.add(pojo);
            }
            teacherAdapter=new TeacherAdapter(getActivity(),R.layout.viewteacher,arrayList,ViewTeacher.this);
            listViewTeacher.setAdapter(teacherAdapter);
            cursor.close();
            db.close();
        }
    }

    private void showList(int position) {
        TeacherPojo pojo=arrayList.get(position);
        Dialog dialog=new Dialog(getActivity());
        LayoutInflater inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.teacher_layout,null);
        dialog.setTitle("Teacher Details");

        TextView teacherDialogName= (TextView) view.findViewById(R.id.teacherDialogName);
        TextView teacherDialogMobile= (TextView) view.findViewById(R.id.teacherDialogMobile);
        TextView teacherDialogEmail= (TextView) view.findViewById(R.id.teacherDialogEmail);
     //   TextView teacherDialogBranch= (TextView) view.findViewById(R.id.teacherDialogBranch);
      //  TextView teacherDialogSemester= (TextView) view.findViewById(R.id.teacherDialogSemester);
        TextView teacherDialogSubject= (TextView) view.findViewById(R.id.teacherDialogSubject);
        TextView teacherDialogQualification= (TextView) view.findViewById(R.id.teacherDialogQualification);

        teacherDialogName.setText("Name:"+pojo.getName());
        teacherDialogMobile.setText("Mobile:"+pojo.getMobile());
        teacherDialogEmail.setText("Email:"+pojo.getEmail());
       // teacherDialogBranch.setText("Branch:"+pojo.getBranch_name());
        //teacherDialogSemester.setText("Semester"+pojo.getSemester());
        teacherDialogSubject.setText("Subject:"+pojo.getSubject_name());
        teacherDialogQualification.setText("Qualification:"+pojo.getQualification());
        dialog.setContentView(view);
        dialog.show();
    }

}
