package com.example.timetable_integration.Adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.MainActivity;
import com.example.timetable_integration.Models.Class_;
import com.example.timetable_integration.Models.Slot;
import com.example.timetable_integration.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ClassAdaptor extends RecyclerView.Adapter<ClassAdaptor.myViewHolder>{


    Context mcontext;
    ArrayList<Class_> class_list;
    FirebaseFirestore fstore;

    SharedPreferences sp;

    String student_program = "BTech";
    String student_year = "First_Year";
    String student_semester = "Semester 1";
    String student_branch;


    public ClassAdaptor(Context mcontext, ArrayList<Class_> class_list) {
        this.mcontext = mcontext;
        this.class_list = class_list;

        fstore = FirebaseFirestore.getInstance();

        sp = mcontext.getSharedPreferences("rollcode", 0);
        String roll_no = sp.getString("roll", null);

        //following line to be removed after integration:
        roll_no = "200101001";

        // Extracting details from roll no.
        if (roll_no != null){
            //int roll = Integer.parseInt(roll_no);
            String branch_code = roll_no.substring(4,6);
            switch(branch_code){
                case "01":  student_branch = "CSE"; break;
                case "02":  student_branch = "ECE"; break;
                case "03":  student_branch = "ME"; break;
                case "04":  student_branch = "CE"; break;
                case "06":  student_branch = "BSBE"; break;
                case "07":  student_branch = "CL"; break;
                case "21":  student_branch = "EP"; break;
                case "22":  student_branch = "CST"; break;
                case "23":  student_branch = "MNC"; break;
                case "05":  student_branch = "DD"; break;
                case "41":  student_branch = "HSS"; break;
                case "61":  student_branch = "DS"; break; //Data Science
                case "54":  student_branch = "RT"; break; //Rural Technology
                default: {student_branch = "00"; displayToast("Invalid roll number");}
            }

            //following code needs to be updated every year:
            String year_code = roll_no.substring(0,4);
            switch(year_code){
                case "2001":  {student_program = "B.Tech"; student_year = "First Year"; break;}
                case "2002":  {student_program = "B.Des"; student_year = "First Year"; break;}
                case "1901":  {student_program = "B.Tech"; student_year = "Second Year"; break;}
                case "1902":  {student_program = "B.Des"; student_year = "Second Year"; break;}
                case "1801":  {student_program = "B.Tech"; student_year = "Third Year"; break;}
                case "1802":  {student_program = "B.Des"; student_year = "Third Year"; break;}
                case "1701":  {student_program = "B.Tech"; student_year = "Fourth Year"; break;}
                case "1702":  {student_program = "B.Des"; student_year = "Fourth Year"; break;}
                case "2061":  {student_program = "PhD"; student_year = "First Year"; break;}
                case "1961":  {student_program = "PhD"; student_year = "Second Year"; break;}
                case "1861":  {student_program = "PhD"; student_year = "Third Year"; break;}
                case "2041":  {student_program = "M.Tech"; student_year = "First Year"; break;}
                case "1941":  {student_program = "M.Tech"; student_year = "Second Year"; break;}
                case "2042":  {student_program = "M.Des"; student_year = "First Year"; break;}
                case "1942":  {student_program = "M.Des"; student_year = "Second Year"; break;}
                case "2021":  {student_program = "M.Sc"; student_year = "First Year"; break;}
                case "1921":  {student_program = "M.Sc"; student_year = "Second Year"; break;}
                case "2022":  {student_program = "MA"; student_year = "First Year"; break;}
                case "1922":  {student_program = "MA"; student_year = "Second Year"; break;}


                default: {student_branch = "00"; displayToast("Invalid roll number");}
            }

        }

        String month = new SimpleDateFormat("MM").format(new Date());
        Log.d("Month", month);
        switch (month){
            case "01": case "02": case "03": case "04": case "05": case "06": { student_semester = "Semester 2"; break;}
            case "07": case "08": case "09": case "10": case "11": case "12":{ student_semester = "Semester 1"; break;}
        }

    }

    private void displayToast(String s) {
        Toast.makeText(mcontext,s,Toast.LENGTH_LONG).show();
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

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slot!=null)
                {
                    edit_class(v,position,holder);
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return class_list.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView course_name,course_type,course_place,course_time,course_duration,txt_time;
        View strip;
        MaterialCardView card_view;

        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
            course_type = itemView.findViewById(R.id.course_type);
            course_place = itemView.findViewById(R.id.course_place);
            course_time = itemView.findViewById(R.id.course_time);
            course_duration = itemView.findViewById(R.id.course_duration);
            txt_time = itemView.findViewById(R.id.txt_Time);
            strip = itemView.findViewById(R.id.course_colour);
            card_view = itemView.findViewById(R.id.card_view);


        }
    }






    @SuppressLint("SetTextI18n")
    public void edit_class(View v, int position, myViewHolder holder)
    {
        LayoutInflater inflater = (LayoutInflater)
                mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupclassView = inflater.inflate(R.layout.popup_edit_class, null);

        // create the popup window
        int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
        int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        boolean focusable = true;
        final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

        // show the popup window
        popupClassWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

        RelativeLayout time_slot_layout = popupclassView.findViewById(R.id.edit_time_slot);
        RelativeLayout choose_day_layout = popupclassView.findViewById(R.id.edit_day_slot);
        RelativeLayout choose_plateform_layout = popupclassView.findViewById(R.id.edit_plateform_slot);
        RelativeLayout choose_name_layout = popupclassView.findViewById(R.id.edit_name_slot);
        EditText course_code = popupclassView.findViewById(R.id.course_code);
        EditText course_name = popupclassView.findViewById(R.id.course_name);
        EditText course_plateform = popupclassView.findViewById(R.id.course_plateform);
        RecyclerView slotrecycler = popupclassView.findViewById(R.id.slot_recycler);
        TextView add_new_slot = popupclassView.findViewById(R.id.add_new_slot);
        Button update = popupclassView.findViewById(R.id.update_class);
        Button new_slot_btn = popupclassView.findViewById(R.id.create_new_slot);
        Button delete = popupclassView.findViewById(R.id.delete_class);

        EditText course_time = popupclassView.findViewById(R.id.course_time);
        TextView Monday = popupclassView.findViewById(R.id.Monday);
        TextView Tuesday = popupclassView.findViewById(R.id.Tuesday);
        TextView Wednesday = popupclassView.findViewById(R.id.Wednesday);
        TextView Thursday = popupclassView.findViewById(R.id.Thursday);
        TextView Friday = popupclassView.findViewById(R.id.Friday);
        TextView Saturday = popupclassView.findViewById(R.id.Saturday);
        TextView Sunday = popupclassView.findViewById(R.id.Sunday);
        TextView header = popupclassView.findViewById(R.id.edit_header_class);
        RelativeLayout rel = popupclassView.findViewById(R.id.edit_class_tag_layout);



        TextView add_tags_button = popupclassView.findViewById(R.id.add_tags);


        final String[] tags;

        if(class_list.get(position).getTags().equals("Lab")){

            header.setText("Edit "+class_list.get(position).getCode()+" Lab");
            add_new_slot.setText("+ add a lab slot");
            tags = new String[]{"Lab"};
        }
        else{
            header.setText("Edit "+class_list.get(position).getCode()+" Class");
            add_new_slot.setText("+ add a class slot");
            tags = new String[]{"Class"};
        }

        course_code.setText(class_list.get(position).getCode());
        course_name.setText(class_list.get(position).getName());
        course_plateform.setText(class_list.get(position).getPlatform());


        add_tags_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view1) {
                LinearLayout l = popupclassView.findViewById(R.id.tags);
                EditText e = popupclassView.findViewById(R.id.tag_edit_text);
                String s = e.getText().toString();
                tags[0] = tags[0] + "," + s;
                e.setText("");
                TextView t = new TextView(popupclassView.getContext());
                t.setText("   "+s);
                t.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                t.setTextAppearance(R.style.tag);

                l.addView(t);
            }
        });




        final Boolean[] isclick = {false};

        ArrayList<Slot> slot_list = class_list.get(position).getSlots();

        SlotAdaptor slotAdaptor = new SlotAdaptor(slot_list,mcontext);

        LinearLayoutManager ll = new LinearLayoutManager(mcontext);
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);
        slotrecycler.setLayoutManager(ll);
        slotrecycler.setAdapter(slotAdaptor);
        slotAdaptor.notifyDataSetChanged();


        //Add new class slots
        add_new_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slotrecycler.setVisibility(View.GONE);
                add_new_slot.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                rel.setVisibility(View.GONE);
                time_slot_layout.setVisibility(View.VISIBLE);
                choose_day_layout.setVisibility(View.VISIBLE);
                choose_name_layout.setVisibility(View.VISIBLE);
                choose_plateform_layout.setVisibility(View.VISIBLE);
                new_slot_btn.setVisibility(View.VISIBLE);
                isclick[0]= !course_time.getText().toString().isEmpty();

                //Highlighting respective button of selected week days
                Monday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if (Monday.getCurrentTextColor() == Color.parseColor("#99A2A5"))
                        {
                            Monday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Monday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Monday", course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Monday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Monday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Monday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Tuesday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Tuesday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Tuesday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Tuesday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Tuesday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Tuesday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Tuesday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Tuesday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Wednesday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Wednesday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Wednesday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Wednesday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Wednesday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Wednesday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Wednesday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Wednesday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Thursday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Thursday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Thursday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Thursday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Thursday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Thursday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Thursday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Thursday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Friday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Friday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Friday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Friday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Friday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Friday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Friday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Friday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Saturday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Saturday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Saturday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Saturday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Saturday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Saturday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Saturday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Saturday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });
                Sunday.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        if(Sunday.getCurrentTextColor()==Color.parseColor("#99A2A5"))
                        {
                            Sunday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                            Sunday.setTextColor(Color.parseColor("#4D5C62"));
                            slot_list.add(new Slot("Sunday",course_time.getText().toString()));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                        else
                        {
                            Sunday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                            Sunday.setTextColor(Color.parseColor("#99A2A5"));
                            slot_list.removeIf(a->(a.getDay().equals("Sunday")));
                            slotrecycler.setAdapter(slotAdaptor);
                            slotAdaptor.notifyDataSetChanged();
                        }
                    }
                });

                // Adding slot to slot list
                new_slot_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slotrecycler.setVisibility(View.VISIBLE);
                        add_new_slot.setVisibility(View.VISIBLE);
                        new_slot_btn.setVisibility(View.GONE);
                        delete.setVisibility(View.VISIBLE);
                        update.setVisibility(View.VISIBLE);
                        rel.setVisibility(View.GONE);
                        time_slot_layout.setVisibility(View.GONE);
                        choose_day_layout.setVisibility(View.GONE);
                        choose_name_layout.setVisibility(View.GONE);
                        choose_plateform_layout.setVisibility(View.GONE);
                        course_time.setText("");

                        Sunday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Sunday.setTextColor(Color.parseColor("#99A2A5"));
                        Saturday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Saturday.setTextColor(Color.parseColor("#99A2A5"));
                        Friday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Friday.setTextColor(Color.parseColor("#99A2A5"));
                        Monday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Monday.setTextColor(Color.parseColor("#99A2A5"));
                        Tuesday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Tuesday.setTextColor(Color.parseColor("#99A2A5"));
                        Wednesday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Wednesday.setTextColor(Color.parseColor("#99A2A5"));
                        Thursday.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
                        Thursday.setTextColor(Color.parseColor("#99A2A5"));

                        isclick[0]=false;
                    }
                });

            }
        });


        isclick[0]=!course_code.getText().toString().isEmpty();

        //Submit final request to create new class
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isclick[0])
                {

                    //adding data to the firstore
                    HashMap<String,Object> doc=new HashMap<>();

                    doc.put("Code",course_code.getText().toString());
                    doc.put("Name",course_name.getText().toString());
                    doc.put("Platform",course_plateform.getText().toString());
                    doc.put("Status","Pending");
                    doc.put("Slots",slot_list);
                    doc.put("tags",tags[0]);
                    doc.put("Duration","1 hour");
                    doc.put("type","edit");

                    fstore.collection("TimeTable/"+student_program+'/'+student_year+'/'+student_semester+'/'+student_branch+"/Group 1/Class")
                            .document(course_code.getText().toString()+" edit")
                            .set(doc)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Request Sent
                                    popupClassWindow.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    //Request not sent
                                    Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    //adding data to the firstore
                    HashMap<String,Object> doc=new HashMap<>();

                    doc.put("Code",course_code.getText().toString());
                    doc.put("Name",course_name.getText().toString());
                    doc.put("Platform",course_plateform.getText().toString());
                    doc.put("Status","Pending");
                    doc.put("Slots",slot_list);
                    doc.put("tags","Theory");
                    doc.put("Duration","1 hour");
                    doc.put("type","delete");

                    fstore.collection("TimeTable/"+student_program+'/'+student_year+'/'+student_semester+'/'+student_branch+"/Group 1/Class")
                            .document(course_code.getText().toString()+" delete")
                            .set(doc)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Request Sent
                                    popupClassWindow.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    //Request not sent
                                    Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

            }
        });


    }




}
