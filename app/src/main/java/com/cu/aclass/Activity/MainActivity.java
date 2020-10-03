package com.cu.aclass.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.cu.aclass.Fragments.NoteFragment;
import com.cu.aclass.Fragments.ReportFragment;
import com.cu.aclass.Fragments.SettingFragment;
import com.cu.aclass.Fragments.TimeFragment;
import com.cu.aclass.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    FrameLayout frameLayout;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"RestrictedApi", "ResourceType", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
        frameLayout=findViewById(R.id.frameLayout);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        Calendar calendar=Calendar.getInstance();
        String cur_date= DateFormat.getDateInstance().format(calendar.getTime());
        assert actionBar != null;
        actionBar.setSubtitle(cur_date);
        final SmoothBottomBar smoothBottomBar=findViewById(R.id.smoothBottomBar);
        String fragment=getIntent().getStringExtra("Fragment");
        if(fragment==null){
            //smoothBottomBar.setActiveItem(0);
            LoadFragment(TimeFragment.newInstance());
        }else if(fragment.equals("Note")){
            smoothBottomBar.setActiveItem(2);
            LoadFragment(NoteFragment.newInstance());
        }
        toolbar.setBackgroundColor(readActionBarColor());
        getWindow().setStatusBarColor(readActionBarColor());
        getWindow().setNavigationBarColor(readActionBarColor());
        frameLayout.setBackgroundColor(readLayoutColor());
        smoothBottomBar.setBackgroundColor(readActionBarColor());
        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelect(int i) {
                switch (i){
                    case 0:
                        LoadFragment(TimeFragment.newInstance());
                        Calendar calendar=Calendar.getInstance();
                        String cur_date= DateFormat.getDateInstance().format(calendar.getTime());
                        assert actionBar != null;
                        actionBar.setSubtitle(cur_date);
                        break;
                    case 1:
                        LoadFragment(ReportFragment.newInstance());
                        actionBar.setSubtitle("Report");
                        break;
                    case 2:
                        LoadFragment(NoteFragment.newInstance());
                        actionBar.setSubtitle("Note");
                        break;
                    case 3:
                        LoadFragment(SettingFragment.newInstance());
                        actionBar.setSubtitle("Setting");
                        break;
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readActionBarColor(){
        SharedPreferences sharedPreferences= getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ActionBarColor",getResources().getColor(R.color.colorPrimary));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readLayoutColor(){
        SharedPreferences sharedPreferences=getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("LayoutColor",getResources().getColor(R.color.colorTextHint));
    }

    public void LoadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,fragment).commit();
    }

}