package com.example.sufferqr;


import static android.app.PendingIntent.getActivity;
import static android.content.ContentValues.TAG;
import static java.lang.Long.toHexString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sufferqr.databinding.ActivityScanCodeBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * reconize of qr code taken,and a photo of sorrounds and send to qrdetailactivity
 */
public class ScanCode extends DrawerBase {
    // https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
    // https://developers.google.com/ml-kit/vision/barcode-scanning/android

    ActivityScanCodeBinding activityScanCodeBinding;

    PreviewView previewView;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    TextView textView;

    String userName,QRstring;

    Button Go,Back,other;

    Boolean foundQR;

    ProgressDialog progressDialog;

    RelativeLayout relativeLayout;
    CircularProgressIndicator loading;
    CardView cardView;

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
        other = findViewById(R.id.scan_code_lib_button);
        relativeLayout = findViewById(R.id.activity_scan_code_progress_layout);
        loading = findViewById(R.id.activity_scan_code_progress);
        cardView = findViewById(R.id.scan_code_camera_card);

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
        Back.setOnClickListener(v -> {
                Intent scanIntent = new Intent(ScanCode.this, DashBoard.class);
                scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(scanIntent);
                finish();

        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), 101);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // storage/emulated/0/Pictures
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    Uri uri = Objects.requireNonNull(data).getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        if (!foundQR){
                            InputImage image = InputImage.fromBitmap(bitmap, 0);
                            ImageFindQR(image,bitmap);
                        } else {
                            calculation(bitmap);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
            }
        }
    }

    private void calculation(Bitmap bitmapImage){

        relativeLayout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.INVISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Uri surrounds = saveImage(bitmapImage);
        String hashed = "";
        String face="";
        int points = 0;
        try {
            hashed =  QRHash.toHexString(QRHash.getSHA(QRstring));
            EmojiDraw emojiDraw = new EmojiDraw(hashed);
            face = emojiDraw.draw(); //call to draw the visual respresentation

        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        } catch (Exception e){
            System.out.println(e);
        }

        ScoreCounter scoreCounter = new ScoreCounter(hashed);
        points = scoreCounter.calculateScore();

        Bundle bundle = new Bundle();
        bundle.putString("QRhash",hashed);
        bundle.putString("QRVisual",face);
        bundle.putString("points",String.valueOf(points));
        bundle.putString("user",userName);
        bundle.putString("QRpath",surrounds.toString());

        Intent scanIntent = new Intent(ScanCode.this, QRDetailActivity.class);
        scanIntent.putExtra("user",userName);
        scanIntent.putExtra("mode","new");
        scanIntent.putExtra("data",bundle);
        scanIntent.putExtra("imageUri",surrounds.toString());
        scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(scanIntent);
        finish();
    }

    private void validation(ArrayList<String> codes){
        ArrayList<String> hashcode = new ArrayList<>();
        // forum arraylist of hash
        for (int i=0;i<codes.size();i++){
            try {
                hashcode.add(QRHash.toHexString(QRHash.getSHA(codes.get(i))));
            } catch (Exception e) {
                hashcode.add("error");
            }
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("GameQrCode");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Scan for same qrcode and remove them
                    for (DocumentSnapshot document : task.getResult()) {
                        String hashed = document.getString("QRhash");
                        String thisUser = document.getString("user");
                        if (hashcode.contains(hashed) && Objects.equals(thisUser, userName)){
                            System.out.println("something removed");
                            int index =  hashcode.indexOf(hashed);
                            codes.remove(index);
                            hashcode.remove(index);
                        }
                    }

                    if (hashcode.size()==0){
                        Toast.makeText(getApplicationContext(),"no unique QRcode under your account",Toast.LENGTH_SHORT).show();
                    } else if (hashcode.size()==1) {
                        QRstring = codes.get(0);
                        foundQR=true;
                        Go.setText("take picture");
                        textView.setText("take a picture of surrounds");
                    } else {
                        System.out.println("more than one");
                        CharSequence[] cs = codes.toArray(new CharSequence[codes.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                        builder.setTitle("select your QRcode")
                                .setCancelable(false)
                                .setItems(cs, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        QRstring = codes.get(which);
                                        foundQR=true;
                                        Go.setText("take picture");
                                        textView.setText("take a picture of surrounds");
                                    }
                                });

                        builder.create();
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
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
                    calculation(bitmap);
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
                        int Counts = 0;
                        ArrayList<String> qrcodes=new ArrayList<>();
                        for (Barcode barcode: barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
                            String rawValue = barcode.getRawValue();

                            qrcodes.add(rawValue);
                        }
                        if (qrcodes.size()>=1){
                            validation(qrcodes);
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

    /**
     * check permission status
     * @param requestCode The request code passed in {@link (
     * android.app.Activity, String[], int)}
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 17300:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Intent scanIntent = new Intent(ScanCode.this, DashBoard.class);
                    scanIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(scanIntent);
                    finish();


                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

}
