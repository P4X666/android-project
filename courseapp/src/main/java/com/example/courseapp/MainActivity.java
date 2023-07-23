package com.example.courseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.courseapp.adapter.PopularCourseAdapter;
import com.example.courseapp.model.CourseData;
import com.example.courseapp.model.PopularCourse;
import com.example.courseapp.retrofit.ApiInterface;
import com.example.courseapp.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = RetrofitClient.getRetrofitInstance().create(ApiInterface.class);

        Call<List<CourseData>> call = apiInterface.getAllCourses();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CourseData>> call, Response<List<CourseData>> response) {
                List<CourseData> courseDataList =  response.body();
                setPopularRecycler(courseDataList.get(0).getPopularCourses());
            }

            @Override
            public void onFailure(Call<List<CourseData>> call, Throwable t) {
                Log.e("onFailure", "onFailure: " + call);
            }
        });
    }

    private void setPopularRecycler(List<PopularCourse> popularCourseList){
        RecyclerView popularRecycler = findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        PopularCourseAdapter popularCourseAdapter = new PopularCourseAdapter(this, popularCourseList);
        popularRecycler.setAdapter(popularCourseAdapter);
    }
}