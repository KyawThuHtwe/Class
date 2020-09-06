package com.cu.aclass.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.cu.aclass.Adapter.WeeklyReportAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Calendar;

public class WeeklyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public Spinner spinner;
    String[] week={"1st week","2nd week","3rd week","4th week"};
    ArrayAdapter<String> arrayAdapter;
    ArrayList<SubjectData> subjectData;
    DatabaseHelper helper;
    int total=0;
    String subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        spinner=findViewById(R.id.spinner);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,week);
        spinner.setAdapter(arrayAdapter);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        helper=new DatabaseHelper(this);
        subjectData=new ArrayList<>();

        Calendar calendar=Calendar.getInstance();
        int current_week=calendar.get(Calendar.DAY_OF_MONTH);
        if(current_week<8){
            spinner.setSelection(0);
        }else if(current_week < 15){
            spinner.setSelection(1);
        }else if(current_week < 22){
            spinner.setSelection(2);
        }else {
            spinner.setSelection(3);
        }
        dataLoad(spinner.getSelectedItemPosition());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dataLoad(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void dataLoad(int selectweek){
        subjectData.clear();
        try {
            Cursor res = helper.subjectTotalCount();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    total=Integer.parseInt(res.getString(1));
                    subject=res.getString(0);
                    subjectData.add(new SubjectData(total,subject));
                    //Toast.makeText(getApplicationContext(),total+subject,Toast.LENGTH_SHORT).show();
                }
            }

            recyclerView.setAdapter(new WeeklyReportAdapter(getApplicationContext(),subjectData,selectweek));
            helper.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
}