package com.cu.aclass.Fragments;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.cu.aclass.Adapter.MonthlyReportAdapter;
import com.cu.aclass.Adapter.WeeklyReportAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;
import com.cu.aclass.TimeTable.MonActivity;
import com.cu.aclass.TimeTable.SunActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class ReportFragment extends Fragment {

    RecyclerView recyclerView;
    public Spinner spinner_report,spinner;
    String[] report={"Weekly","Monthly"};
    String[] week={"1st week","2nd week","3rd week","4th week"};
    String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    ArrayAdapter reportAdapter,weeklyAdapter,monthlyAdapter;
    ArrayList<SubjectData> subjectData;
    DatabaseHelper helper;
    int total = 0;
    String subject;
    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_report, container, false);
        spinner_report=view.findViewById(R.id.spinner_report);
        spinner=view.findViewById(R.id.spinner);
        reportAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,report);
        weeklyAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,week);
        monthlyAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,month);

        spinner_report.setAdapter(reportAdapter);
        selectSpinner();
        spinner_report.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        helper=new DatabaseHelper(getContext());
        subjectData=new ArrayList<>();
        dataLoadWeek(spinner.getSelectedItemPosition());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (spinner_report.getSelectedItemPosition()){
                    case 0:
                        dataLoadWeek(spinner.getSelectedItemPosition());break;
                    case 1:
                        dataLoadMonth(spinner.getSelectedItemPosition());break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
    public void selectSpinner(){
        switch (spinner_report.getSelectedItemPosition()){
            case 0:
                spinner.setAdapter(weeklyAdapter);break;
            case 1:
                spinner.setAdapter(monthlyAdapter);break;
        }
        Calendar calendar=Calendar.getInstance();
        switch (spinner_report.getSelectedItemPosition()){
            case 0:
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
                break;
            case 1:
                int current_month = calendar.get(Calendar.MONTH);
                switch (current_month) {
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

        }

    }
    public void dataLoadWeek(int selectweek){
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

            recyclerView.setAdapter(new WeeklyReportAdapter(getContext(),subjectData,selectweek));
            helper.close();

        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    public void dataLoadMonth(int selectmonth) {
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

            recyclerView.setAdapter(new MonthlyReportAdapter(getContext(),subjectData, selectmonth));
            helper.close();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}