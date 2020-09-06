package com.cu.aclass.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cu.aclass.AboutActivity;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.R;

import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        RelativeLayout timetable,attendance,about,note,feedback,share;
        feedback=view.findViewById(R.id.feedback);
        share=view.findViewById(R.id.share);
        about=view.findViewById(R.id.about);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:copypaste673@gmail.com")));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        timetable=view.findViewById(R.id.timetable);
        attendance=view.findViewById(R.id.rollcall);
        note=view.findViewById(R.id.note);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete("Attendance");
            }
        });
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete("TimeTable");
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDelete("Note");
            }
        });

        return view;
    }
    public void deleteTimeTable(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteTimeTable();
            if(result){
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),"No database",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteNote(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteNoteTable();
            if(result){
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),"No database",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAttendanceTable(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteAttendanceTable();
            if(result){
                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),"No database",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void confirmDelete(final String table) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setTitle("Delete");
            builder.setMessage("Are you sure you want to Delete?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (table) {
                        case "TimeTable":
                            deleteTimeTable();
                            deleteAttendanceTable();
                            break;
                        case "Attendance":
                            deleteAttendanceTable();
                            break;
                        case "Note":
                            deleteNote();
                            break;
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}