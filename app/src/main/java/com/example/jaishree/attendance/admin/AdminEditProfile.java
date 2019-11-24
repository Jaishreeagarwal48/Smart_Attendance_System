package com.example.jaishree.attendance.admin;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.Pojo.AdminPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Admin;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminEditProfile extends AppCompatActivity {

    EditText adminName,adminMobile,adminAddress;
    TextView adminEmail,adminPassword,adminChar;
    Button adminSave,adminCancel;
    FrameLayout adminFrameLayout;
    CircleImageView adminCircleImageView;
    RadioGroup adminRadioGroup;
    RadioButton rb1;
    int selected,id;
    String img="";
    RadioButton adminMale,adminFemale;
    String enteredName="",enteredMobile="",enteredAddress="",enteredEmail="",enteredPassword="",enteredGender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile);
        init();
        methodListener();
        fetchFromDatabase();
    }

    private void init() {
        adminName= (EditText) findViewById(R.id.adminName);
        adminMobile= (EditText) findViewById(R.id.adminMobile);
        adminAddress= (EditText) findViewById(R.id.adminAddress);
        adminEmail= (TextView) findViewById(R.id.adminEmail);
        adminPassword= (TextView) findViewById(R.id.adminPassword);
        adminSave= (Button) findViewById(R.id.adminSave);
        adminCancel= (Button) findViewById(R.id.adminCancel);
        adminCircleImageView= (CircleImageView) findViewById(R.id.adminCircleImageView);
        adminChar= (TextView) findViewById(R.id.adminChar);
        adminRadioGroup= (RadioGroup) findViewById(R.id.adminRadioGroup);
        adminMale= (RadioButton) findViewById(R.id.adminMale);
        adminFemale= (RadioButton) findViewById(R.id.adminFemale);
        adminFrameLayout= (FrameLayout) findViewById(R.id.adminFrameLayout);
    }

    private void methodListener() {

        adminFrameLayout.setOnClickListener(new View.OnClickListener() {
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

        adminSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredName=adminName.getText().toString();
                enteredMobile=adminMobile.getText().toString();
                enteredAddress=adminAddress.getText().toString();
                enteredEmail=adminEmail.getText().toString();
                enteredPassword=adminPassword.getText().toString();
                selected=adminRadioGroup.getCheckedRadioButtonId();
                rb1= (RadioButton) findViewById(selected);
                enteredGender=rb1.getText().toString();
                Log.d("123", "onClick: "+enteredName+enteredMobile+enteredAddress+enteredGender+enteredEmail+enteredPassword);
               if(enteredName.equals("")|enteredMobile.equals("")|enteredAddress.equals("")|enteredGender.equals("")){
                    Toast.makeText(AdminEditProfile.this, "fill all fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(AdminEditProfile.this);
                    SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
                   getId();
                   String selection=Admin.ID + "='" + id + "'";
                    ContentValues cv=new ContentValues();
                    cv.put(Admin.NAME,enteredName);
                    cv.put(Admin.MOBILE,enteredMobile);
                    cv.put(Admin.ADDRESS,enteredAddress);
                    cv.put(Admin.GENDER,enteredGender);
                    cv.put(Admin.IMAGE,img);

                   int l=Admin.update(db,cv,selection);
                    if(l>0){
                        Toast.makeText(AdminEditProfile.this, "Profile is updated", Toast.LENGTH_SHORT).show();
                        fetchFromDatabase();
                        Intent i=new Intent(AdminEditProfile.this,Details.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(AdminEditProfile.this, "Profile is not updated", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        adminCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminEditProfile.this,Details.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void getId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
         id=preference.getInt("adminId",0);
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
                img=uri.toString();
                getImage();
            }
        }
    }

    private void getImage() {
        Glide.with(this).load(img).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(adminCircleImageView);
        adminChar.setVisibility(View.GONE);
    }
     public void fetchFromDatabase() {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(AdminEditProfile.this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor=Admin.select(db,null);
        if(cursor!=null ) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(0);
                String name = cursor.getString(1);
                String mobile = cursor.getString(2);
                String gender = cursor.getString(3);
                String address = cursor.getString(4);

                img=cursor.getString(5);
                String email = cursor.getString(6);
                String password = cursor.getString(7);

                adminName.setText(name);
                adminMobile.setText(mobile);
                adminAddress.setText(address);
                adminEmail.setText(email);
                adminPassword.setText(password);
                Log.d("123", "fetchFromDatabase: "+id);

                if (gender.equals(adminMale.getText().toString())) {
                    adminMale.setChecked(true);
                } else {
                    adminFemale.setChecked(true);
                }

                if(img.equals("") && name.equals("")){
                    adminChar.setVisibility(View.VISIBLE);
                    adminChar.setText("A");
                }
                else if(img.equals("")){
                    adminChar.setVisibility(View.VISIBLE);
                    adminChar.setText(("" + name.charAt(0)).toUpperCase());
                }
                else{
                    adminChar.setVisibility(View.GONE);
                    getImage();
                }
            }
        }

    }
}
