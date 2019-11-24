package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaishree on 17-07-2017.
 */

public class SubjectAttendanceAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<TakeAttendancePojo> arrayList;
    private LayoutInflater inflater;
    int rowId;

    public SubjectAttendanceAdapter(Context context, int layoutResource, ArrayList<TakeAttendancePojo> arrayList) {
        super(context, layoutResource, arrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.subject_attendance_list_item, null);

        TextView attendanceStudentName = (TextView) view.findViewById(R.id.attendanceStudentName);
        TextView attendanceStudentStatus= (TextView) view.findViewById(R.id.attendanceStudentStatus);
        TextView attendanceStudentDate= (TextView) view.findViewById(R.id.attendanceStudentDate);

        TextView studentTextView= (TextView) view.findViewById(R.id.studentTextView);
        CircleImageView studentCircleImage= (CircleImageView) view.findViewById(R.id.studentCircleImage);
        GradientDrawable d= (GradientDrawable) studentTextView.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        final TakeAttendancePojo pojo = arrayList.get(position);
        rowId=pojo.getId();
        attendanceStudentName.setText(pojo.getSubject_name());
        studentTextView.setText((""+pojo.getSubject_name().charAt(0)).toUpperCase());
        attendanceStudentStatus.setText(pojo.getStatus());
        attendanceStudentDate.setText(pojo.getDate());

        switch (pojo.getStatus()) {
            case "present":
                attendanceStudentStatus.setTextColor(Color.GREEN);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "absent":
                attendanceStudentStatus.setTextColor(Color.RED);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "leave":
                attendanceStudentStatus.setTextColor(Color.YELLOW);

                Log.d("123", "status: " + pojo.getStatus());
                break;
            default:

                //Log.d("123", "status: " + pojo.getStatus());
                break;
        }
        return view;
    }
}
