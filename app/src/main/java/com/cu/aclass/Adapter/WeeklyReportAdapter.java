package com.cu.aclass.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.aclass.DatabaseHelper.DatabaseHelper;
import com.cu.aclass.MainActivity;
import com.cu.aclass.Model.AttendanceData;
import com.cu.aclass.Model.SubjectData;
import com.cu.aclass.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyReportAdapter extends RecyclerView.Adapter<WeeklyReportAdapter.ViewHolder> {
    ArrayList<SubjectData> subjectData;
    ArrayList<AttendanceData> attendanceData;
    int select_week;
    Context context;
    Bitmap bitmap;

    public WeeklyReportAdapter(Context context, ArrayList<SubjectData> subjectData, int select_week) {
        this.subjectData=subjectData;
        this.select_week=select_week;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.cutom_report,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.subject.setText(subjectData.get(position).getSubject());
        double total=0;
        int present_count=0;
        int absent_count=0;
        DatabaseHelper helper=new DatabaseHelper(context);
        try {
            Cursor resc = helper.getAttendance();
            if (resc != null && resc.getCount() > 0) {
                while (resc.moveToNext()) {
                    if(resc.getString(2).equals(subjectData.get(position).getSubject())){
                        try {
                            int day=Integer.parseInt(resc.getString(1).split("/")[0]);
                            total=subjectData.get(position).getTotal();
                            if(resc.getString(3).equals("1")){
                                if(select_week==0){
                                    if(day<8){
                                        present_count++;
                                    }
                                }else if(select_week==1){
                                    if(day>7 && day<15){
                                        present_count++;
                                    }
                                }else if(select_week==2){
                                    if(day>14 && day<22){
                                        present_count++;
                                    }
                                }else if(select_week==3){
                                    if(day>21){
                                        present_count++;
                                    }
                                }
                            } else if(resc.getString(3).equals("0")){
                                if(select_week==0){
                                    if(day<8){
                                        absent_count++;
                                    }
                                }else if(select_week==1){
                                    if(day>7 && day<15){
                                        absent_count++;
                                    }
                                }else if(select_week==2){
                                    if(day>14 && day<22){
                                        absent_count++;
                                    }
                                }else if(select_week==3){
                                    if(day>21){
                                        absent_count++;
                                    }
                                }
                            }
                        }catch (NumberFormatException nfe){
                            Toast.makeText(context, nfe.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            double ans_present = 0, ans_absent = 0;
            if(total>0){
                if (present_count >0 || absent_count >0) {
                    ans_present = (present_count/total)*100;
                    ans_absent = (absent_count/total)*100;
                }
            }

            int attendance=subjectData.get(position).getTotal();
            holder.record_text.setText(ans_present+"");
            holder.record_progress.setProgress((int) ans_present);

            holder.percent_present.setText(ans_present+"");
            holder.percent_absent.setText(ans_absent+"");

            holder.progressBar_present.setProgress((int) ans_present);
            holder.progressBar_absent.setProgress((int) ans_absent);

            holder.attendance1.setText(attendance+"");
            holder.attendance2.setText(present_count+"");
            holder.attendance3.setText(absent_count+"");
            int left=attendance-(present_count+absent_count);
            holder.attendance4.setText(left+"");
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail(v,select_week,holder.subject.getText()+"");
                }
            });

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    String date=null,attendance=null;
    public void detail(View v, int select_week, String sub){
        @SuppressLint("InflateParams") final View view = LayoutInflater.from(v.getContext()).inflate(R.layout.report_detail, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        dialog.setContentView(view);
        dialog.show();
        TextView day=view.findViewById(R.id.day);
        TextView subject=view.findViewById(R.id.subject);
        String week = null;
        if(select_week==0){
            week="1st week";
        }else if(select_week==1){
            week="2nd week";
        }else if(select_week==2){
            week="3rd week";
        }else if(select_week==3){
            week="4th week";
        }
        day.setText(week);
        subject.setText(sub);
        attendanceData=new ArrayList<>();
        DatabaseHelper helper=new DatabaseHelper(context);
        try {
            Cursor resc = helper.getAttendance();
            if (resc != null && resc.getCount() > 0) {
                while (resc.moveToNext()) {
                    if(resc.getString(2).equals(sub)){
                        try {
                            int day_s=Integer.parseInt(resc.getString(1).split("/")[0]);
                            if(select_week==0){
                                if(day_s<8){
                                    date=resc.getString(1);
                                    attendance=resc.getString(3);
                                }
                            }else if(select_week==1){
                                if(day_s>7 && day_s<15){
                                    date=resc.getString(1);
                                    attendance=resc.getString(3);
                                }
                            }else if(select_week==2){
                                if(day_s>14 && day_s<22){
                                    date=resc.getString(1);
                                    attendance=resc.getString(3);
                                }
                            }else if(select_week==3){
                                if(day_s>21){
                                    date=resc.getString(1);
                                    attendance=resc.getString(3);
                                }
                            }
                            if(date!=null && attendance!=null) {
                                attendanceData.add(new AttendanceData("", date + "", "", attendance + "", "", ""));
                            }
                        }catch (NumberFormatException nfe){
                            Toast.makeText(context, nfe.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
            if(attendanceData.size()==0){
                for(int i=0;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }

            }else if(attendanceData.size()==1){
                for(int i=1;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==2){
                for(int i=2;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==3){
                for(int i=3;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==4){
                for(int i=4;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==5){
                for(int i=5;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==6){
                for(int i=6;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==7){
                for(int i=7;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }else if(attendanceData.size()==8){
                for(int i=8;i<10;i++){
                    attendanceData.add(new AttendanceData("0","","","","",""));
                }
            }
            RecyclerView recyclerView=view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
            recyclerView.setAdapter(new DetailAdapter(attendanceData,context));

            //save data
            String[] save_as={"Image","Pdf"};
            ArrayAdapter arrayAdapter=new ArrayAdapter<>(v.getContext(),android.R.layout.simple_spinner_dropdown_item,save_as);
            final Spinner spinner=view.findViewById(R.id.spinner);
            spinner.setAdapter(arrayAdapter);

            TextView save_data=view.findViewById(R.id.save);
            final LinearLayout linearLayout=view.findViewById(R.id.layout);
            save_data.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if(spinner.getSelectedItemPosition()==0){
                        if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(v.getContext(),"Please grant the permission to write external storage",Toast.LENGTH_SHORT).show();
                            requestPermission();
                        }else {
                            File file = null;    //which view you want to pass that view as parameter
                            try {
                                file = saveBitMap(v.getContext(), linearLayout);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file != null) {
                                Toast.makeText(v.getContext(), "Drawing saved to the gallery!"+file,Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(v.getContext(), "Oops! Image could not be saved.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else if(spinner.getSelectedItemPosition()==1){
                        if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(v.getContext(),"Please grant the permission to write external storage",Toast.LENGTH_SHORT).show();
                            requestPermission();
                        }else {
                            bitmap = loadBitmapFromView(linearLayout, linearLayout.getWidth(), linearLayout.getHeight());
                            try {
                                createPdf(v.getContext(), linearLayout);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(Context context, View view) throws IOException{
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        File pdfFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), context.getResources().getString(R.string.app_name));
        if (!pdfFileDir.exists()) {
            boolean isDirectoryCreated = pdfFileDir.mkdirs();
            if(!isDirectoryCreated)
                Toast.makeText(context, "Can't create directory to save the pdf",Toast.LENGTH_SHORT).show();
        }
        TextView img_title=view.findViewById(R.id.day);
        TextView img_sub=view.findViewById(R.id.subject);
        String img=img_sub.getText()+"_"+img_title.getText()+"";
        String filename = pdfFileDir.getPath() + File.separator+img+".pdf";
        File filePath = new File(filename);
        try {
            filePath.createNewFile();
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(context, filePath+"", Toast.LENGTH_SHORT).show();

    }

    private File saveBitMap(Context context, View drawView) throws IOException {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),context.getResources().getString(R.string.app_name));
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        TextView img_title=drawView.findViewById(R.id.day);
        TextView img_sub=drawView.findViewById(R.id.subject);
        String img=img_sub.getText()+"_"+img_title.getText()+"";
        String filename = pictureFileDir.getPath() + File.separator+ img +".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
    }
    @Override
    public int getItemCount() {
        return subjectData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subject,record_text;
        public ProgressBar record_progress,progressBar_present,progressBar_absent;
        public TextView percent_present,percent_absent;
        public TextView attendance1,attendance2,attendance3,attendance4;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.subject = itemView.findViewById(R.id.subject);
            this.record_text=itemView.findViewById(R.id.record_text);
            this.record_progress=itemView.findViewById(R.id.record_progress);
            this.progressBar_present = itemView.findViewById(R.id.present_circle_progress_bar);
            this.progressBar_absent = itemView.findViewById(R.id.absent_circle_progress_bar);
            this.percent_present = itemView.findViewById(R.id.percent_present);
            this.percent_absent = itemView.findViewById(R.id.percent_absent);
            this.attendance1 = itemView.findViewById(R.id.attendance1);
            this.attendance2 = itemView.findViewById(R.id.attendance2);
            this.attendance3 = itemView.findViewById(R.id.attendance3);
            this.attendance4 = itemView.findViewById(R.id.attendance4);
            this.cardView=itemView.findViewById(R.id.cardView);
        }
    }
}
