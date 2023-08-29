package com.example.drysister.api;

import static android.os.PersistableBundle.readFromStream;

import android.util.Log;

import com.example.drysister.bean.Sister;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SisterApi {
    private static final String TAG = "Network";
    private static final String BASE_URL = "https://img.paulzzh.com/touhou/random?type=json";
    /**
     * 查询妹子信息
     */
    public Sister fetchSister() {
        String fetchUrl = BASE_URL;
        Sister sister = null;
        try {
            URL url = new URL(fetchUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            Log.v(TAG, "Server response：" + code);
            if (code == 200) {
                InputStream in = conn.getInputStream();
                byte[] data = readFromStream(in);
                String result = new String(data, StandardCharsets.UTF_8);
                sister = parseSister(result);
            } else {
                Log.e(TAG,"请求失败：" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sister;
    }
    /**
     * 解析返回Json数据的方法
     */
    public Sister parseSister(String content) throws Exception {
        Sister sister = null;
        JSONObject object = new JSONObject(content);
        sister = new Sister();
        sister.setId(object.getInt("id"));
        sister.setWidth(object.getInt("width"));
        sister.setHeight(object.getInt("height"));
        sister.setSize(object.getInt("size"));
        sister.setUrl(object.getString("url"));
        sister.setPreview(object.getString("preview"));
        return sister;
    }

    /**
     * 读取流中数据的方法
     */
    public byte[] readFromStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len ;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }
}
