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
    static DBHelper dbHelper;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
    RecyclerAdapter codeAdapter;
    RecyclerView recyclerView;
    private CodeScanner mCodeScanner;
    private Activity activity;
    private SlideUp slideUp;
    private View dim;
    private View sliderView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        dbHelper = new DBHelper(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        loadApp();
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

    public void loadApp() {
        setContentView(R.layout.activity_main);


        sliderView = findViewById(R.id.slideView);

        dim = findViewById(R.id.dim);
        fab = findViewById(R.id.fab);

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

    @Override
    protected void onResume() {
        super.onResume();
        CodeScannerView scannerView = findViewById(R.id.codeScannerView);
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

        recyclerView = findViewById(R.id.code_recycler_view);
//        ArrayList<Code> data = new ArrayList<>();

//        for(int i=0; i<100;i++){
//            data.add(new Code(String.valueOf(i),"Code "+i));
//        }

        codeAdapter = new RecyclerAdapter(dbHelper.getAllCodes(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(codeAdapter);

        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {

        mCodeScanner.releaseResources();
        super.onPause();
    }


}

