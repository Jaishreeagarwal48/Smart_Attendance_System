package com.example.jaishree.attendance.Teacher;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

public class TeacherLogin extends AppCompatActivity {

    Button loginBtn;
    EditText editTextEmail,editTextPass;
    TextView teacherForgetPassword;
    String enteredEmail="",enteredPass="",pass="",enteredMobile="";
    int tId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isUserLogin()){
            Intent i=new Intent(TeacherLogin.this,TeacherPanel.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        init();
        methodListener();

    }

    private void init() {
        loginBtn= (Button) findViewById(R.id.loginBtn);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPass= (EditText) findViewById(R.id.editTextPass);
        teacherForgetPassword= (TextView) findViewById(R.id.teacherForgetPassword);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredEmail=editTextEmail.getText().toString();
                enteredPass=editTextPass.getText().toString();
                boolean flag=checkUserData(enteredEmail,enteredPass);
                if(flag) {
                    Toast.makeText(TeacherLogin.this, "Login success", Toast.LENGTH_SHORT).show();
                    MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(TeacherLogin.this);
                    SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
                    String selection = Teacher.EMAIL + "='" + enteredEmail + "' AND " + Teacher.PASSWORD + "='" + enteredPass + "'";
                    Cursor cursor = Teacher.select(db, selection);
                    if (cursor != null && cursor.moveToNext()) {
                        tId = cursor.getInt(0);
                        storeTeacherId();
                    }
                    storeUserSession();
                    Intent i=new Intent(TeacherLogin.this,TeacherPanel.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }

                }
        });
        teacherForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(TeacherLogin.this);
                dialog.setTitle("Forget Password");
                LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view1=inflater.inflate(R.layout.forget_password_dialog,null);
                final EditText forgetMobileNumber= (EditText) view1.findViewById(R.id.forgetMobileNumber);
                Button forgetBtn= (Button) view1.findViewById(R.id.forgetBtn);
                forgetBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enteredMobile=forgetMobileNumber.getText().toString();
                        if(enteredMobile.equals("")){
                            forgetMobileNumber.setError("Enter mobile number");
                        }
                        else{
                            forgetMobileNumber.setError(null);
                            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(TeacherLogin.this);
                            SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                            String sel= Teacher.MOBILE + "='" + enteredMobile + "'";
                            Cursor cursor=Teacher.select(db,sel);
                            if(cursor!=null){
                                while (cursor.moveToNext()){
                                    Intent i=new Intent(TeacherLogin.this,TeacherOtpForgetPassword.class);
                                    i.putExtra("teacherMobile",enteredMobile);
                                    startActivity(i);
                                    dialog.cancel();

                                }
                            }
                            else {
                                forgetMobileNumber.setError("Invalid mobile number");
                                Toast.makeText(TeacherLogin.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                dialog.setContentView(view1);
                dialog.show();
            }
        });
    }

    private void storeUserSession() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putBoolean("isTeacherLogin",true);
        editor.commit();
    }

    private boolean checkUserData(String enteredEmail, String enteredPass) {
       MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection=Teacher.EMAIL + "='" + enteredEmail + "'";
        Cursor cursor=Teacher.select(db,selection);
        if(cursor!=null){
            while (cursor.moveToNext()){
                pass=cursor.getString(6);
            }
        }

        if(enteredEmail.equals("")){
            editTextEmail.setError("enter email");
            return false;
        }
        else if(enteredPass.equals("")){
            editTextPass.setError("enter password");
            editTextEmail.setError(null);
            return false;
        }
        else{
            editTextPass.setError(null);
            if(enteredPass.equals(pass)){
                editTextEmail.setText("");
                editTextPass.setText("");
                return true;
            }
            Toast.makeText(this, "Invalid email/password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isUserLogin() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        return preference.getBoolean("isTeacherLogin",false);
    }

    private void storeTeacherId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putInt("teacherId",tId);
        Log.d("123", "storeTeacherId: "+tId);
        editor.commit();
    }
}
