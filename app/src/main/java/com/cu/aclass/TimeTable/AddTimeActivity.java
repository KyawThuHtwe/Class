package com.cu.aclass.TimeTable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.aclass.Adapter.DayAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Activity.MainActivity;
import com.cu.aclass.Model.AddDay;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddTimeActivity extends AppCompatActivity {

    DatabaseHelper helper;
    public TextView start,end,startview,endview;
    public EditText subject,type,room,teacher,contact,note;
    public String title,id;
    public TextView day;
    public LinearLayout save;
    public ImageView back;
    public TextView save_text;
    String[] report={"1","2","3","4","5","6","7","8","9","10","11","12"};
    String[] select_qualification = {
            "Add other day","Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    ArrayList<String> other_day_array;
    ArrayAdapter spinnerAdapter;
    Spinner spinner,spinner_day;
    LinearLayout action_layout,layout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);
        layout=findViewById(R.id.layout);
        action_layout=findViewById(R.id.action_layout);
        layout.setBackgroundColor(readLayoutColor());
        action_layout.setBackgroundColor(readActionBarColor());
        getWindow().setStatusBarColor(readActionBarColor());
        getWindow().setNavigationBarColor(readActionBarColor());
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

        spinner=findViewById(R.id.spinner);
        spinnerAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,report);
        spinner.setAdapter(spinnerAdapter);

        //day add

        spinner_day=findViewById(R.id.spinner_item);
        if(id==null) {
            final ArrayList<AddDay> dayArrayList = new ArrayList<>();
            dayArrayList.clear();
            for (String s : select_qualification) {
                //check day
                if (!title.equals(s)) {
                    AddDay dayClass = new AddDay();
                    dayClass.setDay(s);
                    dayClass.setSelected(false);
                    dayArrayList.add(dayClass);
                }
            }
            DayAdapter dayAdapter = new DayAdapter(getApplicationContext(), 0, dayArrayList);
            spinner_day.setAdapter(dayAdapter);
        }else{
            spinner_day.setVisibility(View.GONE);
        }


        ///
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
                        spinner.setSelection(Integer.parseInt(res.getString(10)));
                    }
                }
                helper.close();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

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
                                contact.getText().toString(), note.getText().toString(), day.getText()+"",spinner.getSelectedItemPosition()+"");

                    }
                }else {
                    boolean result=helper.updateTime(id+"",startview.getText()+"",endview.getText()+"",
                            subject.getText()+"",type.getText()+"",room.getText()+"",
                            teacher.getText()+"",contact.getText()+"",note.getText()+"",spinner.getSelectedItemPosition()+"");
                    if(result){
                        @SuppressLint("InflateParams") View view_update= LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast_time_add_successful,null);
                        Toast toast=Toast.makeText(getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT);
                        LinearLayout linearLayout=view_update.findViewById(R.id.layout);
                        linearLayout.setBackgroundColor(readActionBarColor());
                        toast.setGravity(Gravity.BOTTOM,0,100);
                        TextView textView=view_update.findViewById(R.id.type);
                        textView.setText("Update Successful");
                        toast.setView(view_update);
                        toast.show();
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

    boolean result;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void insertDatabase(String from, String to, String subject, String type, String room, String teacher, String contact, String note, String day, String spinner){
        other_day_array=new ArrayList<>();
        if(other_day_array.size()>0){
            other_day_array.clear();
        }
        for (int i = 0; i < select_qualification.length; i++) {
            if(getCheck(select_qualification[i])==1){
                other_day_array.add(select_qualification[i]);
                addDay(getDay(select_qualification[i]),0);
            }
        }
        other_day_array.add(day);
        for(int i=0;i<other_day_array.size();i++) {
            result = helper.insertTime(from, to, subject, type, room, teacher, contact, note, other_day_array.get(i), spinner);
        }
        if (result) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.toast_time_add_successful, null);
            Toast toast = Toast.makeText(this, "Time Add Successfully", Toast.LENGTH_SHORT);
            LinearLayout linearLayout = view.findViewById(R.id.layout);
            linearLayout.setBackgroundColor(readActionBarColor());
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.setView(view);
            toast.show();
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("getDay", title);
            startActivity(intent1);
            finish();
        } else {
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
        }
    }
    public void addDay(String day, int res){
        SharedPreferences sharedPreferences= getSharedPreferences("addDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(day+"day",day);
        editor.putInt(day+"check",res).apply();
    }
    public String getDay(String day){
        SharedPreferences sharedPreferences= getSharedPreferences("addDay", Context.MODE_PRIVATE);
        return sharedPreferences.getString(day+"day","day");
    }
    public int getCheck(String day){
        SharedPreferences sharedPreferences= getSharedPreferences("addDay", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(day+"check",0);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readActionBarColor(){
        SharedPreferences sharedPreferences= getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ActionBarColor",getResources().getColor(R.color.colorPrimary));
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readLayoutColor(){
        SharedPreferences sharedPreferences= getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("LayoutColor",getResources().getColor(R.color.colorTextHint));
    }
}