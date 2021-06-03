package com.example.timetable_integration.Adaptor;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.Models.Class_;
import com.example.timetable_integration.Models.Slot;
import com.example.timetable_integration.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ClassAdaptor extends RecyclerView.Adapter<ClassAdaptor.myViewHolder>{


    Context mcontext;
    ArrayList<Class_> class_list;

    public ClassAdaptor(Context mcontext, ArrayList<Class_> class_list) {
        this.mcontext = mcontext;
        this.class_list = class_list;
    }

    @NonNull
    @NotNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_list,parent,false);


        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ClassAdaptor.myViewHolder holder, int position) {

        SimpleDateFormat spf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String today_day = spf.format(d);

        ArrayList<Slot> slot = class_list.get(position).getSlots();

        if(slot==null)
        {
            if(class_list.get(position).getDeadline()==null)
            {
                Date quiz_date_time = class_list.get(position).getTime();
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);

                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");

                String time = sdf1.format(quiz_date_time).replace("PM","pm").replace("AM","am");

                holder.course_time.setText(time);
                holder.course_duration.setText(class_list.get(position).getDuration());
                holder.strip.setBackgroundColor(Color.parseColor("#FFC907"));
            }
            else
            {

                Date assignment_date = class_list.get(position).getDeadline();

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);

                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d");

                String time = sdf1.format(assignment_date).replace("PM","pm").replace("AM","am");
                String date = sdf2.format(assignment_date);


                holder.course_time.setText(time);
                holder.course_duration.setText(date);
                holder.strip.setBackgroundColor(Color.parseColor("#060D19"));
            }
        }
        else
        {
            for(Slot _slot:slot)
            {
                if(_slot.getDay().equals(today_day))
                {
                    holder.course_time.setText(_slot.getTime());
                    holder.course_duration.setText(class_list.get(position).getDuration());
                    break;
                }
            }
        }



        String name_code = class_list.get(position).getCode() + " " + class_list.get(position).getName();
        holder.course_name.setText(name_code);
        holder.course_type.setText(class_list.get(position).getTags());
        holder.course_place.setText(class_list.get(position).getPlatform());
        holder.txt_time.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return class_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView course_name,course_type,course_place,course_time,course_duration,txt_time;
        View strip;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
            course_type = itemView.findViewById(R.id.course_type);
            course_place = itemView.findViewById(R.id.course_place);
            course_time = itemView.findViewById(R.id.course_time);
            course_duration = itemView.findViewById(R.id.course_duration);
            txt_time = itemView.findViewById(R.id.txt_Time);
            strip = itemView.findViewById(R.id.course_colour);

        }
    }

}
