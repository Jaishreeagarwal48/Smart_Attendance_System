package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.MainActivity;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Admin;

import de.hdodenhof.circleimageview.CircleImageView;

public class Details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView adminHeaderText;
    CircleImageView adminHeaderImage;
    TextView adminHeaderName;
    String name="",img="";
    ImageView adminProfilePic;
    int idAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view=navigationView.getHeaderView(0);
        adminHeaderImage= (CircleImageView) view.findViewById(R.id.adminHeaderImage);
        adminHeaderText= (TextView) view.findViewById(R.id.adminHeaderText);
        adminHeaderName= (TextView) view.findViewById(R.id.adminHeaderName);
        adminProfilePic= (ImageView) view.findViewById(R.id.adminProfilePic);

        showHeaderImage();
        showImage();
    }

    private void showHeaderImage() {
        Glide.with(this).load(R.drawable.admin_profile).diskCacheStrategy(DiskCacheStrategy.ALL).into(adminProfilePic);
    }

    private void showImage() {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor= Admin.select(db,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                 name=cursor.getString(1);
                 img=cursor.getString(5);
            }
        }

        if(img.equals("") && name.equals("")){
            adminHeaderText.setVisibility(View.VISIBLE);
            adminHeaderImage.setVisibility(View.GONE);
            adminHeaderText.setText("A");
        }
        else if(img.equals("")){
            adminHeaderText.setVisibility(View.VISIBLE);
            adminHeaderImage.setVisibility(View.GONE);
            adminHeaderText.setText((""+name.charAt(0)).toUpperCase());

        }
        else{
            adminHeaderText.setVisibility(View.GONE);
            adminHeaderImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(img).crossFade().diskCacheStrategy(DiskCacheStrategy.NONE).into(adminHeaderImage);
        }

        if(name.equals("")){
            adminHeaderName.setText("Admin");
        }
        else{
            adminHeaderName.setText(name);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.adminLogout) {
            SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isLogin",false);
            editor.commit();
            Intent i=new Intent(Details.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        if(id == R.id.adminEditProfile){
            Intent i=new Intent(Details.this, AdminEditProfile.class);
            startActivity(i);
        }
        if(id==R.id.adminChangePassword){
            Intent i=new Intent(Details.this, Otp.class);
            startActivity(i);
        }
        if(id==R.id.adminRemovePic){
            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(Details.this);
            SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
            getId();
            String selection=Admin.ID + " = '" + idAdmin + "'";
            Log.d("1234", "onOptionsItemSelected: "+idAdmin);
            img="";
            ContentValues cv=new ContentValues();
            cv.put(Admin.IMAGE,img);

            int l=Admin.update(db,cv,selection);
            if(l>0){
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "not updated", Toast.LENGTH_SHORT).show();
            }
            adminHeaderText.setVisibility(View.VISIBLE);
            adminHeaderText.setText((""+name.charAt(0)).toUpperCase());
        }

        return super.onOptionsItemSelected(item);
    }
    private void getId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        idAdmin=preference.getInt("adminId",0);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addNews) {
            changeFragment(new AddNews());

        } else if (id == R.id.addStudent) {
            changeFragment(new AddStudent());

        } else if (id == R.id.addTeacher) {
            changeFragment(new AddTeacher());

        }  else if (id == R.id.viewReport) {
            changeFragment(new ViewAttendanceReport());

        } else if (id == R.id.viewStudentDetails) {
            changeFragment(new ViewStudent());

        }
        else if(id == R.id.viewTeacherDetails){
            changeFragment(new ViewTeacher());

        }
        else if(id == R.id.addBranch){
            changeFragment(new addBranch());

        }
        else if (id == R.id.viewSubject){
            changeFragment(new ViewSubject());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer,fragment);
        transaction.commit();
    }
}
