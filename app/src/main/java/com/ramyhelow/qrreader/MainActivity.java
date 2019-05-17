package com.ramyhelow.qrreader;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mancj.slideup.SlideUp;
import com.mancj.slideup.SlideUpBuilder;
import com.ramyhelow.qrreader.Adapter.RecyclerAdapter;
import com.ramyhelow.qrreader.Database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static DBHelper dbHelper;
    private SimpleDateFormat sdf;
    private RecyclerAdapter codeAdapter;
    private RecyclerView recyclerView;
    private CodeScanner mCodeScanner;
    private Activity activity;
    private SlideUp slideUp;
    private View dim;
    private View sliderView;
    private FloatingActionButton fab;
    private CodeScannerView scannerView;

    private void initObjects(){

        sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        activity = this;
        dbHelper = new DBHelper(this);

        recyclerView = findViewById(R.id.code_recycler_view);
        codeAdapter = new RecyclerAdapter(dbHelper.getAllCodes(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(codeAdapter);

        sliderView = findViewById(R.id.slideView);
        dim = findViewById(R.id.dim);
        fab = findViewById(R.id.fab);
        scannerView = findViewById(R.id.codeScannerView);

        slideUp = new SlideUpBuilder(sliderView)
                .withListeners(new SlideUp.Listener.Events() {
                    @Override
                    public void onSlide(float percent) {
                        dim.setAlpha(1 - (percent / 100));
                        if (fab.isShown() && percent < 100) {
                            fab.hide();
                        }
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {
                        if (visibility == View.GONE) {
                            fab.show();
                        }
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(true)
                .withGesturesEnabled(true)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(findViewById(R.id.codeScannerView))
                .build();
    }

    private void setupScanner(){
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCodeScanner.releaseResources();
                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        dbHelper = new DBHelper(getApplicationContext());
                        dbHelper.insertNewCode(sdf.format(new Date()), result.getText());
                        codeAdapter = new RecyclerAdapter(dbHelper.getAllCodes(), MainActivity.this);
                        recyclerView.setAdapter(codeAdapter);
                        mCodeScanner.startPreview();

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        mCodeScanner.startPreview();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initObjects();



        //request permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        setupScanner();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner!=null)
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        if(mCodeScanner!=null)
            mCodeScanner.releaseResources();
        super.onPause();
    }


}

