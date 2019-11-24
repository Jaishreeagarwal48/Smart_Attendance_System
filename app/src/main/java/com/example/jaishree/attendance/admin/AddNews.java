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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.NewsAdapter;
import com.example.jaishree.attendance.Pojo.NewsPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jaishree on 03-07-2017.
 */

public class AddNews extends Fragment {
    EditText newsEditText;
    String currentDate,currentTime;
    NewsAdapter newsAdapter;
    ArrayList<NewsPojo> arrayList=new ArrayList<>();
    ListView listViewNews;
    Button newsBtn;
    public static int rowId;
    public static int clickPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addnews,null);
        init(view);
        methodListener();
        newsAdapter=new NewsAdapter(getActivity(),R.layout.addnews,arrayList,AddNews.this);
        listViewNews.setAdapter(newsAdapter);
        fetchFromDatabase();
        return view;
    }


    private void init(View view) {
        newsEditText= (EditText) view.findViewById(R.id.newsEditText);
        newsBtn= (Button) view.findViewById(R.id.newsBtn);
        listViewNews= (ListView) view.findViewById(R.id.listViewNews);
    }

    private void methodListener() {
        newsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add();
                    }
                });
            }
        });
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                rowId=arrayList.get(position).getId();
                clickPosition=position;

            }
        });
    }

    private void add() {
        String enteredNews=newsEditText.getText().toString();
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);

        currentDate= dayOfMonth + "-" +month + "-" + year;
        currentTime= hour + ":" + minute;

        if(enteredNews.equals("")){
            newsEditText.setError("enter valid news");
        }
        else{
            newsEditText.setError(null);
            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(News.NAME,enteredNews);
            cv.put(News.DATE,currentDate);
            cv.put(News.TIME,currentTime);

            long l=News.insert(db,cv);
            if(l>0){
                Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
                newsEditText.setText("");
                fetchFromDatabase();
            }
            else{
                Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void fetchFromDatabase() {
        arrayList.clear();

        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor=News.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                String name=cursor.getString(1);
                String date=cursor.getString(2);
                String time=cursor.getString(3);

                NewsPojo pojo=new NewsPojo();
                pojo.setId(id);
                pojo.setName(name);
                pojo.setDate(date);
                pojo.setTime(time);
                arrayList.add(pojo);
            }
            newsAdapter.notifyDataSetChanged();
            cursor.close();
            db.close();
        }
    }



}



