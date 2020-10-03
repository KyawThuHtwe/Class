package com.cu.aclass.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.Model.AddDay;
import com.cu.aclass.R;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.RequiresApi;

public class DayAdapter extends ArrayAdapter<AddDay> {
    private Context mContext;
    private ArrayList<AddDay> addDayArrayList;

    public DayAdapter(Context context, int resource, ArrayList<AddDay> addDayArrayList) {
        super(context, resource, addDayArrayList);
        this.mContext = context;
        this.addDayArrayList =addDayArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View getCustomView(final int position, View convertView,
                              final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(addDayArrayList.get(position).getDay());

        if ((position==0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
            holder.mCheckBox.setChecked(true);
        }else{
            try {
                holder.mCheckBox.setVisibility(View.VISIBLE);
                String day = addDayArrayList.get(position).getDay();
                if (day.equals(getDay(day))) {
                    if (getCheck(day) == 1) {
                        holder.mCheckBox.setChecked(true);
                    }
                } else {
                    holder.mCheckBox.setChecked(false);
                    addDay(addDayArrayList.get(position).getDay(), 0);
                }
            }catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(position>0){
                    if(isChecked){
                        holder.mCheckBox.setChecked(true);
                        addDay(addDayArrayList.get(position).getDay(),1);
                    }else {
                        holder.mCheckBox.setChecked(false);
                        addDay(addDayArrayList.get(position).getDay(),0);
                    }
                }
            }
        });

        return convertView;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void addDay(String day, int res){
        SharedPreferences sharedPreferences= Objects.requireNonNull(getContext()).getSharedPreferences("addDay", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(day+"day",day);
        editor.putInt(day+"check",res).apply();
    }
    public String getDay(String day){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("addDay", Context.MODE_PRIVATE);
        return sharedPreferences.getString(day+"day","day");
    }
    public int getCheck(String day){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("addDay", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(day+"check",0);
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}

