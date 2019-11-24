package com.example.jaishree.attendance.Student;

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
import com.example.jaishree.attendance.Parent.ParentViewAttendance;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.StudentViewAttendance;
import com.example.jaishree.attendance.Teacher.ViewNews;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

public class StudentPanel extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons={R.drawable.newsic,
            R.drawable.studentattendanceicon};
    int studentId;
    String image="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);
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
        adapter.addFragment(new StudentViewAttendance()," Attendance");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.studentLogout){
            SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preference.edit();
            editor.putBoolean("isStudentLogin",false);
            editor.commit();
            Intent i=new Intent(StudentPanel.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        if(id==R.id.studentChangePassword){
            Intent i=new Intent(StudentPanel.this,StudentOtp.class);
            startActivity(i);

        }
        if(id==R.id.studentEditProfile){
            Intent i=new Intent(StudentPanel.this, StudentEditProfile.class);
            startActivity(i);
        }
        if(id==R.id.studentRemovePic){
            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
            SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
            getStudentId();
            String selection= Student.ID + "='" + studentId + "'";
            image="";
            ContentValues cv=new ContentValues();
            cv.put(Student.IMAGE,image);
            int u=Student.update(db,cv,selection);
            if(u>0){
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "not updated", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getStudentId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        studentId=preference.getInt("studentId",0);
    }


}
