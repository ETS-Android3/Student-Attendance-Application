package com.example.studentattendance;

public class StudentItem {

    private long sid;
    private int rollno;
    private String name;
    private String status;

    public StudentItem(long sid, int rollno, String name) {
        this.sid = sid;
        this.rollno = rollno;
        this.name = name;
        status = "";
    }

    public int getRollno() {
        return rollno;
    }

    public void setRollno(int rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
