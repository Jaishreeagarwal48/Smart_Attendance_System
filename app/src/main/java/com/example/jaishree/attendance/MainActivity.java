package com.example.jaishree.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.example.jaishree.attendance.Parent.ParentLogin;
import com.example.jaishree.attendance.Student.StudentLogin;
import com.example.jaishree.attendance.Teacher.TeacherLogin;
import com.example.jaishree.attendance.admin.AdminLoginPage;

public class MainActivity extends AppCompatActivity {

    View viewAdmin,viewTeacher,viewStudent,viewParent;
    ViewFlipper viewFlipper;
    private float lastX;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        methodListener();
    }


    private void init() {
        viewAdmin=findViewById(R.id.viewAdmin);
        viewTeacher=findViewById(R.id.viewTeacher);
        viewStudent=findViewById(R.id.viewStudent);
        viewParent=findViewById(R.id.viewParent);
        viewFlipper= (ViewFlipper) findViewById(R.id.viewFlipper);

    }
    private void methodListener() {
        viewAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AdminLoginPage.class);
                startActivity(i);
            }
        });
        viewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,TeacherLogin.class);
                startActivity(i);
            }
        });
        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,StudentLogin.class);
                startActivity(i);
            }
        });
        viewParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ParentLogin.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                lastX=touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                float currentX=touchevent.getX();

                if(lastX<currentX){
                    if(viewFlipper.getDisplayedChild()==0)
                        break;

                    viewFlipper.setInAnimation(this,R.anim.slide_in_from_left);
                    viewFlipper.setOutAnimation(this,R.anim.slide_out_from_right);
                    viewFlipper.showNext();
                }

                if(lastX>currentX){
                    if(viewFlipper.getDisplayedChild()==1)
                        break;

                    viewFlipper.setInAnimation(this,R.anim.slide_in_from_right);
                    viewFlipper.setOutAnimation(this,R.anim.slide_out_from_left);
                    viewFlipper.showPrevious();
                }
                break;
            }
        }
        return false;
    }
}
