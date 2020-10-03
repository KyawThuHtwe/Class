package com.cu.aclass.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Adapter.AttendanceAdapter;
import com.cu.aclass.Adapter.DataNoteAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.AttendanceData;
import com.cu.aclass.Model.NoteData;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DataListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public Spinner spinner;
    String[] report={"Attendance","Note"};
    ArrayAdapter spinnerAdapter;
    LinearLayout start,end;
    TextView start_date,end_date;
    ArrayList<NoteData> noteData;
    ArrayList<AttendanceData> attendanceData;
    ImageView back;
    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setStatusBarColor(readActionBarColor());
        getWindow().setNavigationBarColor(readActionBarColor());
        LinearLayout action_layout=findViewById(R.id.action_layout);
        action_layout.setBackgroundColor(readActionBarColor());
        spinner=findViewById(R.id.spinner);
        spinnerAdapter=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,report);
        spinner.setAdapter(spinnerAdapter);
        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        start_date=findViewById(R.id.start_date);
        end_date=findViewById(R.id.end_date);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start_date.setText("Start");
                end_date.setText("End");
                recyclerView.setAdapter(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Date(start_date,v,0);
                end_date.setText("End");
                recyclerView.setAdapter(null);
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date(end_date, v,1);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readActionBarColor(){
        SharedPreferences sharedPreferences= getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ActionBarColor",getResources().getColor(R.color.colorPrimary));
    }
    public void attendanceDataLoading(){
        try {
            attendanceData=new ArrayList<>();
            String time;
            DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
            Cursor res = helper.getAttendance();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    time=res.getString(1);
                    String s_date=start_date.getText().toString();
                    String e_date=end_date.getText().toString();

                    int year=Integer.parseInt(time.split("/")[2]);
                    int month=Integer.parseInt(time.split("/")[1])+1;
                    int day=Integer.parseInt(time.split("/")[0]);

                    int s_year=Integer.parseInt(s_date.split("/")[2]);
                    int s_month=Integer.parseInt(s_date.split("/")[1]);
                    int s_day=Integer.parseInt(s_date.split("/")[0]);

                    int e_year=Integer.parseInt(e_date.split("/")[2]);
                    int e_month=Integer.parseInt(e_date.split("/")[1]);
                    int e_day=Integer.parseInt(e_date.split("/")[0]);

                    if(year>=s_year && month>=s_month) {
                        if(year<=e_year && month<=e_month){
                            if(s_month==e_month){
                                if(day>=s_day && day<=e_day){
                                    attendanceData.add(new AttendanceData(res.getString(0)+"", res.getString(1)+"", res.getString(2)+"", res.getString(3)+"", res.getString(4)+"",res.getString(5)+""));
                                }
                            }else {
                                attendanceData.add(new AttendanceData(res.getString(0)+"", res.getString(1)+"", res.getString(2)+"", res.getString(3)+"", res.getString(4)+"",res.getString(5)+""));
                            }
                        }
                    }
                }
            }
            recyclerView.setAdapter(new AttendanceAdapter(attendanceData,getApplicationContext()));
            helper.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

    public void noteDataLoading(){
        try {
            noteData=new ArrayList<>();
            String time;
            DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
            Cursor res = helper.getNote();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    time=res.getString(4);
                    String s_date=start_date.getText().toString();
                    String e_date=end_date.getText().toString();

                    int year=Integer.parseInt(time.split("/")[2]);
                    int month=Integer.parseInt(time.split("/")[1]);
                    int day=Integer.parseInt(time.split("/")[0]);

                    int s_year=Integer.parseInt(s_date.split("/")[2]);
                    int s_month=Integer.parseInt(s_date.split("/")[1]);
                    int s_day=Integer.parseInt(s_date.split("/")[0]);

                    int e_year=Integer.parseInt(e_date.split("/")[2]);
                    int e_month=Integer.parseInt(e_date.split("/")[1]);
                    int e_day=Integer.parseInt(e_date.split("/")[0]);

                    if(year>=s_year && month>=s_month) {
                        if(year<=e_year && month<=e_month){
                            if(s_month==e_month){
                                if(day>=s_day && day<=e_day){
                                    noteData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                                }
                            }else {
                                noteData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                            }
                        }
                    }
                }
            }
            recyclerView.setAdapter(new DataNoteAdapter(noteData,getApplicationContext()));
            helper.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

    public void Date(final TextView textView, View v,final int check){
        final Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year+"");
                if(check==1){
                    if(spinner.getSelectedItemPosition()==0){
                        attendanceDataLoading();
                    }else {
                        noteDataLoading();
                    }
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}