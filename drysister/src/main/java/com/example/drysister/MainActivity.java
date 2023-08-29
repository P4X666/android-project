package com.example.drysister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.drysister.api.SisterApi;
import com.example.drysister.bean.Sister;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView         showImg;
    private int               curPos = 0;
    private PictureLoader     loader;
    private Thread sisterThread;

    private ArrayList<Sister> data;
    private SisterApi sisterApi;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new PictureLoader();
        initData();
        initUI();
    }

    private void initData() {
        data = new ArrayList<>();
        sisterApi = new SisterApi();
        for (int i = 0; i < 10; i++) {
            fetchSister();
        }
    }

    private void initUI() {
        Button showBtn = findViewById(R.id.btn_show);
        showImg = findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_show){
            if (curPos > 10) {
                curPos = 0;
            }
            if(!data.isEmpty() && data.size() > curPos){
                loader.load(showImg, data.get(curPos).getUrl());
                curPos++;
            }
        }
    }
    private void updateData(Sister sister){
        data.add(sister);
    }
    private void fetchSister() {
        sisterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sister sister = sisterApi.fetchSister();
                if (sister != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateData(sister);
                        }
                    });
                }
            }
        });
        sisterThread.start();
    }

    private void cancelFetchSister() {
        if (sisterThread != null) {
            sisterThread.interrupt();
            sisterThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelFetchSister();
    }
}
