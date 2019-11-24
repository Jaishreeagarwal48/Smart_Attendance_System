package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.BranchAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.branch;

import java.util.ArrayList;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class addBranch extends Fragment {
    BranchAdapter branchAdapter;
     ListView listViewBranch;
    ArrayList<BranchPojo> arrayList=new ArrayList<>();
     EditText branchEditText;
     ImageView addImage;
    public static int rowId;
    public static int clickPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addbranch,null);
        init(view);
        methodListener();
        branchAdapter=new BranchAdapter(getActivity(),R.layout.branch_list_item,arrayList,addBranch.this);
        listViewBranch.setAdapter(branchAdapter);
        fetchFromDatabase();
        return view;
    }


    private void init(View view) {
        branchEditText= (EditText) view.findViewById(R.id.branchEditText);
        listViewBranch= (ListView) view.findViewById(R.id.listViewBranch);
        addImage= (ImageView) view.findViewById(R.id.addImage);
    }
    private void methodListener() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

    }


    private void add() {
        String enteredBranch=branchEditText.getText().toString();
        if(enteredBranch.equals("")){
            branchEditText.setError("enter branch");
        }
        else{
            branchEditText.setError(null);
            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();

            ContentValues cv=new ContentValues();
            cv.put(branch.NAME,enteredBranch);
            long l=branch.insert(db,cv);
            if(l>0){
                Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
                branchEditText.setText("");
                fetchFromDatabase();
            }
            else {
                Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
    }

    public void fetchFromDatabase() {
        arrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor=branch.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);

                BranchPojo pojo=new BranchPojo();
                pojo.setId(id);
                pojo.setName(name);
                arrayList.add(pojo);

            }
            branchAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }

    }


}
