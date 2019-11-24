package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Jaishree on 14-07-2017.
 */

public class AttendanceAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private ArrayList<SubjectPojo> attendanceArrayList;
    private LayoutInflater inflater;
    public AttendanceAdapter(Context context,int layoutResource,ArrayList<SubjectPojo> attendanceArrayList) {
        super(context, layoutResource, attendanceArrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.attendanceArrayList = attendanceArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.attendance_list_item, null);
        TextView attendanceSubject = (TextView) view.findViewById(R.id.attendanceSubject);
        TextView attendanceSemester = (TextView) view.findViewById(R.id.attendanceSemester);
        TextView attendanceBranch = (TextView) view.findViewById(R.id.attendanceBranch);
        TextView attendanceOval= (TextView) view.findViewById(R.id.attendanceOval);

        GradientDrawable d= (GradientDrawable) attendanceOval.getBackground();
        int red= (int) (Math.random()*255);int green= (int) (Math.random()*255);int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        SubjectPojo pojo = attendanceArrayList.get(position);
        attendanceSubject.setText(pojo.getName());
        attendanceSemester.setText(pojo.getSemester());
        attendanceBranch.setText(pojo.getBranch_name());
        attendanceOval.setText((""+pojo.getName().charAt(0)).toUpperCase());

        return view;
    }
}
