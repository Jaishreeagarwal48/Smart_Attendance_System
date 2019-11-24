package com.example.jaishree.attendance.Adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.Pojo.NewsPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.AddNews;
import com.example.jaishree.attendance.admin.addBranch;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

import java.util.ArrayList;

/**
 * Created by Jaishree on 03-07-2017.
 */

public class NewsAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private LayoutInflater inflater;
    private ArrayList<NewsPojo> arrayList;
    private Fragment fragment;
    public NewsAdapter(Context context,int layoutResource,ArrayList<NewsPojo> arrayList,Fragment fragment){
        super(context,layoutResource,arrayList);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayList=arrayList;
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.news_list_item,null);
        TextView textViewNews= (TextView) view.findViewById(R.id.textViewNews);
        ImageView newsDelete= (ImageView) view.findViewById(R.id.newsDelete);
        ImageView newsUpdate= (ImageView) view.findViewById(R.id.newsUpdate);

       final NewsPojo pojo=arrayList.get(position);
       final int listRowId=pojo.getId();
        textViewNews.setText(pojo.getName());
        newsDelete.setOnClickListener(new View.OnClickListener() {
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

        newsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(listRowId,position);
            }
        });


        return view;
    }

    private void delete(int listRowId) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
        SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
        String selection= News.ID + "='" + listRowId + "'" ;
        int d=News.delete(db,selection);
        if(d>0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            ((AddNews)fragment).fetchFromDatabase();
        }
        else{
            Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
        }

    }

    private void update(final int listRowId, final int position) {
        NewsPojo dialogPojo=arrayList.get(position);
        final Dialog dialog=new Dialog(context);
        dialog.setTitle("Update News");
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.branch_dialog,null);
        final EditText branchDialogEditText= (EditText) view.findViewById(R.id.branchDialogEditText);
        ImageView branchDialogAdd= (ImageView) view.findViewById(R.id.branchDialogAdd);

        branchDialogEditText.setText(dialogPojo.getName());

        branchDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogEnteredText=branchDialogEditText.getText().toString();
                if(dialogEnteredText.equals("")){
                    Toast.makeText(context, "fill branch", Toast.LENGTH_SHORT).show();
                }
                else{
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
                    SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();

                    ContentValues cv=new ContentValues();
                    cv.put(News.NAME,dialogEnteredText);
                    String selection= News.ID + "='" + listRowId + "'" ;
                    long l=News.update(db,cv,selection);
                    if(l>0){
                        dialog.cancel();
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                        ((AddNews)fragment).fetchFromDatabase();
                    }
                    else{
                        Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
