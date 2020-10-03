package com.cu.aclass.Model;

public class TimeAddData {
    private String id;
    private String start;
    private String end;
    private String subject;
    private String type;
    private String room;
    private String teacher;
    private String contact;
    private String notes;
    private String day;
    private String spinner;

    public TimeAddData(String id, String start, String end, String subject, String type, String room, String teacher, String contact, String notes, String day, String spinner) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.type = type;
        this.room = room;
        this.teacher = teacher;
        this.contact = contact;
        this.notes = notes;
        this.day = day;
        this.spinner = spinner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }
}
