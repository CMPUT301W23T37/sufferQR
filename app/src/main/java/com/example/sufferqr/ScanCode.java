package com.example.sufferqr;

import static com.google.gson.internal.$Gson$Types.arrayOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.BitmapCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufferqr.databinding.ActivityScanCodeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class ScanCode extends DrawerBase {
    // https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
    // https://developers.google.com/ml-kit/vision/barcode-scanning/android

    ActivityScanCodeBinding activityScanCodeBinding;

    PreviewView previewView;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    TextView textView;

    String userName,QRstring;

    Button Go,Back;

    Boolean foundQR;

    /**
     * create view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityScanCodeBinding = ActivityScanCodeBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(activityScanCodeBinding.getRoot());
        allocateActivityTitle("Suffer QR");

        previewView = findViewById(R.id.scan_code_camera_view);
        textView = findViewById(R.id.scan_code_textView2);
        Go = findViewById(R.id.scan_code_go_button);
        Back = findViewById(R.id.scan_code_return_button);

        Intent myNewIntent = getIntent();
        userName = myNewIntent.getStringExtra("user");
        foundQR=false;

        // check if camera allowed
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindImageAnalysis(cameraProvider);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        textView.setText("please reenter to lunch camera");
                    }
                }
            }, ContextCompat.getMainExecutor(this));

        } else {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CAMERA}, 17300);
        }

        // if user cancel go to dashboard
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!foundQR){
                    Intent scanIntent = new Intent(ScanCode.this, DashBoard.class);
                    scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(scanIntent);
                    finish();
                }
            }
        });




    }

    /**
     * qr analysis
     */
    private void bindImageAnalysis(@NonNull ProcessCameraProvider cameraProvider) {
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(500,500))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                image.close();
            }
        });
        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {            }
        };
        orientationEventListener.enable();
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,
                imageAnalysis, preview);
        // check qrcode
        Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = previewView.getBitmap();
                if (!foundQR){
                    if(bitmap != null) {
                        InputImage image = InputImage.fromBitmap(bitmap, 0);
                        ImageFindQR(image,bitmap);
                    }
                } else {
                    Uri surrounds = saveImage(bitmap);
                    Intent scanIntent = new Intent(ScanCode.this, QRDetailActivity.class);
                    scanIntent.putExtra("user",userName);
                    scanIntent.putExtra("mode","new");
                    scanIntent.putExtra("QRString",QRstring);
                    scanIntent.putExtra("imageUri",surrounds.toString());
                    scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(scanIntent);
                    finish();
                }
            }
        });


    }


    /**
     * sync with ml to validate such qr exist
     */
    private void ImageFindQR(InputImage inputImage,Bitmap bitmap){
        // setup barcode dector
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        //step 4 lucnch analysis

        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        TextInputEditText ttv = findViewById(R.id.qr_detail_image_textfield);
                        // Task completed successfully
                        // ...
                        int Counts = 0;
                        String codes="";
                        for (Barcode barcode: barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
                            String rawValue = barcode.getRawValue();

                            codes=codes + rawValue;
                            Counts++;
                        }

                        if (Counts>=1){
//                          save umage in uri
                            QRstring = codes;
//                            new/modified/viewer
//                             remember change ScanCode.class
                            foundQR=true;
                            Go.setText("take picture of Surroundings");
                            textView.setText("if you do not want to take surrounds,you can change it at next page");

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        System.out.println(e);
                        Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }

    /**
     * create image file
     */
    private Uri saveImage(Bitmap bitmap) {
        // https://stackoverflow.com/questions/42460710/how-to-convert-a-bitmap-image-to-uri
        Uri image = null;
        File file = createImageFile();
        if (file != null) {
            FileOutputStream fout;
            try {
                fout = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, fout);
                fout.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            image = Uri.fromFile(file);
        }
        return image;
    }


    /**
     * save image file
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File mFileTemp = null;
        String root=this.getDir("my_sub_dir",Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if(!myDir.exists()){
            myDir.mkdirs();
        }
        try {
            mFileTemp=File.createTempFile(imageFileName,".jpg",myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mFileTemp;
    }


}