package com.example.jaishree.attendance.Teacher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.lib.widget.verticalmarqueetextview.VerticalMarqueeTextView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

/**
 * Created by Jaishree on 04-07-2017.
 */

public class ViewNews extends Fragment {
    VerticalMarqueeTextView marqueeTextView;
    public String enteredNews="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewnews,null);
        init(view);
        marqueeTextView.setSelected(true);
        showNews();
        return view;
    }


    private void init(View view) {
        marqueeTextView= (VerticalMarqueeTextView) view.findViewById(R.id.marqueeTextView);
    }

    private void showNews() {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor= News.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                String news = cursor.getString(1);
                enteredNews+="\u2022 "+news+"\n\n";
                marqueeTextView.setText(enteredNews);
            }
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("123", "onResume: resume");
        marqueeTextView.startMarquee();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("123", "onStop: stop");
        marqueeTextView.stopMarquee();
    }

    @Override
    public void onPause() {
        super.onPause();
       // marqueeTextView.setEllipsize(TextUtils.TruncateAt.END);
        Log.d("123", "onPause: pause");
        marqueeTextView.stopMarquee();
    }

}
