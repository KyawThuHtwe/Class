package com.cu.aclass.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Adapter.NoteAdapter;
import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.NoteData;
import com.cu.aclass.Note.AddNoteActivity;
import com.cu.aclass.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import static android.view.View.VISIBLE;

public class NoteFragment extends Fragment {


    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }
    FloatingActionButton floatingActionButton;
    RecyclerView today_recyclerView,tomorrow_recyclerView,other_day_recyclerView;
    ArrayList<NoteData> other_day_noteListData,tomorrow_noteListData,today_noteListData;
    TextView today,tomorrow,other_day,no_note;
    NestedScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_note, container, false);
        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddNoteActivity.class);
                intent.putExtra("Note_Action","Add Note");
                startActivity(intent);
            }
        });
        today=view.findViewById(R.id.today);
        tomorrow=view.findViewById(R.id.tomorrow);
        other_day=view.findViewById(R.id.other_day);
        no_note=view.findViewById(R.id.no_note);
        scrollView=view.findViewById(R.id.scrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>0 && floatingActionButton.getVisibility()== VISIBLE){
                    floatingActionButton.setVisibility(View.GONE);
                }else if(scrollY==0 && floatingActionButton.getVisibility()!= VISIBLE){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });

        try {
            today_recyclerView = view.findViewById(R.id.today_recyclerView);
            today_recyclerView.setHasFixedSize(true);
            tomorrow_recyclerView = view.findViewById(R.id.tomorrow_recyclerView);
            tomorrow_recyclerView.setHasFixedSize(true);
            other_day_recyclerView = view.findViewById(R.id.other_day_recyclerView);
            other_day_recyclerView.setHasFixedSize(true);

            LinearLayoutManager manager_today = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            today_recyclerView.setLayoutManager(manager_today);
            LinearLayoutManager manager_other_day = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
            other_day_recyclerView.setLayoutManager(manager_other_day);
            LinearLayoutManager manager_tomorrow = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
            tomorrow_recyclerView.setLayoutManager(manager_tomorrow);
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        ///notes
        noteLoad();
        return view;
    }

    private void noteLoad() {
        DatabaseHelper helper=new DatabaseHelper(getContext());
        other_day_noteListData=new ArrayList<>();
        today_noteListData=new ArrayList<>();
        tomorrow_noteListData=new ArrayList<>();
        tomorrow_noteListData.clear();
        other_day_noteListData.clear();
        today_noteListData.clear();
        String time;
        try {
            Cursor res = helper.getNote();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    time=res.getString(4);
                    Calendar c= Calendar.getInstance();
                    int year=c.get(Calendar.YEAR);
                    int month=c.get(Calendar.MONTH)+1;
                    int day=c.get(Calendar.DAY_OF_MONTH);
                    int s_year=Integer.parseInt(time.split("/")[2]);
                    int s_month=Integer.parseInt(time.split("/")[1]);
                    int s_day=Integer.parseInt(time.split("/")[0]);
                    if(s_day==day && s_month==month && s_year==year) {
                        //today.setVisibility(VISIBLE);
                        today_noteListData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                    }else if(s_day>day && s_month==month && s_year==year){
                        int left_day=s_day-day;
                        if(left_day==1){
                            //tomorrow.setVisibility(VISIBLE);
                            tomorrow_noteListData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                        }else {
                            //other_day.setVisibility(VISIBLE);
                            other_day_noteListData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                        }
                    }else if(s_day>day && s_month>=month && s_year>=year) {
                        //other_day.setVisibility(VISIBLE);
                        other_day_noteListData.add(new NoteData(res.getString(0), res.getString(1), res.getString(2), res.getString(3), res.getString(4)));
                    }else if(s_day<day && s_month<=month && s_year<=year) {
                       /* other_day.setVisibility(View.VISIBLE);
                        today.setVisibility(View.VISIBLE);
                        tomorrow.setVisibility(View.VISIBLE);
                        */
                    }else {
                        other_day.setVisibility(View.GONE);
                        today.setVisibility(View.GONE);
                        tomorrow.setVisibility(View.GONE);
                    }
                }
            }
            if(today_noteListData.size()>0||tomorrow_noteListData.size()>0||other_day_noteListData.size()>0){
                no_note.setVisibility(View.GONE);
            }else {
                no_note.setVisibility(VISIBLE);
            }
            today_recyclerView.setAdapter(new NoteAdapter(today_noteListData,getContext()));
            tomorrow_recyclerView.setAdapter(new NoteAdapter(tomorrow_noteListData,getContext()));
            other_day_recyclerView.setAdapter(new NoteAdapter(other_day_noteListData,getContext()));
            helper.close();

        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

}