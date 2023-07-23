package com.example.courseapp.retrofit;


import com.example.courseapp.model.CourseData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("coursedata.json")
    Call<List<CourseData>> getAllCourses();

}
