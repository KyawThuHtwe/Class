package com.cu.aclass.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Activity.DataListActivity;
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
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        RelativeLayout timetable,attendance,note,database_list;
        final View theme;

        timetable=view.findViewById(R.id.timetable);
        attendance=view.findViewById(R.id.rollcall);
        note=view.findViewById(R.id.note);
        database_list=view.findViewById(R.id.database_list);
        database_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getContext()).startActivity(new Intent(getContext(), DataListActivity.class));
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
    public void createTheme(final View v){
        try {
            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.theme_layout, (LinearLayout) v.findViewById(R.id.layout));
            builder.setView(view);
            final View actionbar_color=view.findViewById(R.id.actionBar_color);
            final View layout_color=view.findViewById(R.id.layout_color);
            final View time_color=view.findViewById(R.id.time_color);
            final View note_color=view.findViewById(R.id.note_color);
            final View report_color=view.findViewById(R.id.report_color);

            actionbar_color.setBackgroundColor(readActionBarColor());
            layout_color.setBackgroundColor(readLayoutColor());
            time_color.setBackgroundColor(readTimeColor());
            note_color.setBackgroundColor(readNoteColor());
            report_color.setBackgroundColor(readReportColor());
            report_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), readTimeColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            report_color.setBackgroundColor(color);
                            saveReportColor(color);
                        }
                    });
                    colorDialog.show();
                }
            });
            time_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), readTimeColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            time_color.setBackgroundColor(color);
                            saveTimeColor(color);
                        }
                    });
                    colorDialog.show();
                }
            });
            note_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), readTimeColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("ResourceType")
                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {
                            note_color.setBackgroundColor(color);
                            saveNoteColor(color);
                        }
                    });
                    colorDialog.show();
                }
            });

            RadioGroup radioGroup=view.findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.custom:
                            actionbar_color.setEnabled(true);
                            layout_color.setEnabled(true);
                            time_color.setEnabled(true);
                            note_color.setEnabled(true);
                            report_color.setEnabled(true);
                            break;
                        case R.id.original_theme:
                            actionbar_color.setEnabled(false);
                            layout_color.setEnabled(false);
                            time_color.setEnabled(false);
                            note_color.setEnabled(false);
                            report_color.setEnabled(false);
                            actionbar_color.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            layout_color.setBackgroundColor(getResources().getColor(R.color.colorTextHint));
                            time_color.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            note_color.setBackgroundColor(getResources().getColor(R.color.colorDivider));
                            report_color.setBackgroundColor(getResources().getColor(R.color.colorDivider));
                            saveActionBarColor(getResources().getColor(R.color.colorPrimary));
                            saveLayoutColor(getResources().getColor(R.color.colorTextHint));
                            saveTimeColor(getResources().getColor(R.color.colorWhite));
                            saveNoteColor(getResources().getColor(R.color.colorDivider));
                            saveReportColor(getResources().getColor(R.color.colorDivider));

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
                            saveActionBarColor(color);
                        }
                    });
                    colorDialog.show();
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
                            saveLayoutColor(color);
                        }
                    });
                    colorDialog.show();
                }
            });

            TextView ok = view.findViewById(R.id.apply);
            final androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            ok.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    getContext().startActivity(new Intent(getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
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
    public void saveTimeColor(int color){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("TimeColor",color).apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readTimeColor(){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("TimeColor",getResources().getColor(R.color.colorWhite));
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveNoteColor(int color){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("NoteColor",color).apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readNoteColor(){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("NoteColor",getResources().getColor(R.color.colorDivider));
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
        return sharedPreferences.getInt("LayoutColor",getResources().getColor(R.color.colorTextHint));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void saveReportColor(int color){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("ReportColor",color).apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readReportColor(){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ReportColor",getResources().getColor(R.color.colorDivider));
    }

    @SuppressLint("SetTextI18n")
    public void deleteTimeTable(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteTimeTable();
            if(result){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_time_add_successful, null);
                Toast toast = Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT);
                LinearLayout linearLayout = view.findViewById(R.id.layout);
                linearLayout.setBackgroundColor(readActionBarColor());
                TextView type=view.findViewById(R.id.type);
                type.setText("Deleted Successfully");
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();
            }else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_no_database, null);
                Toast toast = Toast.makeText(getContext(), "No database", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("SetTextI18n")
    public void deleteNote(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteNoteTable();
            if(result){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_time_add_successful, null);
                Toast toast = Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT);
                LinearLayout linearLayout = view.findViewById(R.id.layout);
                linearLayout.setBackgroundColor(readActionBarColor());
                TextView type=view.findViewById(R.id.type);
                type.setText("Deleted Successfully");
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();
            }else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_no_database, null);
                Toast toast = Toast.makeText(getContext(), "No database", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void deleteAttendanceTable(){
        DatabaseHelper helper=new DatabaseHelper(getContext());
        try{
            boolean result=helper.deleteAttendanceTable();
            if(result){
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_time_add_successful, null);
                Toast toast = Toast.makeText(getContext(), "Deleted Successfully", Toast.LENGTH_SHORT);
                LinearLayout linearLayout = view.findViewById(R.id.layout);
                linearLayout.setBackgroundColor(readActionBarColor());
                TextView type=view.findViewById(R.id.type);
                type.setText("Deleted Successfully");
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();
            }else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.toast_no_database, null);
                Toast toast = Toast.makeText(getContext(), "No database", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setView(view);
                toast.show();            }
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