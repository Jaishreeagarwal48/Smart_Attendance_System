package com.example.jaishree.attendance.Teacher;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.ViewPagerAdapter;
import com.example.jaishree.attendance.MainActivity;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

public class TeacherPanel extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons={R.drawable.newsic,
            R.drawable.studentattendanceicon};
    int teacherId;
    String image="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcon();
    }
    private void setupTabIcon() {
        TextView tabOne= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabOne.setText(" News");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(R.drawable.newsic,0,0,0);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        TextView tabTwo= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
        tabTwo.setText(" Attendance");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.studentattendanceicon,0,0,0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ViewNews()," News");
        adapter.addFragment(new TakeAttendance()," Attendance");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.teacherLogout){
            SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preference.edit();
            editor.putBoolean("isTeacherLogin",false);
            editor.commit();
            Intent i=new Intent(TeacherPanel.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if(id==R.id.teacherChangePassword){
            Intent i=new Intent(TeacherPanel.this,TeacherOtp.class);
            startActivity(i);
        }
        if(id==R.id.teacherEditProfile){
            Intent i=new Intent(TeacherPanel.this, TeacherEditProfile.class);
            startActivity(i);
        }
        if(id==R.id.teacherRemovePic){
            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
            SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
            getTeacherId();
            String selection= Teacher.ID + "='" + teacherId + "'";
            image="";
            ContentValues cv=new ContentValues();
            cv.put(Teacher.IMAGE,image);
            int u=Teacher.update(db,cv,selection);
            if(u>0){
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "not updated", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTeacherId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        teacherId=preference.getInt("teacherId",0);
    }
}
