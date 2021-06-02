package com.example.timetable_integration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timetable_integration.Adaptor.CourseAdaptor;
import com.example.timetable_integration.Adaptor.SlotAdaptor;
import com.example.timetable_integration.Models.Course;
import com.example.timetable_integration.Models.Slot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_new_event;
    RecyclerView courses_rec;
    ArrayList<Course> coursedata;
    CourseAdaptor courseAdaptor;
    FirebaseFirestore fstore;

    SharedPreferences sp;

    String student_program = "BTech";
    String student_year = "First_Year";
    String student_semester = "Semester 1";
    String student_branch;

    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_new_event=(FloatingActionButton) findViewById(R.id.add_new_event);
        fstore = FirebaseFirestore.getInstance();
        courses_rec = findViewById(R.id.course_class);
        coursedata = new ArrayList<>();

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);

        courses_rec.setLayoutManager(ll);
        courseAdaptor = new CourseAdaptor(coursedata,this);

        sp = getSharedPreferences("rollcode", 0);
        String roll_no = sp.getString("roll", null);

        //following line to be removed after integration:
        roll_no = "200101001";

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

        /*fstore.collection("Timetable").orderBy("course_time", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                for(DocumentSnapshot d:list)
                {
                    Course obj = d.toObject(Course.class);
                    coursedata.add(obj);
                }

                courses_rec.setAdapter(courseAdaptor);
                courseAdaptor.notifyDataSetChanged();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });*/


        add_new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_event(view);
            }
        });
    }

    private void displayToast(String s) {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
    }

    private void new_event(View view) {
        //  add_new_event.hide();
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_new_events, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        RelativeLayout add_new_class=(RelativeLayout) popupView.findViewById(R.id.select_class);
        RelativeLayout add_new_assignment=(RelativeLayout) popupView.findViewById(R.id.select_assignment);
        RelativeLayout add_new_quiz=(RelativeLayout) popupView.findViewById(R.id.select_quiz);
        RelativeLayout add_new_lab=(RelativeLayout) popupView.findViewById(R.id.select_lab);
        RelativeLayout add_new_viva=(RelativeLayout) popupView.findViewById(R.id.select_viva);


        add_new_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
              //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_new_class, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                RelativeLayout time_slot_layout = popupclassView.findViewById(R.id.edit_time_slot);
                RelativeLayout choose_day_layout = popupclassView.findViewById(R.id.edit_day_slot);
                RelativeLayout choose_plateform_layout = popupclassView.findViewById(R.id.edit_plateform_slot);
                RelativeLayout choose_name_layout = popupclassView.findViewById(R.id.edit_name_slot);
                EditText course_code = popupclassView.findViewById(R.id.course_code);
                EditText course_name = popupclassView.findViewById(R.id.course_name);
                EditText course_plateform = popupclassView.findViewById(R.id.course_plateform);
                RecyclerView slotrecycler = popupclassView.findViewById(R.id.slot_recycler);
                TextView add_new_slot = popupclassView.findViewById(R.id.add_new_slot);
                Button submit = popupclassView.findViewById(R.id.create_new_class);
                Button new_slot_btn = popupclassView.findViewById(R.id.create_new_slot);

                EditText course_time = popupclassView.findViewById(R.id.course_time);
                TextView Monday = popupclassView.findViewById(R.id.Monday);
                TextView Tuesday = popupclassView.findViewById(R.id.Tuesday);
                TextView Wednesday = popupclassView.findViewById(R.id.Wednesday);
                TextView Thursday = popupclassView.findViewById(R.id.Thursday);
                TextView Friday = popupclassView.findViewById(R.id.Friday);
                TextView Saturday = popupclassView.findViewById(R.id.Saturday);
                TextView Sunday = popupclassView.findViewById(R.id.Sunday);

                final Boolean[] isclick = {false};

                ArrayList<Slot> slot_list = new ArrayList<>();

                SlotAdaptor slotAdaptor = new SlotAdaptor(slot_list,getApplicationContext());

                LinearLayoutManager ll = new LinearLayoutManager(getApplicationContext());
                ll.setReverseLayout(true);
                ll.setStackFromEnd(true);

                slotrecycler.setLayoutManager(ll);

                slotrecycler.setAdapter(slotAdaptor);
                slotAdaptor.notifyDataSetChanged();


                add_new_slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        slotrecycler.setVisibility(View.GONE);
                        add_new_slot.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);
                        time_slot_layout.setVisibility(View.VISIBLE);
                        choose_day_layout.setVisibility(View.VISIBLE);
                        choose_name_layout.setVisibility(View.VISIBLE);
                        choose_plateform_layout.setVisibility(View.VISIBLE);
                        new_slot_btn.setVisibility(View.VISIBLE);
                        isclick[0]= !course_time.getText().toString().isEmpty();


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
                                    Monday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Tuesday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Wednesday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Thursday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Friday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Saturday.setBackgroundColor(getResources().getColor(R.color.white));
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
                                    Sunday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Sunday.setTextColor(Color.parseColor("#99A2A5"));
                                    slot_list.removeIf(a->(a.getDay().equals("Sunday")));
                                    slotrecycler.setAdapter(slotAdaptor);
                                    slotAdaptor.notifyDataSetChanged();
                                }
                            }
                        });


                        new_slot_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                slotrecycler.setVisibility(View.VISIBLE);
                                add_new_slot.setVisibility(View.VISIBLE);
                                new_slot_btn.setVisibility(View.GONE);
                                submit.setVisibility(View.VISIBLE);
                                time_slot_layout.setVisibility(View.GONE);
                                choose_day_layout.setVisibility(View.GONE);
                                choose_name_layout.setVisibility(View.GONE);
                                choose_plateform_layout.setVisibility(View.GONE);
                                course_time.setText("");

                                Sunday.setBackgroundColor(getResources().getColor(R.color.white));
                                Sunday.setTextColor(Color.parseColor("#99A2A5"));
                                Saturday.setBackgroundColor(getResources().getColor(R.color.white));
                                Saturday.setTextColor(Color.parseColor("#99A2A5"));
                                Friday.setBackgroundColor(getResources().getColor(R.color.white));
                                Friday.setTextColor(Color.parseColor("#99A2A5"));
                                Monday.setBackgroundColor(getResources().getColor(R.color.white));
                                Monday.setTextColor(Color.parseColor("#99A2A5"));
                                Tuesday.setBackgroundColor(getResources().getColor(R.color.white));
                                Tuesday.setTextColor(Color.parseColor("#99A2A5"));
                                Wednesday.setBackgroundColor(getResources().getColor(R.color.white));
                                Wednesday.setTextColor(Color.parseColor("#99A2A5"));
                                Thursday.setBackgroundColor(getResources().getColor(R.color.white));
                                Thursday.setTextColor(Color.parseColor("#99A2A5"));

                                isclick[0]=false;
                            }
                        });

                    }
                });


                isclick[0]=!course_code.getText().toString().isEmpty();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!isclick[0])
                        {

                            HashMap<String,Object> doc=new HashMap<>();

                            doc.put("Code",course_code.getText().toString());
                            doc.put("Name",course_name.getText().toString());
                            doc.put("Platform",course_plateform.getText().toString());
                            doc.put("Status","Pending");
                            doc.put("Slots",slot_list);
                            doc.put("Tag","Theory");
                            doc.put("Duration","1 hour");

                            fstore.collection("TimeTable/"+student_program+'/'+student_year+'/'+student_branch+'/'+student_semester+"/Group 1/Class")
                                    .document(course_code.getText().toString())
                                    .set(doc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            popupClassWindow.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });


            }
        });
        add_new_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_new_assignment, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                Map<String, Object> data = new HashMap<>();

                TextView time_selector = popupclassView.findViewById(R.id.new_date);
                time_selector.setOnClickListener(view2 -> {
                    showDateTimePicker(popupclassView.getContext(),popupclassView);
                });
                ImageView deleteDate = popupclassView.findViewById(R.id.date_delete_button);
                deleteDate.setOnClickListener(v -> {
                    date = null;
                    time_selector.setText("Select Deadline");
                    v.setVisibility(View.GONE);
                });

                final String[] tags = {""};
                TextView add_tags_button = popupclassView.findViewById(R.id.add_tags);
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
                        t.setText(s+"   ");
                        t.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        t.setTextAppearance(R.style.tag);

                        l.addView(t);
                    }
                });

                Button add_assignment = popupclassView.findViewById(R.id.add_assignment);
                EditText  code = popupclassView.findViewById(R.id.new_assignment_code);
                EditText  name = popupclassView.findViewById(R.id.new_assignment_name);
                EditText  platform = popupclassView.findViewById(R.id.new_assignment_platform);
                add_assignment.setOnClickListener(v1 -> {
                    Log.d("abcd", tags[0]);
                    data.put("Code",code.getText().toString());
                    data.put("Name",name.getText().toString());
                    data.put("Platform",platform.getText().toString());
                    if (date != null) {
                        data.put("Deadline", new Timestamp(date));
                    }
                    data.put("tags",tags[0]);
                    data.put("Status","Pending");
                    fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection(student_branch).document("Group 1").collection("Assignment").add(data)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    Log.d("abcd", e.getMessage());
                                }
                            });
                    popupClassWindow.dismiss();
                });

            }
        });
        add_new_quiz.setOnClickListener(view1 -> {
            Map<String, Object> data = new HashMap<>();

            popupWindow.dismiss();
            //  new_class(view);
            LayoutInflater inflater1 = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupclassView = inflater1.inflate(R.layout.popup_new_quiz, null);

            // create the popup window
            int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
            int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
            //boolean focusable = true; // lets taps outside the popup also dismiss it
            boolean focusable1 = true;
            final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable1);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

            TextView time_selector = popupclassView.findViewById(R.id.new_date);
            time_selector.setOnClickListener(view2 -> {
                showDateTimePicker(popupclassView.getContext(),popupclassView);
            });
            ImageView deleteDate = popupclassView.findViewById(R.id.date_delete_button);
            deleteDate.setOnClickListener(v -> {
                date = null;
                time_selector.setText("Select Date and Time");
                v.setVisibility(View.GONE);
            });

            final String[] tags = {""};
            TextView add_tags_button = popupclassView.findViewById(R.id.add_tags);
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
                    t.setText(s+"   ");
                    t.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    t.setTextAppearance(R.style.tag);

                    l.addView(t);
                }
            });

            Button update_time = popupclassView.findViewById(R.id.add_quiz);
            EditText  code = popupclassView.findViewById(R.id.new_quiz_code);
            EditText  name = popupclassView.findViewById(R.id.new_quiz_name);
            EditText  duration = popupclassView.findViewById(R.id.new_quiz_duration);
            EditText  platform = popupclassView.findViewById(R.id.new_quiz_platform);
            update_time.setOnClickListener(v1 -> {
                data.put("Code",code.getText().toString());
                data.put("Name",name.getText().toString());
                data.put("Duration",duration.getText().toString());
                data.put("Platform",platform.getText().toString());
                if (date != null) {
                    data.put("Time", new Timestamp(date));
                }
                data.put("tags",tags[0]);
                data.put("Status","Pending");
                fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection(student_branch).document("Group 1").collection("Quiz").add(data)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                popupClassWindow.dismiss();
            });

        });
        add_new_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_new_lab, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                RelativeLayout lab_name_layout = popupclassView.findViewById(R.id.edit_labname_slot);
                RelativeLayout lab_plateform_layout = popupclassView.findViewById(R.id.edit_labplateform_slot);
                RelativeLayout lab_time_layout = popupclassView.findViewById(R.id.edit_labtime_slot);
                RelativeLayout lab_day_layout = popupclassView.findViewById(R.id.edit_labday_slot);

                EditText lab_code = popupclassView.findViewById(R.id.lab_code);
                EditText lab_name = popupclassView.findViewById(R.id.lab_name);
                EditText lab_plateform = popupclassView.findViewById(R.id.lab_plateform);
                EditText lab_time = popupclassView.findViewById(R.id.lab_time);

                TextView Monday = popupclassView.findViewById(R.id.LMonday);
                TextView Tuesday = popupclassView.findViewById(R.id.LTuesday);
                TextView Wednesday = popupclassView.findViewById(R.id.LWednesday);
                TextView Thursday = popupclassView.findViewById(R.id.LThursday);
                TextView Friday = popupclassView.findViewById(R.id.LFriday);
                TextView Saturday = popupclassView.findViewById(R.id.LSaturday);
                TextView Sunday = popupclassView.findViewById(R.id.LSunday);
                TextView add_new_lab_slot = popupclassView.findViewById(R.id.add_new_labslot);

                RecyclerView lab_slot_rec = popupclassView.findViewById(R.id.slot_labrecycler);

                Button submit = popupclassView.findViewById(R.id.create_new_lab);
                Button create_lab_slot = popupclassView.findViewById(R.id.create_new_labslot);

                final Boolean[] isclick = {false};

                ArrayList<Slot> labslot_list=new ArrayList<>();

                SlotAdaptor labAdaptor = new SlotAdaptor(labslot_list,getApplicationContext());

                LinearLayoutManager ll = new LinearLayoutManager(getApplicationContext());
                ll.setReverseLayout(true);
                ll.setStackFromEnd(true);

                lab_slot_rec.setLayoutManager(ll);

                lab_slot_rec.setAdapter(labAdaptor);
                labAdaptor.notifyDataSetChanged();


                add_new_lab_slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lab_slot_rec.setVisibility(View.GONE);
                        add_new_lab_slot.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);

                        lab_name_layout.setVisibility(View.VISIBLE);
                        lab_day_layout.setVisibility(View.VISIBLE);
                        lab_time_layout.setVisibility(View.VISIBLE);
                        lab_plateform_layout.setVisibility(View.VISIBLE);
                        create_lab_slot.setVisibility(View.VISIBLE);
                        isclick[0]= !lab_time.getText().toString().isEmpty();


                        Monday.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View v) {
                                if (Monday.getCurrentTextColor() == Color.parseColor("#99A2A5"))
                                {
                                    Monday.setBackgroundColor(Color.parseColor("#D8E6FF"));
                                    Monday.setTextColor(Color.parseColor("#4D5C62"));
                                    labslot_list.add(new Slot("Monday", lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Monday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Monday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Monday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Tuesday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Tuesday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Tuesday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Tuesday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Wednesday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Wednesday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Wednesday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Wednesday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Thursday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Thursday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Thursday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Thursday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Friday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Friday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Friday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Friday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Saturday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Saturday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Saturday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Saturday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
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
                                    labslot_list.add(new Slot("Sunday",lab_time.getText().toString()));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                                else
                                {
                                    Sunday.setBackgroundColor(getResources().getColor(R.color.white));
                                    Sunday.setTextColor(Color.parseColor("#99A2A5"));
                                    labslot_list.removeIf(a->(a.getDay().equals("Sunday")));

                                    lab_slot_rec.setAdapter(labAdaptor);
                                    labAdaptor.notifyDataSetChanged();
                                }
                            }
                        });


                        create_lab_slot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                lab_slot_rec.setVisibility(View.VISIBLE);
                                add_new_lab_slot.setVisibility(View.VISIBLE);
                                submit.setVisibility(View.VISIBLE);

                                lab_name_layout.setVisibility(View.GONE);
                                lab_day_layout.setVisibility(View.GONE);
                                lab_time_layout.setVisibility(View.GONE);
                                lab_plateform_layout.setVisibility(View.GONE);
                                create_lab_slot.setVisibility(View.GONE);

                                lab_time.setText("");

                                Sunday.setBackgroundColor(getResources().getColor(R.color.white));
                                Sunday.setTextColor(Color.parseColor("#99A2A5"));
                                Saturday.setBackgroundColor(getResources().getColor(R.color.white));
                                Saturday.setTextColor(Color.parseColor("#99A2A5"));
                                Friday.setBackgroundColor(getResources().getColor(R.color.white));
                                Friday.setTextColor(Color.parseColor("#99A2A5"));
                                Monday.setBackgroundColor(getResources().getColor(R.color.white));
                                Monday.setTextColor(Color.parseColor("#99A2A5"));
                                Tuesday.setBackgroundColor(getResources().getColor(R.color.white));
                                Tuesday.setTextColor(Color.parseColor("#99A2A5"));
                                Wednesday.setBackgroundColor(getResources().getColor(R.color.white));
                                Wednesday.setTextColor(Color.parseColor("#99A2A5"));
                                Thursday.setBackgroundColor(getResources().getColor(R.color.white));
                                Thursday.setTextColor(Color.parseColor("#99A2A5"));

                                isclick[0]=false;
                            }
                        });

                    }
                });



                isclick[0]=!lab_code.getText().toString().isEmpty();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!isclick[0])
                        {

                            HashMap<String,Object> doc=new HashMap<>();

                            doc.put("Code",lab_code.getText().toString());
                            doc.put("Name",lab_name.getText().toString());
                            doc.put("Platform",lab_plateform.getText().toString());
                            doc.put("Status","Pending");
                            doc.put("Slots",labslot_list);
                            doc.put("Tag","Lab");
                            doc.put("Duration","3 hour");

                            fstore.collection("TimeTable/"+student_program+'/'+student_year+'/'+student_branch+'/'+student_semester+"/Group 1/Lab")
                                    .document(lab_code.getText().toString())
                                    .set(doc)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            popupClassWindow.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });



            }
        });
        add_new_viva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_new_viva, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

                Map<String, Object> data = new HashMap<>();

                TextView time_selector = popupclassView.findViewById(R.id.new_date);
                time_selector.setOnClickListener(view2 -> {
                    showDateTimePicker(popupclassView.getContext(),popupclassView);
                });
                ImageView deleteDate = popupclassView.findViewById(R.id.date_delete_button);
                deleteDate.setOnClickListener(v -> {
                    date = null;
                    time_selector.setText("Select Date and Time");
                    v.setVisibility(View.GONE);
                });

                final String[] tags = {""};
                TextView add_tags_button = popupclassView.findViewById(R.id.add_tags);
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
                        t.setText(s+"   ");
                        t.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        t.setTextAppearance(R.style.tag);

                        l.addView(t);
                    }
                });

                Button update_time = popupclassView.findViewById(R.id.add_viva);
                EditText  code = popupclassView.findViewById(R.id.new_viva_code);
                EditText  name = popupclassView.findViewById(R.id.new_viva_name);
                EditText  duration = popupclassView.findViewById(R.id.new_viva_duration);
                EditText  platform = popupclassView.findViewById(R.id.new_viva_platform);
                update_time.setOnClickListener(v1 -> {
                    data.put("Code",code.getText().toString());
                    data.put("Name",name.getText().toString());
                    data.put("Duration",duration.getText().toString());
                    data.put("Platform",platform.getText().toString());
                    if (date != null) {
                        data.put("Time", new Timestamp(date));
                    }
                    data.put("Status","Pending");
                    data.put("tags",tags[0]);
                    fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection(student_branch).document("Group 1").collection("Viva").add(data)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                    popupClassWindow.dismiss();
                });

            }
        });
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
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