package com.example.jaishree.attendance.Adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.ViewSubject;
import com.example.jaishree.attendance.admin.addBranch;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

import static com.example.jaishree.attendance.R.id.branchDialogEditText;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class SubjectAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<SubjectPojo> arrayList;
    private Fragment fragment;
    private LayoutInflater inflater;
    private ArrayList<SubjectPojo> filterArrayList=new ArrayList<>();

    public SubjectAdapter(Context context,int layoutResource,ArrayList<SubjectPojo> arrayList,Fragment fragment){
        super(context,layoutResource,arrayList);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayList=arrayList;
        this.fragment=fragment;
        filterArrayList.addAll(arrayList);
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.subject_list_item,null);
        TextView subjectTextView= (TextView) view.findViewById(R.id.subjectTextView);
        TextView semesterTextView= (TextView) view.findViewById(R.id.semesterTextView);
        TextView branchTextView= (TextView) view.findViewById(R.id.branchTextView);
        ImageView subjectDelete= (ImageView) view.findViewById(R.id.subjectDelete);

       final SubjectPojo pojo=arrayList.get(position);
        final int listRowId=pojo.getId();
        subjectTextView.setText(pojo.getName());
        semesterTextView.setText(pojo.getSemester());
        branchTextView.setText(pojo.getBranch_name());


        subjectDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("1234", "onClick: "+pojo);
                                Log.d("1234", "onClick: "+listRowId);
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
        String selection= Subject.ID + "='" + listRowId + "'" ;

        int d=Subject.delete(db,selection);
        if(d>0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            ((ViewSubject)fragment).fetchFromDatabase();
        }
        else {
            Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
        }
    }
    public void getFilter(String charText){
        arrayList.clear();
        if(charText.length()==0){
            arrayList.addAll(filterArrayList);
        }
        else{
            for(SubjectPojo pojo : filterArrayList){
                if(pojo.getName().toUpperCase().contains(charText.toUpperCase()) | pojo.getBranch_name().toUpperCase().contains(charText.toUpperCase()) | pojo.getSemester().toUpperCase().contains(charText.toUpperCase())){
                    arrayList.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }

}
