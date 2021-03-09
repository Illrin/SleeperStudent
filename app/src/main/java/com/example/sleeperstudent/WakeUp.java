package com.example.sleeperstudent;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import io.realm.Realm;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class WakeUp extends Fragment {
    //setup widget variables
    Button btWake;
    SeekBar sbWeekday;
    TextView tvWeekday;
    Switch swIgnore;

    //variables for storing current input
    int wakeHour, wakeMinute, weekday;
    //for printing
    String[] weekdayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //Change text/state of widgets based on current day and saved data
    public void setupWidgets(@NonNull View view){
        //new user, get data
        User initial = new User();
        initial.inputData(view.getContext());
        int wakeup = initial.getWakeup(weekday);
        //get switch status, wakeup time
        int ignored = Integer.signum(wakeup);
        wakeup = Math.abs(wakeup) - 1;
        //setup text to output
        int minute = wakeup % 60;
        int hour = wakeup / 60;
        Calendar calendar = Calendar.getInstance();
        calendar.set(0,0,0, hour, minute);
        //set widgets
        btWake.setText(DateFormat.format("hh:mm aa", calendar));
        swIgnore.setChecked(ignored == -1);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initialize widgets
        btWake = view.findViewById(R.id.bt_wakeup);
        sbWeekday = view.findViewById(R.id.sb_weekday);
        tvWeekday = view.findViewById(R.id.tv_weekday);
        swIgnore = view.findViewById(R.id.sw_ignore);

        setupWidgets(view);

        btWake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //set button text
                                wakeHour = hourOfDay;
                                wakeMinute = minute;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,wakeHour,wakeMinute);
                                btWake.setText(DateFormat.format("hh:mm aa", calendar));

                                //save to user
                                User user = new User();
                                user.inputData(view.getContext());
                                int wakeup = (wakeHour * 60) + wakeMinute + 1;
                                if(swIgnore.isChecked()) wakeup *= -1;
                                user.setWakeups(wakeup, weekday);
                                user.writeToFile(view.getContext());
                            }
                        }, 12, 0, false);
                timePickerDialog.updateTime(wakeHour,wakeMinute);
                timePickerDialog.show();
            }
        });

        sbWeekday.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weekday = progress;
                tvWeekday.setText(weekdayNames[weekday]);
                setupWidgets(view);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //stub
            }
        });

        swIgnore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //switch signs, save
                User user = new User();
                user.inputData(view.getContext());
                int ignore = isChecked ? -1:1;
                int wakeup = Math.abs(user.getWakeup(weekday));
                user.setWakeups(wakeup * ignore, weekday);
                user.writeToFile(view.getContext());
            }
        });

        view.findViewById(R.id.bt_wakeToHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WakeUp.this)
                        .navigate(R.id.action_wakeUp_to_FirstFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wakeup, container, false);
    }
}