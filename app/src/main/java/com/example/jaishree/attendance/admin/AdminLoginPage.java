package com.example.jaishree.attendance.admin;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Admin;

public class AdminLoginPage extends AppCompatActivity {

    EditText editTextEmail,editTextPass;
    Button loginBtn;
    TextView adminForgetPassword;
    int id;
    String email="admin@admin",password="",enteredMobile="",mobile="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isUserLogin()){
            Intent i=new Intent(this,Details.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }

        init();
        methodListener();

    }

    private void init() {
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        adminForgetPassword= (TextView) findViewById(R.id.adminForgetPassword);
        editTextPass= (EditText) findViewById(R.id.editTextPass);
        loginBtn= (Button) findViewById(R.id.loginBtn);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredEmail=editTextEmail.getText().toString();
                String enteredPass=editTextPass.getText().toString();
                boolean flag=checkUserData(enteredEmail, enteredPass);
                if(flag){
                    Toast.makeText(AdminLoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(AdminLoginPage.this);
                    SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                    String selection= Admin.EMAIL + "='" + enteredEmail + "' AND " + Admin.PASSWORD + "='" + enteredPass + "'";
                    Cursor cursor=Admin.select(db,selection);
                    if(cursor!=null){
                        while(cursor.moveToNext()){
                            id=cursor.getInt(0);
                            storeId();
                        }
                    }
                    storeUserSession();
                    Intent i=new Intent(AdminLoginPage.this,Details.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }

            }
        });

        adminForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(AdminLoginPage.this);
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
                            MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(AdminLoginPage.this);
                            SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
                            String sel=Admin.MOBILE + "='" + enteredMobile + "'";
                            Cursor cursor=Admin.select(db,sel);
                            if(cursor!=null){
                                while (cursor.moveToNext()){
                                    Intent i=new Intent(AdminLoginPage.this,AdminForgetPassword.class);
                                    i.putExtra("adminMobile",enteredMobile);
                                    startActivity(i);
                                    dialog.cancel();

                                }
                            }
                            else {
                                forgetMobileNumber.setError("Invalid mobile number");
                                Toast.makeText(AdminLoginPage.this, "Invalid mobile number", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                dialog.setContentView(view1);
                dialog.show();
            }
        });

    }

    private void storeId() {
        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("adminId",id);
        editor.commit();
    }

    private void storeUserSession() {
        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    private boolean checkUserData(String enteredEmail, String enteredPass) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        String selection=Admin.EMAIL + "='" + email + "'";
        Cursor cursor=Admin.select(db,selection);
        if(cursor!=null){
            while (cursor.moveToNext()){
                password=cursor.getString(7);
            }
        }
        if(enteredEmail.equals("") || enteredPass.equals("")){
            Toast.makeText(AdminLoginPage.this, "fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(enteredEmail.equals(email) && enteredPass.equals(password)){
            editTextEmail.setText("");
            editTextPass.setText("");
            return true;
        }
        else
        {
            Toast.makeText(AdminLoginPage.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isUserLogin() {
        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        return preferences.getBoolean("isLogin",false);
    }


}
