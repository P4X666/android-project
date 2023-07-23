package com.example.courseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.courseapp.R;
import com.example.courseapp.model.PopularCourse;

import java.util.List;

public class PopularCourseAdapter extends RecyclerView.Adapter<PopularCourseAdapter.PopularViewHolder> {
    Context             context;
    List<PopularCourse> popularCourseList;

    public PopularCourseAdapter(Context context, List<PopularCourse> popularCourseList) {
        this.context = context;
        this.popularCourseList = popularCourseList;
    }
    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_row_item, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        holder.courseName.setText(popularCourseList.get(position).getName());
        holder.totalLessons.setText(popularCourseList.get(position).getName());

        Glide.with(context).load(popularCourseList.get(position).getImageUrl()).into(holder.courseImage);

//        holder.itemView.setOnClickListener((View view)->{
//            Intent i = new Intent(context, CourseDetail.class);
//
//            Pair[] pairs = new Pair[1];
//            pairs[0] = new Pair<View, String>(holder.courseImage, "image");
//            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
//
//            context.startActivity(i, activityOptions.toBundle());
//        });
    }


    @Override
    public int getItemCount() {
        return popularCourseList.size();
    }

    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseName, totalLessons;
        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);

            courseImage = itemView.findViewById(R.id.course_image);
            courseName = itemView.findViewById(R.id.lesson_name);
            totalLessons = itemView.findViewById(R.id.total_lesson);
        }
    }
}
