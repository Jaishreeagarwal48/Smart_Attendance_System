package com.example.jaishree.attendance.Pojo;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class BranchPojo {

    private int id;
    private String name;

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

    @Override
    public String toString() {
        return "BranchPojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
