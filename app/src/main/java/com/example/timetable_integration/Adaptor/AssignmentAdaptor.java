package com.example.timetable_integration.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.Models.Assignment;
import com.example.timetable_integration.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssignmentAdaptor extends RecyclerView.Adapter<AssignmentAdaptor.myholder>{


    ArrayList<Assignment> assignment_list;
    Context mcontext;

    public AssignmentAdaptor(ArrayList<Assignment> assignment_list, Context mcontext) {
        this.assignment_list = assignment_list;
        this.mcontext = mcontext;
    }

    @NonNull
    @NotNull
    @Override
    public myholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list,parent,false);

        return new myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AssignmentAdaptor.myholder holder, int position) {

        Date assignment_date = assignment_list.get(position).getDeadline();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d");

        String time = sdf1.format(assignment_date).replace("PM","pm").replace("AM","am");
        String date = sdf2.format(assignment_date);




        holder.assignment_name.setText(assignment_list.get(position).getCode()+" "+assignment_list.get(position).getName());
        holder.assignment_type.setText(assignment_list.get(position).getTags());
        holder.assignment_platform.setText(assignment_list.get(position).getPlatform());
        holder.strip_colour.setBackgroundColor(Color.parseColor("#060D19"));

        holder.assignment_deadline.setText(date);
        holder.assignment_time.setText(time);

        holder.txt_assignment_date.setText(date);
    }

    @Override
    public int getItemCount() {
        return assignment_list.size();
    }

    public static class myholder extends RecyclerView.ViewHolder
    {
        TextView assignment_name,assignment_type,assignment_platform,assignment_time,assignment_deadline,txt_assignment_date;
        View strip_colour;

        public myholder(@NonNull @NotNull View itemView) {
            super(itemView);


            assignment_name = itemView.findViewById(R.id.course_name);
            assignment_type = itemView.findViewById(R.id.course_type);
            assignment_platform = itemView.findViewById(R.id.course_place);
            assignment_time = itemView.findViewById(R.id.course_time);
            assignment_deadline = itemView.findViewById(R.id.course_duration);
            txt_assignment_date = itemView.findViewById(R.id.txt_Time);
            strip_colour = itemView.findViewById(R.id.course_colour);

        }
    }


}
