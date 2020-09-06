package com.cu.aclass.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;
import com.cu.aclass.Report.MonthlyActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder> {
    ArrayList<SubjectData> subjectData;
    int select_month;
    Context context;
    public MonthlyReportAdapter(Context context, ArrayList<SubjectData> subjectData, int select_month) {
        this.context=context;
        this.subjectData=subjectData;
        this.select_month=select_month;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.cutom_report,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.subject.setText(subjectData.get(position).getSubject());
        if(holder.percent.getText()!=null){
            holder.percent.setText("");
        }
        double total=0;
        double count=0;
        DatabaseHelper helper=new DatabaseHelper(context);
        try {
            Cursor resc = helper.getAttendance();
            if (resc != null && resc.getCount() > 0) {
                while (resc.moveToNext()) {
                    if(resc.getString(2).equals(subjectData.get(position).getSubject()) && resc.getString(3).equals("1")){
                        try {
                            total=subjectData.get(position).getTotal();
                            int month=Integer.parseInt(resc.getString(1).split("/")[1]);
                            if(select_month==month){
                                count++;
                            }
                        }catch (NumberFormatException nfe){
                            Toast.makeText(context, nfe.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            double ans;
            if(count>0 && total>0) {
                ans=(count/(total*4)*100.0);
            }else {
                ans=0;
            }
            String s=ans+"";
            String fs;
            if(s.length()>4){
                fs=s.charAt(0)+""+s.charAt(1)+""+s.charAt(2)+""+s.charAt(3);
            }else {
                fs=s;
            }
            int attendance=subjectData.get(position).getTotal()*4;
            int present= (int) count;
            int absent= (int) (attendance-count);
            holder.percent.setText(fs+"%");
            holder.progressBar.setProgress((int) ans);
            holder.attendance1.setText(attendance+"");
            holder.attendance2.setText(present+"");
            holder.attendance3.setText(absent+"");
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return subjectData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subject;
        public ProgressBar progressBar;
        public TextView percent;
        public TextView attendance1,attendance2,attendance3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subject = itemView.findViewById(R.id.subject);
            this.progressBar = itemView.findViewById(R.id.circle_progress_bar);
            this.percent = itemView.findViewById(R.id.percent);
            this.attendance1 = itemView.findViewById(R.id.attendance1);
            this.attendance2 = itemView.findViewById(R.id.attendance2);
            this.attendance3 = itemView.findViewById(R.id.attendance3);
        }
    }
}
