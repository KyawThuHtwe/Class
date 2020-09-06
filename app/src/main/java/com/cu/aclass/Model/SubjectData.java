package com.cu.aclass.Model;

public class SubjectData {
    private int total;
    private String subject;

    public SubjectData(int total, String subject) {
        this.total = total;
        this.subject = subject;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
