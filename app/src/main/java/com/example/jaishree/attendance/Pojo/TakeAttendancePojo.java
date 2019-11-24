package com.example.jaishree.attendance.Pojo;

/**
 * Created by Jaishree on 14-07-2017.
 */

public class TakeAttendancePojo {

    private int id,student_id,teacher_id,subject_id,p_count,a_count,l_count;
    private String name,mobile,status="",date,subject_name,parent_mobile;

    public String getParent_mobile() {
        return parent_mobile;
    }

    public void setParent_mobile(String parent_mobile) {
        this.parent_mobile = parent_mobile;
    }

    public int getP_count() {
        return p_count;
    }

    public void setP_count(int p_count) {
        this.p_count = p_count;
    }

    public int getA_count() {
        return a_count;
    }

    public void setA_count(int a_count) {
        this.a_count = a_count;
    }

    public int getL_count() {
        return l_count;
    }

    public void setL_count(int l_count) {
        this.l_count = l_count;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
