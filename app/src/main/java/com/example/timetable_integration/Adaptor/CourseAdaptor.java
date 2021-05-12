package com.example.timetable_integration.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.Models.Course;
import com.example.timetable_integration.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CourseAdaptor extends RecyclerView.Adapter<CourseAdaptor.myViewHolder>{

    ArrayList<Course> course_list;
    Context mcontext;

    public CourseAdaptor(ArrayList<Course> course_list, Context mcontext) {
        this.course_list = course_list;
        this.mcontext = mcontext;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list,parent,false);


        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CourseAdaptor.myViewHolder holder, int position) {

        holder.course_name.setText(course_list.get(position).getCourse_name());
        holder.course_type.setText(course_list.get(position).getCourse_type());
        holder.course_place.setText(course_list.get(position).getCourse_place());
        holder.course_time.setText(course_list.get(position).getCourse_time());
        holder.course_duration.setText(course_list.get(position).getCourse_duration());

    }

    @Override
    public int getItemCount() {
        return course_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView course_name,course_type,course_place,course_time,course_duration;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
            course_type = itemView.findViewById(R.id.course_type);
            course_place = itemView.findViewById(R.id.course_place);
            course_time = itemView.findViewById(R.id.course_time);
            course_duration = itemView.findViewById(R.id.course_duration);

        }
    }

}
