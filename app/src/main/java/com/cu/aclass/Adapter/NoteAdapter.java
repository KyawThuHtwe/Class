package com.cu.aclass.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.MainActivity;
import com.cu.aclass.Model.NoteData;
import com.cu.aclass.Note.AddNoteActivity;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    ArrayList<NoteData> noteData;
    Context context;
    public NoteAdapter(ArrayList<NoteData> noteData, Context context) {
        this.noteData=noteData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.custom_note,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action(v,noteData.get(position).getId(),noteData.get(position).getTitle(),noteData.get(position).getSubject(),noteData.get(position).getTime(),noteData.get(position).getDate());
            }
        });
        holder.type.setText(noteData.get(position).getTitle());
        holder.subject.setText(noteData.get(position).getSubject());
        holder.time.setText(time(noteData.get(position).getTime()));
        String date_time=noteData.get(position).getDate();
        Calendar c= Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        int day=c.get(Calendar.DAY_OF_MONTH);
        int s_year=Integer.parseInt(date_time.split("/")[2]);
        int s_month=Integer.parseInt(date_time.split("/")[1]);
        int s_day=Integer.parseInt(date_time.split("/")[0]);

        holder.date1.setText(s_day+"");
        holder.date2.setText(s_month+"");
        holder.date3.setText(s_year+"");
        if(s_day==day && s_month==month && s_year==year) {
            holder.day_left.setText("Today");
        }else if(s_day>day && s_month==month && s_year==year){
            int left_day=s_day-day;
            if(left_day==1){
                holder.day_left.setText("Tomorrow");
            }else {
                holder.day_left.setText(left_day+" day left");
            }
        }
    }

    private void action(View v, String id, final String title, final String subject, final String time, final String day) {
        try {
            /*
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.action_bottom_sheet, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
            dialog.setContentView(view);
            dialog.show();
             */
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.action_bottom_sheet,null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            ImageView close=view.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            final Button edit = view.findViewById(R.id.edit);
            Button delete=view.findViewById(R.id.delete);
            final String eid=id;
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Intent edit_intent = new Intent(context.getApplicationContext(), AddNoteActivity.class);
                    edit_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    edit_intent.putExtra("Note_Action","Edit Note");
                    edit_intent.putExtra("Id",eid);
                    edit_intent.putExtra("Title",title);
                    edit_intent.putExtra("Subject",subject);
                    edit_intent.putExtra("Time",time);
                    edit_intent.putExtra("Date",day);
                    context.startActivity(edit_intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DatabaseHelper helper = new DatabaseHelper(context);
                        int res = helper.deleteNote(eid);
                        if (res == 1) {
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent main=new Intent(context, MainActivity.class);
                            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.putExtra("Fragment","Note");
                            context.startActivity(main);
                        } else {
                            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }catch (Exception e){
            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

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
        public TextView type,date1,date2,date3,subject,time,day_left;
        public LinearLayout show;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.type=itemView.findViewById(R.id.type);
            this.date1=itemView.findViewById(R.id.date1);
            this.date2=itemView.findViewById(R.id.date2);
            this.date3=itemView.findViewById(R.id.date3);
            this.subject=itemView.findViewById(R.id.subject);
            this.time=itemView.findViewById(R.id.time);
            this.day_left=itemView.findViewById(R.id.day_left);
            this.show=itemView.findViewById(R.id.layout);
        }
    }
}
