package com.example.jaishree.attendance.admin;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.jaishree.attendance.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminForgetPassword extends AppCompatActivity {
    Button startTimer,checkBtn;
    TextView textView;
    CircleImageView circleImage;
    EditText editText;
    String enteredOtp="",generateOtp="";
    private int count=6;
    private static final String DIGITS="0123456789";
    String enteredMobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_forget_password);
        enteredMobile=getIntent().getStringExtra("adminMobile");

        init();
        Glide.with(this).load(R.drawable.otp).crossFade().into(circleImage);
        methodListener();
    }

    private void init() {
        startTimer= (Button) findViewById(R.id.startTimer);
        textView= (TextView) findViewById(R.id.textView);
        editText= (EditText) findViewById(R.id.editText);
        checkBtn= (Button) findViewById(R.id.checkBtn);
        circleImage= (CircleImageView) findViewById(R.id.circleImage);
    }

    private void methodListener() {
        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer.setText("Resend OTP");
                startTimer.setEnabled(false);
                generatedOtp(count);
                sendMsg();
                new CountDownTimer(60000,1000){

                    @Override
                    public void onTick(long l) {
                        textView.setText("time remaining: " + l/1000);
                    }

                    @Override
                    public void onFinish() {
                        textView.setText("");
                        startTimer.setEnabled(true);
                        generateOtp=null;
                    }
                }.start();
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredOtp=editText.getText().toString();
                if(enteredOtp.equals("")){
                    editText.setError("Please enter OTP");
                }
                else {
                    editText.setError(null);
                    if (enteredOtp.equals(generateOtp)) {
                        Toast.makeText(AdminForgetPassword.this, "OTP matched", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        textView.setText("");
                        Intent i=new Intent(AdminForgetPassword.this, AdminChangeForgetPassword.class);
                        i.putExtra("adminMobile",enteredMobile);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(AdminForgetPassword.this, "OTP not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void generatedOtp(int count) {
        StringBuffer buffer=new StringBuffer();
        while (count!=0){
            int c= (int) (Math.random()*DIGITS.length());
            buffer.append(DIGITS.charAt(c));
            count--;
        }
        generateOtp=buffer.toString();
        Log.d("123", "generatedOtp: "+generateOtp);
    }

    private void sendMsg() {
        StringRequest request=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + enteredMobile + "&message= OTP:" + generateOtp + "&sender=ABCDEF&route=4&country=91", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("123", "onResponse: Otp sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("123", "onErrorResponse: error ");
            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

}
