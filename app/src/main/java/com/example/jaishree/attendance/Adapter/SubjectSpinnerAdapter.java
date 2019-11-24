package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.table.Subject;

import java.util.ArrayList;

/**
 * Created by Jaishree on 05-07-2017.
 */

public class SubjectSpinnerAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private LayoutInflater inflater;
    private ArrayList<SubjectPojo> arrayListSubject;
    private Fragment fragment;
    public SubjectSpinnerAdapter(Context context,int layoutResource,ArrayList<SubjectPojo> arrayListSubject,Fragment fragment){
        super(context,layoutResource,arrayListSubject);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayListSubject=arrayListSubject;
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.text_list_item,null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        SubjectPojo pojo=arrayListSubject.get(position);
        textView.setText(pojo.getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.text_list_item,null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        SubjectPojo pojo1=arrayListSubject.get(0);
        textView.setText(pojo1.getName());
        SubjectPojo pojo=arrayListSubject.get(position);
        textView.setText(pojo.getName());
        return view;
    }
}
