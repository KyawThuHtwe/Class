package com.cu.aclass.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.MainActivity;
import com.cu.aclass.Model.TimeAddData;
import com.cu.aclass.R;
import com.cu.aclass.TimeTable.AddTimeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TimeAddDataAdapter extends RecyclerView.Adapter<TimeAddDataAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeAddData> timeAddData;
    String result ="1";

    public TimeAddDataAdapter(Context context, ArrayList<TimeAddData> timeAddData) {
        this.context = context;
        this.timeAddData = timeAddData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_add_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.present_radio:
                        result="1";break;
                    case R.id.absent_radio:
                        result="0";break;
                }
            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                present(v,timeAddData.get(position).getId()+"",timeAddData.get(position).getSubject()+"",timeAddData.get(position).getDay()+"",result);
            }
        });
        holder.subject.setText(timeAddData.get(position).getSubject());
        holder.start.setText(time(timeAddData.get(position).getStart()));
        holder.end.setText(time(timeAddData.get(position).getEnd()));

        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet(v,timeAddData.get(position).getId()+"",holder.start.getText()+"",holder.end.getText()+"",
                        timeAddData.get(position).getSubject()+"",timeAddData.get(position).getType()+"",timeAddData.get(position).getRoom()+"",
                        timeAddData.get(position).getTeacher()+"",timeAddData.get(position).getContact()+"",timeAddData.get(position).getNotes()+"",
                        timeAddData.get(position).getDay()+"");
            }
        });
    }

    private void present(View v, final String id, final String subject, final String day, final String vote) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.attendance_submit, (LinearLayout) v.findViewById(R.id.layout));
            builder.setView(view);
            TextView today=view.findViewById(R.id.today);
            Calendar calendar=Calendar.getInstance();
            String cur_date= DateFormat.getDateInstance().format(calendar.getTime());
            today.setText(cur_date);

            Button cancel = view.findViewById(R.id.cancel);
            Button ok = view.findViewById(R.id.ok);
            final AlertDialog dialog = builder.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = new DatePicker(context);
                    DatabaseHelper helper = new DatabaseHelper(context);
                    boolean result = helper.insertAttendance(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear(), subject + "", vote+"",day+"",id+"");
                    if (result) {
                        Toast.makeText(v.getContext(),"Successful",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(v.getContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(v.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void openBottomSheet(View v,String id, String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, String day) {
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.time_bottom_sheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        dialog.setContentView(view);
        dialog.show();
        Button close=view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView start,end,subject,type,room,teacher,contact,note;
        start=view.findViewById(R.id.infostart);
        end=view.findViewById(R.id.infoend);
        subject=view.findViewById(R.id.infosubject);
        type=view.findViewById(R.id.infotype);
        room=view.findViewById(R.id.inforoom);
        teacher=view.findViewById(R.id.infoteacher);
        contact=view.findViewById(R.id.infocontact);
        note=view.findViewById(R.id.infonote);

        start.setText(s);
        end.setText(s1);
        subject.setText(s2);
        type.setText(s3);
        room.setText(s4);
        teacher.setText(s5);
        contact.setText(s6);
        note.setText(s7);
        final String eid=id;
        final String eday=day;
        Button edit=view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final Intent edit_intent = new Intent(context.getApplicationContext(), AddTimeActivity.class);
                edit_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                edit_intent.putExtra("Id",eid);
                context.startActivity(edit_intent);
            }
        });
        Button delete=view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHelper helper = new DatabaseHelper(context);
                    int res = helper.deleteTime(eid);
                    if (res == 1) {
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent main=new Intent(context, MainActivity.class);
                        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        main.putExtra("getDay",eday);
                        context.startActivity(main);
                    } else {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        return timeAddData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioGroup radioGroup;
        RadioButton present_radio,absent_radio;
        CardView show;
        public TextView start,end,subject,submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.start=itemView.findViewById(R.id.start);
            this.end=itemView.findViewById(R.id.end);
            this.subject=itemView.findViewById(R.id.subject);
            this.submit=itemView.findViewById(R.id.submit);
            this.radioGroup = itemView.findViewById(R.id.radioGroup);
            this.present_radio = itemView.findViewById(R.id.present_radio);
            this.absent_radio = itemView.findViewById(R.id.absent_radio);
            this.show = itemView.findViewById(R.id.show);

        }

    }
}
