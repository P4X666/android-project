package com.example.drysister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drysister.api.SisterApi;
import com.example.drysister.bean.Sister;
import com.example.drysister.helper.SisterLoader;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private       ImageView         showImg;
    private       int               curPos = 0;
    private       PictureLoader     loader;
    private       ExecutorService   executorService;
    private       ArrayList<Sister> data;
    private       SisterApi         sisterApi;
    private final int               pictureLimit = 5;
    TextView textView;
    private String TAG = "MainActivity sister";

    private SisterLoader mLoader;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new PictureLoader();
        textView = findViewById(R.id.pictureNum);
        executorService = Executors.newFixedThreadPool(1);
        mLoader = SisterLoader.getInstance(MainActivity.this);
        initData();
        initUI();
    }

    private void initData() {
        data = new ArrayList<>();
        sisterApi = new SisterApi();
        fetchSister();
    }

    private void initUI() {
        Button showBtn = findViewById(R.id.btn_show);
        showImg = findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_show){
            if(data.get(curPos) != null){
//                loader.load(showImg, data.get(curPos).getUrl());
                mLoader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);
            }
            curPos++;
            textView.setText("这是第" + curPos + "张图片");
            if (curPos >= data.size()) {
                curPos = 0;
            }
        }
    }
    private void updateData(ArrayList<Sister> sisters){
        Handler uiThread = new Handler(Looper.getMainLooper());
        uiThread.post(() -> {
            data.clear();
            data.addAll(sisters);
        });
    }
    private void fetchSister() {
        executorService.submit(() -> {
            ArrayList<Sister> lastSisters = new ArrayList<>();
            for (int i = 0; i < pictureLimit; i++) {
                Sister sister = sisterApi.fetchSister();
                if (sister != null) {
                    lastSisters.add(sister);
                }
            }
            Log.e(TAG, "fetchSister: " + lastSisters.size());
            updateData(lastSisters);
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}
