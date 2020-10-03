package com.cu.aclass.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Model.NoteData;
import com.cu.aclass.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class DataNoteAdapter extends RecyclerView.Adapter<DataNoteAdapter.ViewHolder> {
    ArrayList<NoteData> noteData;
    Context context;
    public DataNoteAdapter(ArrayList<NoteData> noteData, Context context) {
        this.noteData=noteData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.data_note_layout,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.layout.setBackgroundColor(readNoteColor());
        holder.type.setText(noteData.get(position).getTitle());
        holder.subject.setText(noteData.get(position).getSubject());
        holder.time.setText(time(noteData.get(position).getTime()));
        String date_time=noteData.get(position).getDate();

        int s_year=Integer.parseInt(date_time.split("/")[2]);
        int s_month=Integer.parseInt(date_time.split("/")[1]);
        int s_day=Integer.parseInt(date_time.split("/")[0]);

        holder.date1.setText(s_day+"");
        holder.date2.setText(s_month+"");
        holder.date3.setText(s_year+"");
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readNoteColor(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("NoteColor",context.getResources().getColor(R.color.colorDivider));
    }

    public String time(String time){
        int hr = 0,min;
        String s=null,des;
        try {
            hr=Integer.parseInt(time.split(":")[0]);
            min=Integer.parseInt(time.split(":")[1].replaceAll("\\D+","").replaceAll("^0+",""));
            s=min+"";
            if(s.length()==1){
                s="0"+s;
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }

        }catch (NumberFormatException nfe){
            Toast.makeText(context,nfe.getMessage(),Toast.LENGTH_SHORT).show();
            if(s==null){
                s="00";
            }
            if(hr>12){
                hr-=12;
                des=" PM";
            }else {
                des=" AM";
            }
        }

        return hr+":"+s+des;
    }

    @Override
    public int getItemCount() {
        return noteData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,date1,date2,date3,subject,time;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.date1=itemView.findViewById(R.id.date1);
            this.date2=itemView.findViewById(R.id.date2);
            this.date3=itemView.findViewById(R.id.date3);
            this.subject=itemView.findViewById(R.id.subject);
            this.time=itemView.findViewById(R.id.time);
            this.layout=itemView.findViewById(R.id.layout);
        }
    }
}
