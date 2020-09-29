package com.cu.aclass.TimeTable;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Activity.MainActivity;
import com.cu.aclass.R;

import java.util.Calendar;

public class AddTimeActivity extends AppCompatActivity {

    DatabaseHelper helper;
    public TextView start,end,startview,endview;
    public EditText subject,type,room,teacher,contact,note;
    public String title,id;
    public TextView day;
    public LinearLayout save;
    public ImageView back;
    public TextView save_text;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        helper=new DatabaseHelper(this);
        Intent intent=getIntent();
        title=intent.getStringExtra("Day");
        day=findViewById(R.id.day);
        day.setText(title);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        startview=findViewById(R.id.startview);
        endview=findViewById(R.id.endview);
        subject=findViewById(R.id.subject);
        type=findViewById(R.id.type);
        room=findViewById(R.id.room);
        teacher=findViewById(R.id.teacher);
        contact=findViewById(R.id.contact);
        note=findViewById(R.id.note);
        save=findViewById(R.id.save);
        save_text=findViewById(R.id.save_text);
        final String id=intent.getStringExtra("Id");
        if(id==null){
            save_text.setText(R.string.save);
        }else {
            save_text.setText(R.string.update);
        }
        try {
            Cursor res = helper.getAllTime();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if (res.getString(0).equals(id)) {
                        startview.setText(res.getString(1));
                        endview.setText(res.getString(2));
                        subject.setText(res.getString(3));
                        type.setText(res.getString(4));
                        room.setText(res.getString(5));
                        teacher.setText(res.getString(6));
                        contact.setText(res.getString(7));
                        note.setText(res.getString(8));
                        day.setText(res.getString(9));
                    }
                }
                helper.close();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==null){
                    if (subject.getText().toString().equals("")||type.getText().toString().equals("")||
                            room.getText().toString().equals("")||teacher.getText().toString().equals("")||
                            contact.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(),"Please Fill Empty,Not Insert!!!",Toast.LENGTH_SHORT).show();
                    }else {
                        insertDatabase(startview.getText().toString(), endview.getText().toString(),
                                subject.getText().toString(), type.getText().toString(),
                                room.getText().toString(), teacher.getText().toString(),
                                contact.getText().toString(), note.getText().toString(), day.getText().toString());
                    }
                }else {
                    boolean result=helper.updateTime(id+"",startview.getText()+"",endview.getText()+"",
                            subject.getText()+"",type.getText()+"",room.getText()+"",
                            teacher.getText()+"",contact.getText()+"",note.getText()+"");
                    if(result){
                        Toast.makeText(getApplicationContext(),"Update successful",Toast.LENGTH_SHORT).show();
                        Intent main=new Intent(getApplicationContext(),MainActivity.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.putExtra("getDay",day.getText().toString());
                        startActivity(main);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime(startview);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTime(endview);
            }
        });
    }
    public void insertTime(final TextView time){
        Calendar c= Calendar.getInstance();
        int hr=c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                time.setText(i+":"+i1);
            }
        },hr,min,false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void insertDatabase(String from, String to, String subject, String type, String room, String teacher, String contact, String note, String day){
        boolean result=helper.insertTime(from,to,subject,type,room,teacher,contact,note,day);
        if(result){
            Toast.makeText(getApplicationContext(),"Insert successful",Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(getApplicationContext(), MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("getDay",title);
            startActivity(intent1);
            finish();
        }else {
            Toast.makeText(this,"Fail",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}