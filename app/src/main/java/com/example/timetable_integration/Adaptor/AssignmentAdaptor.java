package com.example.timetable_integration.Adaptor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetable_integration.MainActivity;
import com.example.timetable_integration.Models.Assignment;
import com.example.timetable_integration.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AssignmentAdaptor extends RecyclerView.Adapter<AssignmentAdaptor.myholder>{


    ArrayList<Assignment> assignment_list;
    Context mcontext;
    Date date;

    FirebaseFirestore fstore;

    SharedPreferences sp;

    String student_program = "BTech";
    String student_year = "First_Year";
    String student_semester = "Semester 1";
    String student_branch;

    public AssignmentAdaptor(ArrayList<Assignment> assignment_list, Context mcontext) {
        this.assignment_list = assignment_list;
        this.mcontext = mcontext;

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

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_assignment(v,position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return assignment_list.size();
    }

    public static class myholder extends RecyclerView.ViewHolder
    {
        TextView assignment_name,assignment_type,assignment_platform,assignment_time,assignment_deadline,txt_assignment_date;
        View strip_colour;
        MaterialCardView card_view;

        public myholder(@NonNull @NotNull View itemView) {
            super(itemView);


            assignment_name = itemView.findViewById(R.id.course_name);
            assignment_type = itemView.findViewById(R.id.course_type);
            assignment_platform = itemView.findViewById(R.id.course_place);
            assignment_time = itemView.findViewById(R.id.course_time);
            assignment_deadline = itemView.findViewById(R.id.course_duration);
            txt_assignment_date = itemView.findViewById(R.id.txt_Time);
            strip_colour = itemView.findViewById(R.id.course_colour);
            card_view = itemView.findViewById(R.id.card_view);

        }
    }

    private void edit_assignment(View vi, int position) {

        LayoutInflater inflater = (LayoutInflater)
                mcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupclassView = inflater.inflate(R.layout.popup_edit_assignment, null);

        // create the popup window
        int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
        int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        boolean focusable = true;
        final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

        // show the popup window
        popupClassWindow.showAtLocation(vi, Gravity.BOTTOM, 0, 0);

        Map<String, Object> data = new HashMap<>();

        TextView header = popupclassView.findViewById(R.id.assignment_header);

        header.setText("Edit "+assignment_list.get(position).getCode()+" Assignment");

        TextView time_selector = popupclassView.findViewById(R.id.new_date);
        time_selector.setText(assignment_list.get(position).getDeadline().toString());
        time_selector.setOnClickListener(view2 -> {
            showDateTimePicker(popupclassView.getContext(),popupclassView);
        });
        ImageView deleteDate = popupclassView.findViewById(R.id.date_delete_button);
        deleteDate.setOnClickListener(v -> {
            date = null;
            time_selector.setText("Select Deadline");
            v.setVisibility(View.GONE);
        });

        final String[] tags = {"Assignment"};
        TextView add_tags_button = popupclassView.findViewById(R.id.add_tags);

        // Add tags to assignment
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

        Button update_assignment = popupclassView.findViewById(R.id.update_assignment);
        Button delete_assignment = popupclassView.findViewById(R.id.delete_assignment);
        EditText  code = popupclassView.findViewById(R.id.new_assignment_code);
        EditText  name = popupclassView.findViewById(R.id.new_assignment_name);
        EditText  platform = popupclassView.findViewById(R.id.new_assignment_platform);

        code.setText(assignment_list.get(position).getCode());
        name.setText(assignment_list.get(position).getName());
        platform.setText(assignment_list.get(position).getPlatform());

        //Submit final request to create new assignment
        update_assignment.setOnClickListener(v1 -> {
            Log.d("abcd", tags[0]);
            data.put("Code",code.getText().toString());
            data.put("Name",name.getText().toString());
            data.put("Platform",platform.getText().toString());
            if (date != null) {
                data.put("Deadline", new Timestamp(date));
            }
            data.put("tags",tags[0]);
            data.put("Status","Pending");
            data.put("type","edit");
            fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection(student_branch).document("Group 1").collection("Assignment").add(data)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            //Request not sent
                            Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            Log.d("abcd", e.getMessage());
                        }
                    });

            popupClassWindow.dismiss();
        });


        delete_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.put("Code",code.getText().toString());
                data.put("Name",name.getText().toString());
                data.put("Platform",platform.getText().toString());
                if (date != null) {
                    data.put("Deadline", new Timestamp(date));
                }
                data.put("tags",tags[0]);
                data.put("Status","Pending");
                data.put("type","delete");
                fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection(student_branch).document("Group 1").collection("Assignment").add(data)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                //Request not sent
                                Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
                                Log.d("abcd", e.getMessage());
                            }
                        });

                popupClassWindow.dismiss();
            }
        });

    }

    public void showDateTimePicker(Context context, View popup) {

        Calendar calendar;
        calendar = Calendar.getInstance();
        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                TextView update_time = popup.findViewById(R.id.new_date);
                String d = calendar.getTime().toString();
                update_time.setText(d.substring(11,16) + ", "+ d.substring(0,10) +","+ d.substring(29,34));
                //update_time.setText(calendar.MINUTE + calendar.HOUR_OF_DAY +calendar.DAY_OF_MONTH + calendar.MONTH + calendar.YEAR);
                ImageView deleteDate = popup.findViewById(R.id.date_delete_button);
                deleteDate.setVisibility(View.VISIBLE);
                Log.v("abcd", "The chosen one " + calendar.getTime()); //16,29
                date = calendar.getTime();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }



}
