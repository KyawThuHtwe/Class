package com.cu.aclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Adapter.NoteAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Fragments.NoteFragment;
import com.cu.aclass.Fragments.ReportFragment;
import com.cu.aclass.Fragments.SettingFragment;
import com.cu.aclass.Fragments.TimeFragment;
import com.cu.aclass.Model.NoteData;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.toolbar);
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
            smoothBottomBar.setActiveItem(1);
            LoadFragment(NoteFragment.newInstance());
        }

        smoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
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
    public void LoadFragment(Fragment fragment){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout,fragment).commit();
    }

}