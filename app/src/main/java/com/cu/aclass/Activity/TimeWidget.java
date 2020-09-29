package com.cu.aclass.Activity;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.R;

import java.util.Calendar;

import androidx.annotation.RequiresApi;

/**
 * Implementation of App Widget functionality.
 */
public class TimeWidget extends AppWidgetProvider {

    private static final String COUNT_KEY = "count";

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        // Get the count from prefs.
        SharedPreferences prefs =
                context.getSharedPreferences(context.getPackageName(), 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case 1:
                dataLoad(context,views,"Sunday");
                break;
            case 2:
                dataLoad(context, views, "Monday");
                break;
            case 3:
                dataLoad(context, views, "Tuesday");
                break;
            case 4:
                dataLoad(context, views, "Wednesday");
                break;
            case 5:
                dataLoad(context, views, "Thursday");
                break;
            case 6:
                dataLoad(context, views, "Friday");
                break;
            case 7:
                dataLoad(context, views, "Saturday");
                break;
        }


       // RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.time_widget);
        //views.setTextViewText(R.id.count,count+"");
        // Save count back to prefs.
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();

        Intent intent = new Intent(context, TimeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int ids[] = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, TimeWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        context.sendBroadcast(intent);
        // Instruct the widget manager to update the widget.
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    public void dataLoad(Context context, RemoteViews views, String day){
        try {
            DatabaseHelper helper=new DatabaseHelper(context);
            Cursor res = helper.getAllTime();
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    if(res.getString(9).equals(day)) {
                        if (!res.getString(3).equals("Subject")) {
                            String start,end;
                            //current time
                            TimePicker timePicker=new TimePicker(context);
                            int cur_hr= timePicker.getCurrentHour();
                            int cur_min=timePicker.getCurrentMinute();

                            start=res.getString(1)+"";
                            end=res.getString(2)+"";
                            //start time
                            int start_hr=Integer.parseInt(start.split(":")[0]);
                            int start_min=Integer.parseInt(start.split(":")[1]);

                            //end time
                            int end_hr=Integer.parseInt(end.split(":")[0]);
                            int end_min=Integer.parseInt(end.split(":")[1]);

                            if(cur_hr>=start_hr && cur_hr<=end_hr){
                                if(cur_min>=start_min && cur_min<=end_min){
                                    views.setTextViewText(R.id.start,res.getString(1));
                                    views.setTextViewText(R.id.end, res.getString(2));
                                    views.setTextViewText(R.id.subject, res.getString(3));
                                }
                            }else {
                                views.setTextViewText(R.id.start,context.getResources().getString(R.string._00_00));
                                views.setTextViewText(R.id.end,context.getResources().getString(R.string._00_00));
                                views.setTextViewText(R.id.subject,context.getResources().getString(R.string.subject));
                            }

                        }
                    }
                }
            }
            helper.close();
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

