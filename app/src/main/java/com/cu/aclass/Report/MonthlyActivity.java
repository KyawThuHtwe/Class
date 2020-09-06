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

import com.cu.aclass.Adapter.MonthlyReportAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthlyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public Spinner spinner;
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    ArrayAdapter<String> arrayAdapter;
    ArrayList<SubjectData> subjectData;
    DatabaseHelper helper;
    int total = 0;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        spinner = findViewById(R.id.spinner);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, month);
        spinner.setAdapter(arrayAdapter);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        helper = new DatabaseHelper(this);
        subjectData = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int currentmonth = calendar.get(Calendar.MONTH);

        switch (currentmonth) {
            case 1:
                spinner.setSelection(1);
                break;
            case 2:
                spinner.setSelection(2);
                break;
            case 3:
                spinner.setSelection(3);
                break;
            case 4:
                spinner.setSelection(4);
                break;
            case 5:
                spinner.setSelection(5);
                break;
            case 6:
                spinner.setSelection(6);
                break;
            case 7:
                spinner.setSelection(7);
                break;
            case 8:
                spinner.setSelection(8);
                break;
            case 9:
                spinner.setSelection(9);
                break;
            case 10:
                spinner.setSelection(10);
                break;
            case 11:
                spinner.setSelection(11);
                break;
            case 12:
                spinner.setSelection(12);
                break;
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

    public void dataLoad(int selectmonth) {
        subjectData.clear();
        try {
            Cursor res = helper.subjectTotalCount();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    total = Integer.parseInt(res.getString(1));
                    subject = res.getString(0);
                    subjectData.add(new SubjectData(total, subject));
                }
            }

            recyclerView.setAdapter(new MonthlyReportAdapter(getApplicationContext(),subjectData, selectmonth));
            helper.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}