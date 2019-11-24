package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.Pojo.StudentPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.ViewStudent;
import com.example.jaishree.attendance.admin.ViewTeacher;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaishree on 07-07-2017.
 */

public class StudentAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private String image="";
    private Fragment fragment;
    private LayoutInflater inflater;
    private ArrayList<StudentPojo> arrayList;
    private ArrayList<StudentPojo> filterArrayList=new ArrayList<>();
    public StudentAdapter(Context context,int layoutResource,ArrayList<StudentPojo> arrayList,Fragment fragment){
        super(context,layoutResource,arrayList);
        this.context=context;
        this.layoutResource=layoutResource;
        this.fragment=fragment;
        this.arrayList=arrayList;
        filterArrayList.addAll(arrayList);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.teacher_list_item,null);
        TextView teacherImageView= (TextView) view.findViewById(R.id.teacherImageView);
        CircleImageView circleImageView= (CircleImageView) view.findViewById(R.id.circleImageView);
        TextView teacherName= (TextView) view.findViewById(R.id.teacherName);
        TextView teacherMobile= (TextView) view.findViewById(R.id.teacherMobile);
        TextView teacherSubject= (TextView) view.findViewById(R.id.teacherSubject);
        ImageView teacherDelete= (ImageView) view.findViewById(R.id.teacherDelete);

        GradientDrawable d= (GradientDrawable) teacherImageView.getBackground();
        int red= (int) (Math.random()*255);int green= (int) (Math.random()*255);int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        StudentPojo pojo=arrayList.get(position);
       final int listRowId=pojo.getId();
        image=pojo.getImage();
        if(image.equals("")){
            circleImageView.setVisibility(View.GONE);
            teacherImageView.setVisibility(View.VISIBLE);
            teacherImageView.setText((""+pojo.getName().charAt(0)).toUpperCase());
        }
        else{
            teacherImageView.setVisibility(View.GONE);
            circleImageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(image).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
        }

        teacherName.setText(pojo.getName());
        teacherMobile.setText(pojo.getMobile());
        teacherSubject.setText(pojo.getBranch_name());

        teacherDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(listRowId);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });
        return view;
    }

    private void delete(int listRowId) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
        SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
        String selection= Student.ID + "='" + listRowId + "'";
        int d=Student.delete(db,selection);
        if(d>0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            ((ViewStudent)fragment).fetchFromDatabase();
        }
        else{
            Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void filter(String charText){
        arrayList.clear();
        if(charText.length()==0){
            arrayList.addAll(filterArrayList);
        }
        else{
            for(StudentPojo pojo : filterArrayList){
                if(pojo.getName().toUpperCase().contains(charText.toUpperCase()) | pojo.getBranch_name().toUpperCase().contains(charText.toUpperCase()) | pojo.getSemester().toUpperCase().contains(charText.toUpperCase())){
                    arrayList.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }
}

