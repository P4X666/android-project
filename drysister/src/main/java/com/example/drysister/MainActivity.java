package com.example.drysister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView         showImg;
    private ArrayList<String> urls;
    private int               curPos = 0;
    private PictureLoader     loader;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new PictureLoader();
        initData();
        initUI();
    }

    private void initData() {
        urls = new ArrayList<>();
        urls.add("https://img.paulzzh.com/touhou/yandere/image/825acad0ec0e86112628cfce826bd911.jpg");
        urls.add("https://img.paulzzh.com/touhou/yandere/jpeg/b60f02efb34eb59b0d9f67ae2de705a4.jpg");
        urls.add("https://img.paulzzh.com/touhou/yandere/image/d4b4a09a4183cc926f11c4ac50442478.jpg");
        urls.add("https://img.paulzzh.com/touhou/yandere/jpeg/a3ec214210b17213cebdfc15da9a8263.jpg");
        urls.add("https://img.paulzzh.com/touhou/yandere/jpeg/1026827e276769879093e92935f58632.jpg");
        urls.add("https://img.paulzzh.com/touhou/yandere/image/2c1887dca70a01dbc5d5ff8bb7e16950.jpg");
    }

    private void initUI() {
        Button showBtn = findViewById(R.id.btn_show);
        showImg = findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
//        初始化，第一页填充
        onClick(showBtn);
    }

    @Override public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_show){
            if (curPos > 5) {
                curPos = 0;
            }
            loader.load(showImg, urls.get(curPos));
            curPos++;
        }
    }
}
