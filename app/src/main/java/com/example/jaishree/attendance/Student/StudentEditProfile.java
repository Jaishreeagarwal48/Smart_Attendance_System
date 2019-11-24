package com.example.jaishree.attendance.Student;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentEditProfile extends AppCompatActivity {
    CircleImageView teacherProfilePic;
    TextView teacherProfileChar,teacherProfileName,teacherProfileMobile,teacherProfileEmail;
    Button teacherProfileSave,teacherProfileCancel;
    FrameLayout teacherProfileFrame;
    String image="",name="",mobile="",email="";
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit_profile);

        init();
        getStudentId();
        methodListener();
        fetchFromDatabase();
    }

    private void init() {
        teacherProfilePic= (CircleImageView) findViewById(R.id.teacherProfilePic);
        teacherProfileChar= (TextView) findViewById(R.id.teacherProfileChar);
        teacherProfileName= (TextView) findViewById(R.id.teacherProfileName);
        teacherProfileMobile= (TextView) findViewById(R.id.teacherProfileMobile);
        teacherProfileEmail= (TextView) findViewById(R.id.teacherProfileEmail);
        teacherProfileSave= (Button) findViewById(R.id.teacherProfileSave);
        teacherProfileCancel= (Button) findViewById(R.id.teacherProfileCancel);
        teacherProfileFrame= (FrameLayout) findViewById(R.id.teacherProfileFrame);
    }

    private void getStudentId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        studentId=preference.getInt("studentId",0);
    }

    private void methodListener() {
        teacherProfileFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGalleryPermission()){
                    Intent i=new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(i,0);
                }
            }
        });
        teacherProfileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StudentEditProfile.this, StudentPanel.class);
                startActivity(i);
                finish();
            }
        });
        teacherProfileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeInDatabase();
            }
        });

    }

    private void storeInDatabase() {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
        String selection= Student.ID + "='" + studentId + "'";
        ContentValues cv=new ContentValues();
        cv.put(Student.IMAGE,image);
        int u=Student.update(db,cv,selection);
        if(u>0){
            Toast.makeText(this, "Profile is updated", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Profile is not updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkGalleryPermission() {
        boolean flag= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                Uri uri=data.getData();
                image=uri.toString();
                getImage();
            }
        }
    }

    private void getImage() {
        Glide.with(this).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(teacherProfilePic);
        teacherProfileChar.setVisibility(View.GONE);
    }

    private void fetchFromDatabase() {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection=Student.ID + "='" + studentId + "'";
        Cursor cursor=Student.select(db,selection);
        if(cursor!=null && cursor.moveToNext()){

            name=cursor.getString(1);
            mobile=cursor.getString(2);
            email=cursor.getString(3);
            image=cursor.getString(11);
            teacherProfileName.setText("Name:"+name);
            teacherProfileMobile.setText("Mobile:"+mobile);
            teacherProfileEmail.setText("Email:"+email);

            if (image.equals("")){
                teacherProfileChar.setVisibility(View.VISIBLE);
                teacherProfileChar.setText((""+name.charAt(0)).toUpperCase());
            }
            else {
                teacherProfileChar.setVisibility(View.GONE);
                teacherProfilePic.setVisibility(View.VISIBLE);
                getImage();
            }

        }
    }


}
