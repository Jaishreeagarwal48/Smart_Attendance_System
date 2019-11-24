package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Jaishree on 17-07-2017.
 */

public class ParentAttendanceAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<TakeAttendancePojo> attendanceArrayList;
    private LayoutInflater inflater;

    public ParentAttendanceAdapter(Context context, int layoutResource, ArrayList<TakeAttendancePojo> attendanceArrayList) {
        super(context, layoutResource, attendanceArrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.attendanceArrayList = attendanceArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.parent_attendance_list_item,null);
        TextView subjectTextView = (TextView) view.findViewById(R.id.subjectTextView);
        TextView presentTextView = (TextView) view.findViewById(R.id.presentTextView);
        TextView absentTextView = (TextView) view.findViewById(R.id.absentTextView);
        TextView leaveTextView = (TextView) view.findViewById(R.id.leaveTextView);
        TextView studentTextView= (TextView) view.findViewById(R.id.studentTextView);

        GradientDrawable d= (GradientDrawable) studentTextView.getBackground();
        int red= (int) (Math.random()*255);int green= (int) (Math.random()*255);int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        TakeAttendancePojo pojo = attendanceArrayList.get(position);
        subjectTextView.setText(pojo.getSubject_name());

        presentTextView.setText("P:"+pojo.getP_count());
        absentTextView.setText("A:"+pojo.getA_count());
        leaveTextView.setText("L:"+pojo.getL_count());
        studentTextView.setText((""+pojo.getSubject_name().charAt(0)).toUpperCase());

        return view;
    }
}
