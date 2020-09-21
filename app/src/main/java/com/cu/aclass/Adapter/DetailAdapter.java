package com.cu.aclass.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.Model.AttendanceData;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAdapter  extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{
    Context context;
    ArrayList<AttendanceData> attendanceData;

    public DetailAdapter(ArrayList<AttendanceData> attendanceData, Context context) {
        this.attendanceData=attendanceData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_report_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(attendanceData.get(position).getId().equals("0")){
            holder.attendance_text.setText("");
            holder.day_text.setText("");

        }else {
            if(attendanceData.get(position).getVote().equals("0")){
                holder.attendance_text.setText("Absent");
            }else if(attendanceData.get(position).getVote().equals("1")){
                holder.attendance_text.setText("Present");
            }else {
                holder.attendance_text.setVisibility(View.GONE);
            }
            String date_time=attendanceData.get(position).getDate();
            int s_year=Integer.parseInt(date_time.split("/")[2]);
            int s_month=Integer.parseInt(date_time.split("/")[1])+1;
            int s_day=Integer.parseInt(date_time.split("/")[0]);
            holder.day_text.setText(s_day+"/"+s_month+"/"+s_year);
        }

    }

    @Override
    public int getItemCount() {
        return attendanceData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView day_text,attendance_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.day_text=itemView.findViewById(R.id.day);
            this.attendance_text=itemView.findViewById(R.id.attendance);

        }
    }
}
