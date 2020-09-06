package com.cu.aclass.Model;

public class AttendanceData {
    private String id,date,subject,vote,day,ssid;

    public AttendanceData(String id, String date, String subject, String vote, String day, String ssid) {
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.vote = vote;
        this.day = day;
        this.ssid = ssid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
