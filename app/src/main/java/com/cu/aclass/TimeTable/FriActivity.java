package com.cu.aclass.TimeTable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Adapter.TimeAddDataAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.TimeAddData;
import com.cu.aclass.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class FriActivity extends AppCompatActivity {

    TextView check_data;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    ArrayList<TimeAddData> timeAddData;
    DatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fri);
        helper=new DatabaseHelper(this);
        check_data=findViewById(R.id.check_data);
        floatingActionButton=findViewById(R.id.floatingActionButton);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataLoad();
        try {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && floatingActionButton.getVisibility() == VISIBLE) {
                        floatingActionButton.hide();
                    } else if (dy < 0 && floatingActionButton.getVisibility() != VISIBLE) {
                        floatingActionButton.show();
                    }
                }
            });
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),AddTimeActivity.class);
                    intent.putExtra("Day","Friday");
                    startActivity(intent);
                }
            });
            floatingActionButton.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("WrongConstant")
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public boolean onLongClick(View v) {
                    floatingActionButton.setLayoutDirection(Gravity.CENTER);
                    return false;
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    public void dataLoad(){
        timeAddData = new ArrayList<>();
        try {
            Cursor res = helper.getAllTime();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(9).equals("Friday")) {
                        if (!res.getString(3).equals("Subject")) {
                            timeAddData.add(new TimeAddData(res.getString(0), res.getString(1), res.getString(2), res.getString(3),
                                    res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9)));
                        }
                    }
                }
            }
            recyclerView.setAdapter(new TimeAddDataAdapter(getApplicationContext(), timeAddData));
            helper.close();
            if(timeAddData.size()>0){
                check_data.setVisibility(View.GONE);
            }else{
                check_data.setVisibility(VISIBLE);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}