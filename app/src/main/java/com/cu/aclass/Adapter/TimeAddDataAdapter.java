package com.cu.aclass.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.MainActivity;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.Model.TimeAddData;
import com.cu.aclass.R;
import com.cu.aclass.TimeTable.AddTimeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TimeAddDataAdapter extends RecyclerView.Adapter<TimeAddDataAdapter.ViewHolder> {
    Context context;
    ArrayList<TimeAddData> timeAddData;
    boolean state1 = false;
    boolean state2 = false;
    boolean state3 = false;

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
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                present(v,timeAddData.get(position).getId(),timeAddData.get(position).getSubject(),timeAddData.get(position).getDay());
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
        final Handler handler = new Handler();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    if (state1) {
                        holder.ani1.setImageResource(R.drawable.ic_hourglass_full);
                        state1 = false;
                    } else {
                        holder.ani1.setImageResource(R.drawable.ic_hourglass_empty);
                        state1 = true;
                    }
                    holder.ani1.setAnimation(animation);
                    handler.postDelayed(this, 2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    if (state2) {
                        holder.ani2.setImageResource(R.drawable.ic_hourglass_full);
                        state2 = false;
                    } else {
                        holder.ani2.setImageResource(R.drawable.ic_hourglass_empty);
                        state2 = true;
                    }
                    holder.ani2.setAnimation(animation);
                    handler.postDelayed(this, 3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                try {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    if (state3) {
                        holder.ani3.setImageResource(R.drawable.ic_hourglass_full);
                        state3 = false;
                    } else {
                        holder.ani3.setImageResource(R.drawable.ic_hourglass_empty);
                        state3 = true;
                    }
                    holder.ani3.setAnimation(animation);
                    handler.postDelayed(this, 4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.post(runnable1);
        handler.post(runnable2);
        handler.post(runnable3);
    }

    private void present(View v, final String id, final String subject, final String day) {
        try {
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.attendance_submit, null);
            final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
            dialog.setContentView(view);
            dialog.show();
            Button present = view.findViewById(R.id.present);
            present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePicker datePicker = new DatePicker(context);
                    DatabaseHelper helper = new DatabaseHelper(context);
                    boolean result = helper.insertAttendance(datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear(), subject + "", "1",day+"",id+"");
                    if (result) {
                        Toast.makeText(v.getContext(),"Present",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                    }

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
        ImageView close=view.findViewById(R.id.close);
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
        LinearLayout edit=view.findViewById(R.id.edit);
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
        LinearLayout delete=view.findViewById(R.id.delete);
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
        ImageView ani1, ani2, ani3;
        CardView show;
        public TextView start,end,subject,submit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.start=itemView.findViewById(R.id.start);
            this.end=itemView.findViewById(R.id.end);
            this.subject=itemView.findViewById(R.id.subject);
            this.submit=itemView.findViewById(R.id.submit);
            this.ani1 = itemView.findViewById(R.id.ani1);
            this.ani2 = itemView.findViewById(R.id.ani2);
            this.ani3 = itemView.findViewById(R.id.ani3);
            this.show = itemView.findViewById(R.id.show);

        }

    }
}
