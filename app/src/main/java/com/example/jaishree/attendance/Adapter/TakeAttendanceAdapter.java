package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaishree on 14-07-2017.
 */

public class TakeAttendanceAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private ArrayList<TakeAttendancePojo> arrayList;
    private LayoutInflater inflater;
    int rowId;

    public TakeAttendanceAdapter(Context context, int layoutResource, ArrayList<TakeAttendancePojo> arrayList) {
        super(context, layoutResource, arrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.take_attendance_list_item, null);

        TextView attendanceStudentName = (TextView) view.findViewById(R.id.attendanceStudentName);
        TextView attendanceStudentMobile = (TextView) view.findViewById(R.id.attendanceStudentMobile);
        RadioGroup attendanceRadioGroup= (RadioGroup) view.findViewById(R.id.attendanceRadioGroup);
        RadioButton radioButtonPresent = (RadioButton) view.findViewById(R.id.radioButtonPresent);
        RadioButton radioButtonAbsent = (RadioButton) view.findViewById(R.id.radioButtonAbsent);
        RadioButton radioButtonLeave = (RadioButton) view.findViewById(R.id.radioButtonLeave);
        TextView studentTextView= (TextView) view.findViewById(R.id.studentTextView);
        CircleImageView studentCircleImage= (CircleImageView) view.findViewById(R.id.studentCircleImage);
        GradientDrawable d= (GradientDrawable) studentTextView.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        final TakeAttendancePojo pojo = arrayList.get(position);
        rowId=pojo.getId();
        attendanceStudentName.setText(pojo.getName());
        attendanceStudentMobile.setText(pojo.getMobile());
        studentTextView.setText((""+pojo.getName().charAt(0)).toUpperCase());

        switch (pojo.getStatus()) {
            case "present":
                radioButtonPresent.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "absent":
                radioButtonAbsent.setChecked(true);
                radioButtonAbsent.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "leave":
                radioButtonLeave.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            default:
                radioButtonPresent.setChecked(false);
                radioButtonAbsent.setChecked(false);
                radioButtonLeave.setChecked(false);
                Log.d("123", "status: " + pojo.getStatus());
                break;
        }
        attendanceRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonPresent:
                        pojo.setStatus("present");
                        Log.d("123", "onCheckedChanged: "+pojo.getStatus());
                        break;
                    case R.id.radioButtonAbsent:
                        pojo.setStatus("absent");
                        Log.d("123", "onCheckedChanged: "+pojo.getStatus());
                        break;
                    case R.id.radioButtonLeave:
                        pojo.setStatus("leave");
                        Log.d("123", "onCheckedChanged: "+pojo.getStatus());
                        break;
                    default:
                        radioGroup.clearCheck();
                        break;
                }
            }
        });
        return view;
    }
}
