package com.cu.aclass.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Activity.MainActivity;
import com.cu.aclass.R;

import java.util.Objects;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import yuku.ambilwarna.AmbilWarnaDialog;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }
    int a_color,l_color;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        RelativeLayout timetable,attendance,note,default_database,data_list;
        final View theme;

        timetable=view.findViewById(R.id.timetable);
        attendance=view.findViewById(R.id.rollcall);
        note=view.findViewById(R.id.note);
        default_database=view.findViewById(R.id.default_database);
        default_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete("Default");
            }
        });

        theme=view.findViewById(R.id.theme_color);
        theme.setBackgroundColor(readActionBarColor());
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTheme(v);
            }
        });
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
    boolean r1=false,r2=false;
    public void createTheme(final View v){
        try {
            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.theme_layout, (LinearLayout) v.findViewById(R.id.layout));
            builder.setView(view);
            final LinearLayout linearLayout=view.findViewById(R.id.layout);
            final View actionbar_color=view.findViewById(R.id.actionBar_color);
            final View layout_color=view.findViewById(R.id.layout_color);

            actionbar_color.setBackgroundColor(readActionBarColor());
            linearLayout.setBackgroundColor(readActionBarColor());
            layout_color.setBackgroundColor(readLayoutColor());
            RadioGroup radioGroup=view.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.custom:
                            actionbar_color.setEnabled(true);
                            layout_color.setEnabled(true);
                            a_color=readActionBarColor();
                            l_color=readLayoutColor();
                            break;
                        case R.id.original_theme:
                            actionbar_color.setEnabled(false);
                            layout_color.setEnabled(false);
                            actionbar_color.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            layout_color.setBackgroundColor(getResources().getColor(R.color.colorDivider));
                            a_color=getResources().getColor(R.color.colorPrimary);
                            l_color=getResources().getColor(R.color.colorDivider);
                            r1=true;r2=true;
                            break;
                    }
                }
            });

            actionbar_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), readActionBarColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            actionbar_color.setBackgroundColor(color);
                            linearLayout.setBackgroundColor(color);
                            a_color = color;
                        }
                    });
                    colorDialog.show();
                    r1=true;
                }
            });
            layout_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), readLayoutColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            layout_color.setBackgroundColor(color);
                            l_color = color;
                        }
                    });
                    colorDialog.show();
                    r2=true;
                }
            });

            TextView ok = view.findViewById(R.id.apply);
            TextView cancel = view.findViewById(R.id.cancel);
            final androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    if(r1 && r2){
                        saveActionBarColor(a_color);
                        saveLayoutColor(l_color);
                        dialog.dismiss();
                        getContext().startActivity(new Intent(getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    }else {
                        if(!r1){
                            Toast.makeText(getContext(),"ActionBar set Color",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"Layout set Color",Toast.LENGTH_SHORT).show();
                        }
                    }
                    }
            });
        }catch (Exception e){
            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveActionBarColor(int color){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("ActionBarColor",color).apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readActionBarColor(){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ActionBarColor",getResources().getColor(R.color.colorPrimary));
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveLayoutColor(int color){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("LayoutColor",color).apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readLayoutColor(){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("LayoutColor",getResources().getColor(R.color.colorDivider));
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
                        case "Default":
                            deleteTimeTable();
                            deleteNote();
                            deleteAttendanceTable();
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