package com.example.drysister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.MediaRouteButton;
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
import com.example.drysister.db.SisterDBHelper;
import com.example.drysister.helper.SisterLoader;
import com.example.drysister.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private       ImageView         showImg;
    private       int               curPos = 0;
    private       ExecutorService   executorService;
    private       ArrayList<Sister> data;
    private       SisterApi         sisterApi;
    private final int               pictureLimit = 5;
    TextView textView;
    private String TAG = "MainActivity sister";

    private SisterLoader   mLoader;
    private SisterDBHelper mDbHelper;
    Button previousBtn;
    Button nextBtn;

    int page = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newFixedThreadPool(1);
        mLoader = SisterLoader.getInstance(MainActivity.this);
        mDbHelper = SisterDBHelper.getInstance();
        initData();
        initUI();
    }

    private void initData() {
        data = new ArrayList<>();
        sisterApi = new SisterApi();
        fetchSister();
    }

    private void initUI() {
        previousBtn = findViewById(R.id.btn_previous);
        nextBtn = findViewById(R.id.btn_next);
        showImg = findViewById(R.id.img_show);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        textView = findViewById(R.id.pictureNum);

        boolean isNetworkAlive = NetworkUtils.isAvailable(this);
        if(!isNetworkAlive){
            textView.setText("当前没有网络");
        }
    }

    @Override public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_next){
            if(!data.isEmpty() && data.get(curPos) != null){
                mLoader.bindBitmap(data.get(curPos).getUrl(),showImg,400,400);
            }
            curPos++;
            textView.setText("这是第" + curPos + "张图片");
            if (curPos >= data.size()) {
                curPos = 0;
            }
        }
        if(id == R.id.btn_previous){
            if (curPos == 0) {
                previousBtn.setVisibility(View.INVISIBLE);
            }
            --curPos;
            if (curPos == data.size() - 1) {
                fetchSister();
            } else if(curPos < data.size()) {
                mLoader.bindBitmap(data.get(curPos).getUrl(), showImg, 400, 400);
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
            Log.v("MainActivity",mDbHelper.getSistersCount() + "");

            if(mDbHelper.getSistersCount() < pictureLimit) {
                for (int i = 0; i < pictureLimit; i++) {
                    Sister sister = sisterApi.fetchSister();
                    if (sister != null) {
                        lastSisters.add(sister);
                    }
                }
                mDbHelper.insertSisters(lastSisters);
            }else{
                lastSisters.addAll(mDbHelper.getSistersLimit(page,pictureLimit));
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
