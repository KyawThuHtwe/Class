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
import com.cu.aclass.Report.MonthlyActivity;
import com.cu.aclass.Report.WeeklyActivity;
import com.cu.aclass.TimeTable.MonActivity;
import com.cu.aclass.TimeTable.SunActivity;

public class ReportFragment extends Fragment {

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_report, container, false);
        final TabHost tabHost = view.findViewById(R.id.tabHost);
        LocalActivityManager localActivityManager=new LocalActivityManager(getActivity(),false);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);
        TabHost.TabSpec spec;

        spec=tabHost.newTabSpec("Weekly");
        spec.setIndicator("Weekly");
        spec.setContent(new Intent(getActivity(), WeeklyActivity.class));
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("Monthly");
        spec.setIndicator("Monthly");
        spec.setContent(new Intent(getActivity(), MonthlyActivity.class));
        tabHost.addTab(spec);

        return view;
    }
}