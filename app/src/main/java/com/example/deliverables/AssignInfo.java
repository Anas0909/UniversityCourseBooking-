package com.example.deliverables;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;

public class AssignInfo extends AppCompatActivity {
    private String teacherName, courseCode;
    private Button btn_back, btn_confirm, btn_day1StartTime, btn_day2StartTime, btn_day1EndTime, btn_day2EndTime, btn_reset;
    private TextView tv_nameAndCode;
    private ListView lv_oldCourseInfo;
    private EditText et_descrption, et_capacity;
    private DataBaseHelper db = new DataBaseHelper(AssignInfo.this);
    private Spinner sp_day1, sp_day2;
    private ArrayAdapter<String> dayAdapter;
    private String[] daySelection = {"None", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private int hourDay1Start,hourDay1End,hourDay2Start, hourDay2End;
    private DataBaseHelper dbHelper = new DataBaseHelper(AssignInfo.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_info);
        Intent i = getIntent();
        teacherName = i.getStringExtra("teacherName");
        courseCode = i.getStringExtra("courseCode");



        et_descrption = findViewById(R.id.et_assign_info_description);
        et_capacity = findViewById(R.id.et_assign_info_capacity);
        btn_back = findViewById(R.id.btn_assign_info_back);
        btn_confirm = findViewById(R.id.btn_assign_info_confirm);
        btn_day1StartTime = findViewById(R.id.btn_assign_info_startTime1);
        btn_day2StartTime = findViewById(R.id.btn_assign_info_startTime2);
        btn_day1EndTime = findViewById(R.id.btn_assign_info_endTime1);
        btn_day2EndTime = findViewById(R.id.btn_assign_info_endTime2);
        btn_reset = findViewById(R.id.btn_assign_info_reset_time);

        tv_nameAndCode = findViewById(R.id.tv_assign_info_nameAndCode);
        tv_nameAndCode.setText("You are assigning/editing course : " + courseCode + ", Instructor: " + teacherName);
        lv_oldCourseInfo = findViewById(R.id.lv_oldCourseInfo);
        ArrayAdapter courseArrayAdapter;
        ArrayList<String> infoArray = dbHelper.searchedCoursesByCode(courseCode);
        courseArrayAdapter = new ArrayAdapter<String>(AssignInfo.this, android.R.layout.simple_list_item_1, infoArray);
        lv_oldCourseInfo.setAdapter(courseArrayAdapter);


        sp_day1 = findViewById(R.id.sp_assign_info_day1);
        sp_day2 = findViewById(R.id.sp_assign_info_day2);

        dayAdapter = new ArrayAdapter<String>(AssignInfo.this, android.R.layout.simple_spinner_dropdown_item, daySelection);
        sp_day1.setAdapter(dayAdapter);
        sp_day2.setAdapter(dayAdapter);


        btn_day1StartTime.setOnClickListener(new View.OnClickListener() {
            int hour, minute;
            @Override
            public void onClick(View view) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hour = selectHour;
                        minute = selectMinute;
                        btn_day1StartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        hourDay1Start = hour;
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignInfo.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Day 1 Course Start Time");
                timePickerDialog.show();

            }
        });

        btn_day1EndTime.setOnClickListener(new View.OnClickListener() {
            int hour, minute;
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hour = selectHour;
                        minute = selectMinute;
                        btn_day1EndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        hourDay1End = hour;
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignInfo.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Day 1 Course End Time");
                timePickerDialog.show();

            }
        });

        btn_day2StartTime.setOnClickListener(new View.OnClickListener() {
            int hour, minute;
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hour = selectHour;
                        minute = selectMinute;
                        btn_day2StartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        hourDay2Start = hour;
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignInfo.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Day 2 Course Start Time");
                timePickerDialog.show();

            }
        });

        btn_day2EndTime.setOnClickListener(new View.OnClickListener() {
            int hour, minute;
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectHour, int selectMinute) {
                        hour = selectHour;
                        minute = selectMinute;
                        btn_day2EndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                        hourDay2End = hour;
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(AssignInfo.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Day 2 Course End Time");
                timePickerDialog.show();

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstr();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_day1StartTime.setText("START TIME");
                btn_day1EndTime.setText("END TIME");
                btn_day2StartTime.setText("START TIME");
                btn_day2EndTime.setText("END TIME");

                hourDay1Start = 0;
                hourDay1End = 0;
                hourDay2Start = 0;
                hourDay2End = 0;
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_descrption.getText().toString().trim().isEmpty() || et_capacity.getText().toString().trim().isEmpty()){
                    Toast.makeText(AssignInfo.this, "Input all fields!", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(et_capacity.getText().toString().trim()) <= 0 ){
                    Toast.makeText(AssignInfo.this, "Capacity can not be negative or 0!", Toast.LENGTH_SHORT).show();
                }
                else if((btn_day1StartTime.getText().toString().equals("START TIME") || btn_day1EndTime.getText().toString().equals("END TIME"))&&
                        (btn_day2StartTime.getText().toString().equals("START TIME") || btn_day2EndTime.getText().toString().equals("END TIME"))){
                    Toast.makeText(AssignInfo.this, "Missing a time slot for the day!", Toast.LENGTH_SHORT).show();
                }
                else if(sp_day1.getSelectedItem().toString().equals("None") && sp_day2.getSelectedItem().toString().equals("None")){
                    Toast.makeText(AssignInfo.this, "You must  select at least one day! ", Toast.LENGTH_SHORT).show();
                }
                else if(sp_day1.getSelectedItem().toString().equals(sp_day2.getSelectedItem().toString())){
                    Toast.makeText(AssignInfo.this, "You can't put two slots in one day!", Toast.LENGTH_SHORT).show();
                }
                else if(sp_day2.getSelectedItem().toString().equals("None") && !(btn_day2StartTime.getText().toString().equals("START TIME") && btn_day2EndTime.getText().toString().equals("END TIME"))){
                    Toast.makeText(AssignInfo.this, "Wrong time for the Wrong day!", Toast.LENGTH_SHORT).show();
                }
                else if(sp_day1.getSelectedItem().toString().equals("None") && !(btn_day1StartTime.getText().toString().equals("START TIME") && btn_day1EndTime.getText().toString().equals("END TIME"))){
                    Toast.makeText(AssignInfo.this, "Wrong time for the Wrong day!", Toast.LENGTH_SHORT).show();
                }
                else if(btn_day1StartTime.getText().toString().equals(btn_day1EndTime.getText().toString()) || btn_day2StartTime.getText().toString().equals(btn_day2EndTime.getText().toString())){
                    Toast.makeText(AssignInfo.this, "Can't have Start and End time same!", Toast.LENGTH_SHORT).show();
                }

                else{
                    String days = "", time = "";
                    int capacity = Integer.parseInt(et_capacity.getText().toString().trim());
                    if(sp_day1.getSelectedItem().toString().equals("None") ){
                        if(hourDay2End< hourDay2Start){
                            Toast.makeText(AssignInfo.this, "You can't have class End time before it begins!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            days = sp_day2.getSelectedItem().toString();
                            time = btn_day2StartTime.getText().toString() + " to " + btn_day2EndTime.getText().toString();
                            db.updateCourseInfo(courseCode, et_descrption.getText().toString().trim(), days, time, capacity, teacherName);
                            openInstr();
                        }
                    }
                    else if (sp_day2.getSelectedItem().toString().equals("None")){
                        if(hourDay1End< hourDay1Start){
                            Toast.makeText(AssignInfo.this, "You can't have class End time before it begins!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            days = sp_day1.getSelectedItem().toString();
                            time = btn_day1StartTime.getText().toString() + " to " + btn_day1EndTime.getText().toString();
                            db.updateCourseInfo(courseCode, et_descrption.getText().toString().trim(), days, time, capacity, teacherName);
                            openInstr();
                        }
                    }
                    else if(!sp_day1.getSelectedItem().toString().equals("None") && !sp_day2.getSelectedItem().toString().equals("None")){
                        if (btn_day1StartTime.getText().toString().equals("START TIME") || btn_day1EndTime.getText().toString().equals("END TIME")){
                            Toast.makeText(AssignInfo.this, "Missing a time slot for day 1", Toast.LENGTH_SHORT).show();
                        }
                        else if(btn_day2StartTime.getText().toString().equals("START TIME") || btn_day2EndTime.getText().toString().equals("END TIME")){
                            Toast.makeText(AssignInfo.this, "Missing a time slot for day 2", Toast.LENGTH_SHORT).show();}
                        else if((hourDay1End< hourDay1Start)||(hourDay2End< hourDay2Start)){
                            Toast.makeText(AssignInfo.this, "You can't have class End time before it begins!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            days = sp_day1.getSelectedItem().toString() + ", " + sp_day2.getSelectedItem().toString();
                            time = btn_day1StartTime.getText().toString() + " to " + btn_day1EndTime.getText().toString() + " and " + btn_day2StartTime.getText().toString() + " to " + btn_day2EndTime.getText().toString();
                            db.updateCourseInfo(courseCode, et_descrption.getText().toString().trim(), days, time, capacity, teacherName);
                            openInstr();
                        }
                    }

                }
            }
        });




    }

    public void openInstr() {
        Intent intent = new Intent(this, Instr.class);
        intent.putExtra("teacherName", teacherName);
        intent.putExtra("courseCode", courseCode);
        startActivity(intent);
    }



}