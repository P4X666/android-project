
package com.example.courseapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CourseData {

    @SerializedName("popularCourses")
    @Expose
    private List<PopularCourse> popularCourses = null;
    @SerializedName("courseForYou")
    @Expose
    private List<CourseForYou> courseForYou = null;

    public List<PopularCourse> getPopularCourses() {
        return popularCourses;
    }

    public void setPopularCourses(List<PopularCourse> popularCourses) {
        this.popularCourses = popularCourses;
    }

    public List<CourseForYou> getCourseForYou() {
        return courseForYou;
    }

    public void setCourseForYou(List<CourseForYou> courseForYou) {
        this.courseForYou = courseForYou;
    }

}
