package com.example.jaishree.attendance.Student;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentChangePassword extends AppCompatActivity {
    TextView adminChangeEmail,adminPasswordTick,adminConfirmPasswordTick;
    EditText adminChangePassword,adminChangeConfirmPassword;
    Button saveChangePassword;
    String enteredEmail="",enteredPassword="",enteredConfirm="";
    CircleImageView adminChangeImage;
    int studentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_change_password);
        init();
        Glide.with(this).load(R.drawable.changepassword).into(adminChangeImage);
        getEmail();
        methodListener();
    }
    private void init() {
        adminChangeEmail= (TextView) findViewById(R.id.adminChangeEmail);
        adminPasswordTick= (TextView) findViewById(R.id.adminPasswordTick);
        adminConfirmPasswordTick= (TextView) findViewById(R.id.adminConfirmPasswordTick);
        adminChangePassword= (EditText) findViewById(R.id.adminChangePassword);
        adminChangeConfirmPassword= (EditText) findViewById(R.id.adminChangeConfirmPassword);
        saveChangePassword= (Button) findViewById(R.id.saveChangePassword);
        adminChangeImage= (CircleImageView) findViewById(R.id.adminChangeImage);
    }

    private void getEmail() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        studentId=preference.getInt("studentId",0);
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection= Student.ID + "='" + studentId + "'";
        Cursor cursor= Student.select(db,selection);
        if(cursor!=null){
            while (cursor.moveToNext()){
                enteredEmail=cursor.getString(3).toString();
            }
        }
        adminChangeEmail.setText(enteredEmail);
    }

    private void methodListener() {
        adminChangePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enteredPassword=charSequence.toString();
                if(enteredPassword.length()<8){
                    adminPasswordTick.setText("\u274C");
                }
                else{
                    adminPasswordTick.setText("\u2713");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        adminChangeConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enteredConfirm=editable.toString();
                if(enteredConfirm.equals(enteredPassword)){
                    adminConfirmPasswordTick.setText("\u2713");
                }
                else{
                    adminConfirmPasswordTick.setText("\u274C");
                }
            }
        });
        saveChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enteredPassword.equals("") || enteredConfirm.equals("")) {
                    Toast.makeText(StudentChangePassword.this, "fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(StudentChangePassword.this);
                    SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();
                    String selection = Student.ID + "='" + studentId + "'";
                    ContentValues cv = new ContentValues();
                    cv.put(Student.PASSWORD, enteredPassword);
                    int u = Student.update(db, cv, selection);
                    if (u > 0) {
                        Toast.makeText(StudentChangePassword.this, "updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentChangePassword.this, "not updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
