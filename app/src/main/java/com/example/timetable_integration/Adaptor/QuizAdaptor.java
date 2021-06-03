package com.example.timetable_integration.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.Models.Quiz;
import com.example.timetable_integration.R;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuizAdaptor extends RecyclerView.Adapter<QuizAdaptor.myViewholder>{

    ArrayList<Quiz> quiz_list;
    Context mcontext;

    public QuizAdaptor(ArrayList<Quiz> quiz_list, Context mcontext) {
        this.quiz_list = quiz_list;
        this.mcontext = mcontext;
    }

    @NonNull
    @NotNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list,parent,false);


        return new myViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QuizAdaptor.myViewholder holder, int position) {

        Date quiz_date_time = quiz_list.get(position).getTime();


        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d");

        String time = sdf1.format(quiz_date_time).replace("PM","pm").replace("AM","am");
        String date = sdf2.format(quiz_date_time);


        //String time = DateFormat.format("h:mm a",cal).toString();
        //String date = DateFormat.format("MMM d",cal).toString();

        holder.quiz_time.setText(time);
        holder.txt_quiz_date.setText(date);
        holder.quiz_name.setText(quiz_list.get(position).getCode()+ " " +quiz_list.get(position).getName());
        holder.quiz_duration.setText(quiz_list.get(position).getDuration());
        holder.quiz_platform.setText(quiz_list.get(position).getPlatform());
        holder.quiz_type.setText(quiz_list.get(position).getTags());

        holder.strip_colour.setBackgroundColor(Color.parseColor("#FFC907"));

    }

    @Override
    public int getItemCount() {
        return quiz_list.size();
    }

    public static class myViewholder extends RecyclerView.ViewHolder
    {
        TextView quiz_name,quiz_type,quiz_platform,quiz_time,quiz_duration,txt_quiz_date;
        View strip_colour;

        public myViewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            quiz_name = itemView.findViewById(R.id.course_name);
            quiz_type = itemView.findViewById(R.id.course_type);
            quiz_platform = itemView.findViewById(R.id.course_place);
            quiz_time = itemView.findViewById(R.id.course_time);
            quiz_duration = itemView.findViewById(R.id.course_duration);
            txt_quiz_date = itemView.findViewById(R.id.txt_Time);
            strip_colour = itemView.findViewById(R.id.course_colour);

        }
    }

}
