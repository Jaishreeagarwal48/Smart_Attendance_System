package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class BranchSpinnerAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<BranchPojo> arrayListBranch;
    private Fragment fragment;
    private LayoutInflater inflater;

    public BranchSpinnerAdapter(Context context,int layoutResource,ArrayList<BranchPojo> arrayListBranch,Fragment fragment){
        super(context,layoutResource,arrayListBranch);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayListBranch=arrayListBranch;
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.text_list_item,null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        BranchPojo pojo1=arrayListBranch.get(0);
        textView.setText(pojo1.getName());
        BranchPojo pojo=arrayListBranch.get(position);
        textView.setText(pojo.getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.text_list_item,null);
        TextView textView= (TextView) view.findViewById(R.id.textView);
        BranchPojo pojo1=arrayListBranch.get(0);
        textView.setText(pojo1.getName());
        BranchPojo pojo=arrayListBranch.get(position);
        textView.setText(pojo.getName());

        return view;
    }
}
