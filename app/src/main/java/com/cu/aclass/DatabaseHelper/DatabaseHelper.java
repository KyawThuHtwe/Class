package com.cu.aclass.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME="TimeDb.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Time (ID INTEGER PRIMARY KEY AUTOINCREMENT,FROM_TIME TEXT,TO_TIME TEXT,SUBJECT TEXT,TYPE TEXT,ROOM TEXT,TEACHER TEXT,CONTACT TEXT,NOTE TEXT,DAY TEXT)");
        db.execSQL("CREATE TABLE Attendance (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,SUBJECT TEXT,VOTE TEXT,DAY TEXT,S_SID TEXT)");
        db.execSQL("CREATE TABLE Note (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,SUBJECT TEXT,TIME TEXT,DATE TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Time");
        db.execSQL("DROP TABLE IF EXISTS Attendance");
        db.execSQL("DROP TABLE IF EXISTS Note");
    }
    public boolean insertTime(String from_time,String to_time,String subject,String type,String room,String teacher,String contact,String note,String day){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("FROM_TIME",from_time);
        contentValues.put("TO_TIME",to_time);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TYPE",type);
        contentValues.put("ROOM",room);
        contentValues.put("TEACHER",teacher);
        contentValues.put("CONTACT",contact);
        contentValues.put("NOTE",note);
        contentValues.put("DAY",day);

        long result=db.insert("Time",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }

    }
    public Cursor getAllTime(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Time",null);
        return res;
    }
    public int deleteTime(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("Time","ID=?",new String[]{id});
        return res;
    }
    public boolean updateTime(String id,String from_time,String to_time,String subject,String type,String room,String teacher,String contact,String note){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("ID",id);
        contentValues.put("FROM_TIME",from_time);
        contentValues.put("TO_TIME",to_time);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TYPE",type);
        contentValues.put("ROOM",room);
        contentValues.put("TEACHER",teacher);
        contentValues.put("CONTACT",contact);
        contentValues.put("NOTE",note);
        int result=db.update("Time",contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }

    public Cursor subjectTotalCount(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor count=sqLiteDatabase.rawQuery("Select SUBJECT,Count(*) count from Time Group By SUBJECT Having count>0",null);
        return count;
    }
    public Cursor attendanceTotalCount(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor count=sqLiteDatabase.rawQuery("Select SUBJECT,Count(*) count from Attendance Group By SUBJECT Having count>0",null);
        return count;
    }
    public boolean insertAttendance(String date,String Subject,String vote,String day,String sid){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("DATE",date);
        contentValues.put("SUBJECT",Subject);
        contentValues.put("VOTE",vote);
        contentValues.put("DAY",day);
        contentValues.put("S_SID",sid);
        long result=db.insert("Attendance",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }
    }

    public Cursor getAttendance(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Attendance",null);
        return res;
    }
    public boolean deleteAttendanceTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Attendance",null,null);
        return affectedRows>0;
    }
    public boolean deleteTimeTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Time",null,null);
        return affectedRows>0;
    }
    public boolean deleteNoteTable(){
        SQLiteDatabase db=this.getReadableDatabase();
        int affectedRows=db.delete("Note",null,null);
        return affectedRows>0;
    }

    public boolean insertNote(String title,String subject,String time,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("TITLE",title);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TIME",time);
        contentValues.put("DATE",date);
        long result=db.insert("Note",null,contentValues);
        db.close();

        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getNote(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("Select * from Note",null);
        return res;
    }
    public boolean updateNote(String id,String title,String subject,String time,String date){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("TITLE",title);
        contentValues.put("SUBJECT",subject);
        contentValues.put("TIME",time);
        contentValues.put("DATE",date);
        int result=db.update("Note",contentValues,"ID=?",new String[]{id});
        if(result>0){
            return true;
        }else {
            return false;
        }
    }
    public int deleteNote(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete("Note","ID=?",new String[]{id});
        return res;
    }

}
