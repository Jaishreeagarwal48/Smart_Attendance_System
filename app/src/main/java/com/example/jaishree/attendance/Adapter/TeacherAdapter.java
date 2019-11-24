package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.ViewTeacher;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaishree on 05-07-2017.
 */

public class TeacherAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private String image;
    private ArrayList<TeacherPojo> arrayList;
    private LayoutInflater inflater;
    private Fragment fragment;
    private ArrayList<TeacherPojo> filterArrayList=new ArrayList<>();
    public TeacherAdapter(Context context,int layoutResource,ArrayList<TeacherPojo> arrayList,Fragment fragment){
        super(context,layoutResource,arrayList);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayList=arrayList;
        this.fragment=fragment;
        filterArrayList.addAll(arrayList);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        Log.d("123", "getCount:array "+arrayList.size());
        Log.d("123", "getCount:filter "+filterArrayList.size());
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.teacher_list_item,null);
        TextView teacherImageView= (TextView) view.findViewById(R.id.teacherImageView);
        TextView teacherName= (TextView) view.findViewById(R.id.teacherName);
        TextView teacherMobile= (TextView) view.findViewById(R.id.teacherMobile);
        TextView teacherSubject= (TextView) view.findViewById(R.id.teacherSubject);
        ImageView teacherDelete= (ImageView) view.findViewById(R.id.teacherDelete);
        CircleImageView circleImageView= (CircleImageView) view.findViewById(R.id.circleImageView);

        GradientDrawable d= (GradientDrawable) teacherImageView.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        TeacherPojo pojo=arrayList.get(position);
        image=pojo.getImage();
        if(image.equals("")) {
            teacherImageView.setText(("" + pojo.getName().charAt(0)).toUpperCase());
        }
        else {
            teacherImageView.setVisibility(View.GONE);
            Glide.with(context).load(image).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(circleImageView);
        }
        teacherName.setText(pojo.getName());
        teacherMobile.setText(pojo.getMobile());
        teacherSubject.setText(pojo.getSubject_name());
        final int listRowId=pojo.getId();

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
        String selection=Teacher.ID + "='" + listRowId + "'";
        int d=Teacher.delete(db,selection);
        if(d>0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            ((ViewTeacher)fragment).fetchFromDatabase();
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
            for(TeacherPojo pojo : filterArrayList){
                if(pojo.getName().toUpperCase().contains(charText.toUpperCase()) | pojo.getSubject_name().toUpperCase().contains(charText.toUpperCase())){
                    arrayList.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }
}
