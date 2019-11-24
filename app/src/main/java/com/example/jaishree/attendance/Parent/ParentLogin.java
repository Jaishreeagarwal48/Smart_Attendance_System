package com.example.jaishree.attendance.Parent;

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

public class ParentLogin extends AppCompatActivity {

    Button loginBtn;
    EditText editTextEmail,editTextPass;
    String enteredEmail="",enteredPass="",pass="",enteredMobile="";
    int parentId;
    TextView parentForgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isUserLogin()){
            Intent i=new Intent(this,ParentPanel.class);
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
        parentForgetPassword= (TextView) findViewById(R.id.parentForgetPassword);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredEmail=editTextEmail.getText().toString();
                enteredPass=editTextPass.getText().toString();
                boolean flag=checkUserData(enteredEmail,enteredPass);
                if(flag){
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(ParentLogin.this);
                    SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                    String selection=Student.PARENT_EMAIL + "='" + enteredEmail + "' AND " + Student.PARENT_PASSWORD + "='" + enteredPass + "'";
                    Cursor cursor=Student.select(db,selection);
                    if(cursor!=null && cursor.moveToNext()){
                        parentId=cursor.getInt(0);
                        storeParentId();
                    }
                    storeUserSession();
                    Intent i=new Intent(ParentLogin.this,ParentPanel.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            }
        });
        parentForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(ParentLogin.this);
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
                            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(ParentLogin.this);
                            SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                            String sel= Student.PARENT_MOBILE + "='" + enteredMobile + "'";
                            Cursor cursor=Student.select(db,sel);
                            if(cursor!=null){
                                while (cursor.moveToNext()){
                                    Intent i=new Intent(ParentLogin.this,ParentOtpForgetPassword.class);
                                    i.putExtra("parentMobile",enteredMobile);
                                    startActivity(i);
                                    dialog.cancel();

                                }
                            }
                            else {
                                forgetMobileNumber.setError("Invalid mobile number");
                                Toast.makeText(ParentLogin.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
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
        editor.putBoolean("isParentLogin",true);
        editor.commit();
    }

    private void storeParentId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putInt("parentId",parentId);
        Log.d("123", "storeParentId: "+parentId);
        editor.commit();
    }

    private boolean checkUserData(String enteredEmail, String enteredPass) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(ParentLogin.this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection= Student.PARENT_EMAIL + "='" + enteredEmail + "'";
        Cursor cursor=Student.select(db,selection);
        if(cursor!=null && cursor.moveToNext()){
            pass=cursor.getString(10);
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
        return preference.getBoolean("isParentLogin",false);
    }
}
