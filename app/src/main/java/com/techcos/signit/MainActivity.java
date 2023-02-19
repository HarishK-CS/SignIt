package com.techcos.signit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SignaturePad sign_pad;
    Button saveBtn,clearBtn;
    private static String filepath;
//    ImageView folder;
    File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/SignIt(TechCos)");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askPermission();
        sign_pad=findViewById(R.id.sign_pad);
        saveBtn=findViewById(R.id.saveBtn);
        clearBtn=findViewById(R.id.clearBtn);
//        folder = findViewById(R.id.folder);


        filepath = path.getAbsolutePath();
        if(!path.exists()){
            path.mkdirs();
        }
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_pad.clear();
            }
        });

//        folder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openFolder();
//            }
//        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = sign_pad.getSignatureBitmap();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmm");
                String fname = "SignIt - "+sdf.format(new Date())+".jpg";
                File file = new File(path,fname);
                if(file.exists())
                    file.delete();
                try{
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                    Toast.makeText(MainActivity.this,"Successfully Saved in Pictures/SignIt(TechCos)", Toast.LENGTH_SHORT).show();
                    out.flush();
                    out.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
                MediaScannerConnection.scanFile(MainActivity.this, new String[]{path.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });

            }
        });
    }

    private void askPermission() {
        Dexter.withContext(this).withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
//    public void openFolder(){
//        Intent intent = getPackageManager().getLaunchIntentForPackage("com.sec.android.app.myfiles");
//        if(intent!=null){
//            intent.setData(Uri.parse(path.toString()));
//            startActivity(intent);
//        }else {
//            Intent intent1 = getPackageManager().getLaunchIntentForPackage("com.android.documentsui");
//            intent1.setData(Uri.fromFile(path));
//            startActivity(intent1);
//        }
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
//            intent.setDataAndType(Uri.parse(path.toString()), DocumentsContract.Document.MIME_TYPE_DIR);
//        }else{
//            intent.setDataAndType(Uri.parse(path.toString()),"*/*");
//        }
//        if (intent.resolveActivityInfo(getPackageManager(),0)!= null){
//            startActivity(Intent.createChooser(intent,"Open Folder"));
//        }else{
//            Toast.makeText(MainActivity.this, "Folder Open Failed", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}