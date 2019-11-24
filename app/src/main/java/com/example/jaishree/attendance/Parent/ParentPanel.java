package com.example.jaishree.attendance.Parent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.ViewPagerAdapter;
import com.example.jaishree.attendance.MainActivity;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.Teacher.ViewNews;

public class ParentPanel extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons={R.drawable.newsic,
            R.drawable.studentattendanceicon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_panel);
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
        adapter.addFragment(new ParentViewAttendance()," Attendance");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.parent_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.parentChangePassword){
            Intent i=new Intent(ParentPanel.this, ParentOtp.class);
            startActivity(i);
        }
        if(id==R.id.parentLogout){
            SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preference.edit();
            editor.putBoolean("isParentLogin",false);
            editor.commit();
            Intent i=new Intent(ParentPanel.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
