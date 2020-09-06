package com.cu.aclass.Fragments;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.cu.aclass.R;
import com.cu.aclass.TimeTable.FriActivity;
import com.cu.aclass.TimeTable.MonActivity;
import com.cu.aclass.TimeTable.SatActivity;
import com.cu.aclass.TimeTable.SunActivity;
import com.cu.aclass.TimeTable.ThuActivity;
import com.cu.aclass.TimeTable.TueActivity;
import com.cu.aclass.TimeTable.WedActivity;

import java.util.Calendar;

public class TimeFragment extends Fragment {

    public TimeFragment() {
        // Required empty public constructor
    }

    public static TimeFragment newInstance() {
        TimeFragment fragment = new TimeFragment();
        return fragment;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_time, container, false);
        try {
            TabHost tabHost = view.findViewById(R.id.tabHost);
            LocalActivityManager localActivityManager=new LocalActivityManager(getActivity(),false);
            localActivityManager.dispatchCreate(savedInstanceState);
            tabHost.setup(localActivityManager);
            TabHost.TabSpec spec;

            spec=tabHost.newTabSpec("Sun");
            spec.setIndicator("Sun");
            spec.setContent(new Intent(getActivity(), SunActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Mon");
            spec.setIndicator("Mon");
            spec.setContent(new Intent(getActivity(), MonActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Tue");
            spec.setIndicator("Tue");
            spec.setContent(new Intent(getActivity(), TueActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Wed");
            spec.setIndicator("Wed");
            spec.setContent(new Intent(getActivity(), WedActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Thu");
            spec.setIndicator("Thu");
            spec.setContent(new Intent(getActivity(), ThuActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Fri");
            spec.setIndicator("Fri");
            spec.setContent(new Intent(getActivity(), FriActivity.class));
            tabHost.addTab(spec);

            spec=tabHost.newTabSpec("Sat");
            spec.setIndicator("Sat");
            spec.setContent(new Intent(getActivity(), SatActivity.class));
            tabHost.addTab(spec);

            ////
            Intent getday=getActivity().getIntent();
            String title=getday.getStringExtra("getDay");
            if(title!=null) {
                switch (title) {
                    case "Sunday":
                        tabHost.setCurrentTab(0);
                        break;
                    case "Monday":
                        tabHost.setCurrentTab(1);
                        break;
                    case "Tuesday":
                        tabHost.setCurrentTab(2);
                        break;
                    case "Wednesday":
                        tabHost.setCurrentTab(3);
                        break;
                    case "Thursday":
                        tabHost.setCurrentTab(4);
                        break;
                    case "Friday":
                        tabHost.setCurrentTab(5);
                        break;
                    case "Saturday":
                        tabHost.setCurrentTab(6);
                        break;
                }
            }else {
                Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_WEEK);
                switch (day){
                    case 1:
                        tabHost.setCurrentTab(0);
                        break;
                    case 2:
                        tabHost.setCurrentTab(1);
                        break;
                    case 3:
                        tabHost.setCurrentTab(2);
                        break;
                    case 4:
                        tabHost.setCurrentTab(3);
                        break;
                    case 5:
                        tabHost.setCurrentTab(4);
                        break;
                    case 6:
                        tabHost.setCurrentTab(5);
                        break;
                    case 7:
                        tabHost.setCurrentTab(6);
                        break;
                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}