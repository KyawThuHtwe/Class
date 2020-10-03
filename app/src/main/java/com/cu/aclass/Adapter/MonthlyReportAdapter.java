package com.cu.aclass.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ViewHolder> {
    ArrayList<SubjectData> subjectData;
    ArrayList<AttendanceData> attendanceData;
    int select_month;
    Context context;
    Bitmap bitmap;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.report_layout.setBackgroundColor(readReportColor());
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
                            int month=Integer.parseInt(resc.getString(1).split("/")[1]);
                            total=subjectData.get(position).getTotal();
                            if(resc.getString(3).equals("1")){
                                if(select_month==month){
                                    present_count++;
                                }
                            }else if(resc.getString(3).equals("0")){
                                if(select_month==month){
                                    absent_count++;
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
                    ans_present = (present_count / (total*4)) * 100.0;
                    ans_absent = (absent_count / (total*4)) * 100.0;
                }
            }
            /*
            String s_p=ans_present+"";
            String s_a=ans_absent+"";
            String prfs,abfs;

            if(s_p.length()>4 || s_a.length()>4){
                prfs=s_p.charAt(0)+""+s_p.charAt(1)+""+s_p.charAt(2)+""+s_p.charAt(3);
                abfs=s_a.charAt(0)+""+s_a.charAt(1)+""+s_a.charAt(2)+""+s_a.charAt(3);
            }else {
                prfs=s_p;
                abfs=s_a;
            }

             */

            int attendance=subjectData.get(position).getTotal()*4;
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
                    detail(v,select_month,holder.subject.getText()+"");
                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    String date=null,attendance=null;
    public void detail(View v, int select_month, String sub){
        @SuppressLint("InflateParams") View view = LayoutInflater.from(v.getContext()).inflate(R.layout.report_detail, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(v.getContext());
        dialog.setContentView(view);
        dialog.show();
        TextView day=view.findViewById(R.id.day);
        TextView subject=view.findViewById(R.id.subject);
        String month = null;
        switch (select_month){
            case 1:
                month="January";
                break;
            case 2:
                month="February";break;
            case 3:
                month="March";break;
            case 4:
                month="April";break;
            case 5:
                month="May";break;
            case 6:
                month="June";break;

            case 7:
                month="July";
                break;
            case 8:
                month="August";
                break;
            case 9:
                month="September";
                break;
            case 10:
                month="October";
                break;
            case 11:
                month="November";
                break;
            case 12:
                month="December";
                break;
        }
        day.setText(month);
        subject.setText(sub);
        attendanceData=new ArrayList<>();
        DatabaseHelper helper=new DatabaseHelper(context);
        try {
            Cursor resc = helper.getAttendance();
            if (resc != null && resc.getCount() > 0) {
                while (resc.moveToNext()) {
                    if(resc.getString(2).equals(sub)){
                        try {
                            int month_s=Integer.parseInt(resc.getString(1).split("/")[1]);
                            if(resc.getString(3).equals("1")){
                                if(select_month==month_s){
                                    date=resc.getString(1);
                                    attendance=resc.getString(3);
                                }
                            }else if(resc.getString(3).equals("0")){
                                if(select_month==month_s){
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
    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
    @Override
    public int getItemCount() {
        return subjectData.size();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int readReportColor(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("Color", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("ReportColor",context.getResources().getColor(R.color.colorDivider));
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView subject,record_text;
        public ProgressBar record_progress,progressBar_present,progressBar_absent;
        public TextView percent_present,percent_absent;
        public TextView attendance1,attendance2,attendance3,attendance4;
        public CardView cardView;
        public LinearLayout report_layout;

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
            this.report_layout=itemView.findViewById(R.id.report_layout);
        }
    }
}
