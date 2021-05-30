package com.example.timetable_integration.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.Models.Slot;
import com.example.timetable_integration.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SlotAdaptor extends RecyclerView.Adapter<SlotAdaptor.myViehHolder> {

    ArrayList<Slot> slot_list;
    Context mcontext;

    public SlotAdaptor(ArrayList<Slot> slot_list, Context mcontext) {
        this.slot_list = slot_list;
        this.mcontext = mcontext;
    }

    @NonNull
    @NotNull
    @Override
    public myViehHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.slot_list,parent,false);


        return new myViehHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SlotAdaptor.myViehHolder holder, int position) {

        String txt_day=slot_list.get(position).getDay();
        String txt_time=slot_list.get(position).getTime();


        holder.class_time_date.setText(txt_time+" "+txt_day);
        holder.slot_no.setText("Slot "+(Integer.parseInt(String.valueOf(position))+1));


        holder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slot_list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,slot_list.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return slot_list.size();
    }

    public static class myViehHolder extends RecyclerView.ViewHolder{

        TextView class_time_date;
        ImageView minus_btn;
        TextView slot_no;

        public myViehHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            class_time_date = itemView.findViewById(R.id.slot_time);
            minus_btn = itemView.findViewById(R.id.slot_minus);
            slot_no = itemView.findViewById(R.id.Slot_No);
        }
    }

}
