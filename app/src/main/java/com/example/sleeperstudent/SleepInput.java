package com.example.sleeperstudent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TimePicker;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SleepInput} factory method to
 * create an instance of this fragment.
 */
public class SleepInput extends Fragment {
    //Date/time pickers
    Button btStartDate, btEndDate, btSubmit, btStartTime, btEndTime;

    //Date dialogue
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;

    //Time Storage
    int startHour, startMinute, endHour, endMinute;

    //Quality parameters
    SeekBar sbMood, sbQuality, sbStress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initialize widgets
        btStartDate = view.findViewById(R.id.bt_startDate);
        btEndDate = view.findViewById(R.id.bt_endDate);
        btSubmit = view.findViewById(R.id.bt_submit);
        btStartTime = view.findViewById(R.id.bt_startTime);
        btEndTime = view.findViewById(R.id.bt_endTime);

        sbMood = view.findViewById(R.id.sb_mood);
        sbQuality = view.findViewById(R.id.sb_quality);
        sbStress = view.findViewById(R.id.sb_stress);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Set dates as today
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        btEndDate.setText(date);
        btStartDate.setText(date);

        //Set Listeners
        btStartDate.setOnClickListener(new View.OnClickListener(){//Create date dialogue (start)
            @Override
            public void onClick(View view){
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener1,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {//Set button text
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                btStartDate.setText(date);
            }
        };

        btEndDate.setOnClickListener(new View.OnClickListener(){//Create date dialogue (end)
            @Override
            public void onClick(View view){
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {//Set button text (end)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                btEndDate.setText(date);
            }
        };

        btStartTime.setOnClickListener(new View.OnClickListener(){//Start time dialogue
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startHour = hourOfDay;
                                startMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,startHour,startMinute);
                                btStartTime.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(startHour,startMinute);
                timePickerDialog.show();
            }
        });

        btEndTime.setOnClickListener(new View.OnClickListener(){//End time dialogue
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endHour = hourOfDay;
                                endMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,endHour,endMinute);
                                btEndTime.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(endHour,endMinute);
                timePickerDialog.show();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() { //Submission button, calculate
            //time slept and quality of sleep, put in db
            @Override
            public void onClick(View view) {
                String sDate = btStartDate.getText().toString();
                String eDate = btEndDate.getText().toString();
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
                int sleepTime = 0; //time slept, in minutes
                try {
                    Date date1 = simpleDateFormat1.parse(sDate);
                    Date date2 = simpleDateFormat1.parse(eDate);
                    long startDate = date1.getTime();
                    Log.d("start",String.valueOf(startDate));
                    long endDate = date2.getTime();
                    Log.d("end",String.valueOf(endDate));
                    if (startDate <= endDate) {
                        Period period = new Period(startDate, endDate, PeriodType.days());
                        int days = period.getDays();
                        if(startHour > endHour){
                            days--;
                            endHour += 24;
                        }
                        if(startMinute > endMinute) {
                            endHour--;
                            endMinute += 60;
                        }
                        if(days >= 0) sleepTime += ((endHour - startHour) * 60)
                                + (endMinute - startMinute) + (days * 24 * 60);
                    }
                    String namevar="apple";
                    Realm realm = Realm.getDefaultInstance();
                    //DbUserInfo userentry= realm.where(DbUserInfo.class).equalTo("name",namevar).findFirst();
                    //RealmResults<DbUserInfo> userentry=realm.where(DbUserInfo.class).equalTo("name",namevar).findAll();
                    DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();

                    int mood = sbMood.getProgress();
                    //Log.d("MOODS",String.valueOf(mood));
                    int quality = sbQuality.getProgress();
                    //Log.d("QUAL",String.valueOf(quality));
                    int stress = sbStress.getProgress();
                    long startTime=startDate+startHour*3600000+startMinute*60000;
                    long endTime=endDate+endHour*3600000+endMinute*60000 - 86400000;
                    //Log.d("STRESS",String.valueOf(stress));
                    int finalSleepTime = sleepTime;
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            //Log.d("HEIGHT",String.valueOf(userentry.getHeight()));
                            //Log.d("moodvalue",String.valueOf(moods.get(0)));
                            //Log.d("moodbefore",String.valueOf(userentry.getMood().size()));
                            userentry.getMood().add(mood);
                            userentry.getStartDate().add(startTime);
                            userentry.getEndDate().add(endTime);
                            userentry.getQuality().add(quality);
                            userentry.getStress().add(stress);
                            userentry.getSleepTime().add(finalSleepTime);

                            /*RealmList<Long> sdates=userentry.getStartDate();
                            Log.d("datesize",String.valueOf(sdates.size()));
                            for(int i=0;i<sdates.size();i++){
                                Log.d("dates"+String.valueOf(i),String.valueOf(sdates.get(i)));
                            }*/

                            realm.insertOrUpdate(userentry);
                        }
                    });


                    //TODO put into database - startDate, endDate, startHour, endHour,
                    //TODO startMinute, endMinute, sleepTime, mood, quality, stress
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                User user = new User();
                user.inputData(requireContext());
                if(user.getAge() != 0) {
                    Sleep sleep = new Sleep(user.getHours(), user.getMinutes());
                    int consistency = sleep.isConsistent();
                    if (consistency == 1) NavHostFragment.findNavController(SleepInput.this)
                            .navigate(R.id.action_sleepInput_to_sleepCalibration);
                }
                NavHostFragment.findNavController(SleepInput.this)
                        .navigate(R.id.action_sleepInput_to_FirstFragment);
            }
        });

        //transition to first screen
        view.findViewById(R.id.bt_inputToFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SleepInput.this)
                        .navigate(R.id.action_sleepInput_to_FirstFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep_input, container, false);
    }
}