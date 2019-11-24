package com.example.jaishree.attendance.Student;

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
import com.example.jaishree.attendance.Teacher.TeacherOtpForgetPassword;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

public class StudentLogin extends AppCompatActivity {
    Button loginBtn;
    EditText editTextEmail,editTextPass;
    TextView studentForgetPassword;
    String enteredEmail="",enteredPass="",pass="",enteredMobile="";
    int studentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isUserLogin()){
            Intent i=new Intent(StudentLogin.this,StudentPanel.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        init();
        methodListener();
    }

    private void init() {
        loginBtn= (Button) findViewById(R.id.loginBtn);
        studentForgetPassword= (TextView) findViewById(R.id.studentForgetPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPass= (EditText) findViewById(R.id.editTextPass);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredEmail=editTextEmail.getText().toString();
                enteredPass=editTextPass.getText().toString();
                boolean flag=checkUserData(enteredEmail,enteredPass);
                if(flag){
                    Toast.makeText(StudentLogin.this, "Login Success", Toast.LENGTH_SHORT).show();
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(StudentLogin.this);
                    SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                    String selection=Student.EMAIL + "='" + enteredEmail + "' AND " + Student.PASSWORD + "='" + enteredPass + "'";
                    Cursor cursor=Student.select(db,selection);
                    if(cursor!=null && cursor.moveToNext()){
                        studentId=cursor.getInt(0);
                        storeStudentId();
                    }
                    storeUserSession();
                    Intent i=new Intent(StudentLogin.this,StudentPanel.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });

        studentForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(StudentLogin.this);
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
                            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(StudentLogin.this);
                            SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                            String sel= Student.MOBILE + "='" + enteredMobile + "'";
                            Cursor cursor=Student.select(db,sel);
                            if(cursor!=null){
                                while (cursor.moveToNext()){
                                    Intent i=new Intent(StudentLogin.this,StudentOtpForgetPassword.class);
                                    i.putExtra("StudentMobile",enteredMobile);
                                    Log.d("123", "onClick: "+enteredMobile);
                                    startActivity(i);
                                    dialog.cancel();

                                }
                            }
                            else {
                                forgetMobileNumber.setError("Invalid mobile number");
                                Toast.makeText(StudentLogin.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
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
        editor.putBoolean("isStudentLogin",true);
        editor.commit();
    }

    private void storeStudentId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putInt("studentId",studentId);
        Log.d("123", "storeStudentId: "+studentId);
        editor.commit();
    }

    private boolean checkUserData(String enteredEmail, String enteredPass) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection= Student.EMAIL + "='" + enteredEmail + "'";
        Cursor cursor=Student.select(db,selection);
        if(cursor!=null && cursor.moveToNext()){
            pass=cursor.getString(9);
        }
        if(enteredEmail.equals("")){
            editTextEmail.setError("Enter email");
            return false;
        }
        else if(enteredPass.equals("")){
            editTextPass.setError("Enter password");
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
       return preference.getBoolean("isStudentLogin",false);
    }

}
