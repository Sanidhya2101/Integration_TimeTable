package com.example.timetable_integration;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_new_event,edit_event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_new_event=(FloatingActionButton) findViewById(R.id.add_new_event);
        edit_event=(FloatingActionButton) findViewById(R.id.edit_event);
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
            }
        });
        add_new_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                popupWindow.dismiss();
                //  new_class(view);
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupclassView = inflater.inflate(R.layout.popup_new_quiz, null);

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
}