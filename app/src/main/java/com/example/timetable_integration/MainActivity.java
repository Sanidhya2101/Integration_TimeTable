package com.example.timetable_integration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timetable_integration.Adaptor.CourseAdaptor;
import com.example.timetable_integration.Models.Course;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_new_event,edit_event;
    RecyclerView courses_rec;
    ArrayList<Course> coursedata;
    CourseAdaptor courseAdaptor;
    FirebaseFirestore fstore;

    String student_program = "BTech";
    String student_year = "First_Year";
    String student_semester = "Semester 1";

    Date date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_new_event=(FloatingActionButton) findViewById(R.id.add_new_event);
        edit_event=(FloatingActionButton) findViewById(R.id.edit_event);
        fstore = FirebaseFirestore.getInstance();
        courses_rec = findViewById(R.id.course_class);
        coursedata = new ArrayList<>();

        LinearLayoutManager ll = new LinearLayoutManager(this);
        ll.setReverseLayout(true);
        ll.setStackFromEnd(true);

        courses_rec.setLayoutManager(ll);
        courseAdaptor = new CourseAdaptor(coursedata,this);

        fstore.collection("Timetable").orderBy("course_time", Query.Direction.DESCENDING)
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
        });



        add_new_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_event(view);
            }
        });
        edit_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_event_f(view);
            }
        });
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

                Button time_selector = popupclassView.findViewById(R.id.new_assignment_deadline);
                final long[] time = new long[1];
                time_selector.setOnClickListener(view2 -> {
                    showDateTimePicker(popupclassView.getContext());
                });
                Button update_time = popupclassView.findViewById(R.id.add_assignment);
                EditText  code = popupclassView.findViewById(R.id.new_assignment_code);
                EditText  name = popupclassView.findViewById(R.id.new_assignment_name);
                EditText  platform = popupclassView.findViewById(R.id.new_assignment_platform);
                update_time.setOnClickListener(v1 -> {
                    data.put("Code",code.getText().toString());
                    data.put("Name",name.getText().toString());
                    data.put("Platform",platform.getText().toString());
                    data.put("Deadline", new Timestamp(date));
                    data.put("Status","Pending");
                    fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection("Assignment").add(data)
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

            Button time_selector = popupclassView.findViewById(R.id.new_quiz_time_button);
            final long[] time = new long[1];
            time_selector.setOnClickListener(view2 -> {
                showDateTimePicker(popupclassView.getContext());
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
                data.put("Time", new Timestamp(date));
                data.put("Status","Pending");
                fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection("Quiz").add(data)
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

                Button time_selector = popupclassView.findViewById(R.id.new_viva_time_button);
                time_selector.setOnClickListener(view2 -> {
                    showDateTimePicker(popupclassView.getContext());
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
                    data.put("Time", new Timestamp(date));
                    data.put("Status","Pending");
                    fstore.collection("TimeTable").document(student_program).collection(student_year).document(student_semester).collection("Viva").add(data)
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
    private void edit_event_f(View view) {
        //  add_new_event.hide();
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_edit_events, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        RelativeLayout edit_class=(RelativeLayout) popupView.findViewById(R.id.select_class);
        RelativeLayout edit_assignment=(RelativeLayout) popupView.findViewById(R.id.select_assignment);
        RelativeLayout edit_quiz=(RelativeLayout) popupView.findViewById(R.id.select_quiz);
        RelativeLayout edit_lab=(RelativeLayout) popupView.findViewById(R.id.select_lab);
        RelativeLayout edit_viva=(RelativeLayout) popupView.findViewById(R.id.select_viva);
        edit_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_edit_class, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
        edit_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_edit_assignment, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
        edit_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_edit_quiz, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
        edit_lab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_edit_class, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
        });
        edit_viva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_edit_class, null);

                // create the popup window
                int width_class = LinearLayout.LayoutParams.MATCH_PARENT;
                int height_class = LinearLayout.LayoutParams.WRAP_CONTENT;
                //boolean focusable = true; // lets taps outside the popup also dismiss it
                boolean focusable = true;
                final PopupWindow popupClassWindow = new PopupWindow(popupclassView, width_class, height_class, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupClassWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
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

    public void showDateTimePicker(Context context) {
        Calendar calendar;
        calendar = Calendar.getInstance();
        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Log.v("abcd", "The chosen one " + calendar.getTime());
                date = calendar.getTime();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
    }

}