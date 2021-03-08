package com.example.sleeperstudent;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class FirstFragment extends Fragment {
    TextView tvTips, tvStart, tvIntro;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    /*************************************************************************************
     *        Function: displayResults
     *       Variables: tips: ArrayList of Strings, coming from Tips.topTenRecs()
     *                  sleepAmount: total amount of sleep recommended
     *                  startTime: Calendar representing the recommended sleep time
     *  Post Condition: Sets first fragment's TextViews to properly display the determined
     *                  sleep time and tips.
     ************************************************************************************/
    public void displayResults(ArrayList<String> tips, Calendar startTime){
        StringBuilder fullTips = new StringBuilder("Sleep Improvement Tips:\n");
        for(int i = 0; i < tips.size(); i++) {
            fullTips.append(i+1).append(". ").append(tips.get(i)).append("\n");
        }
        tvTips.setText(fullTips.toString());
        String side = "AM";
        int hour = startTime.get(Calendar.HOUR_OF_DAY);
        if(hour >= 12){
            hour -= 12;
            side = "PM";
        }
        if(hour == 0) hour = 12;
        @SuppressLint("DefaultLocale")
        String rec = "Your recommended bedtime is " + hour
                + ":" + String.format("%02d", startTime.get(Calendar.MINUTE)) + side;
        tvStart.setText(rec);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void querySleep(){
        Tips tipCalculator = new Tips();
        User user = new User();
        user.inputData(requireContext());

        if(!user.getRealName().equals("")) {
            String intro = "Welcome back, " + user.getRealName();
            tvIntro.setText(intro);
        }
        Integer[] query = user.buildQuery(requireContext());
        ArrayList<String> tips = new ArrayList<String>();
        if(query != null) tips = tipCalculator.topTenRecs(query);
        Sleep sleep = new Sleep(user.getHours(), user.getMinutes());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day == 7) day = 0;
        int wakeup = user.getWakeup(day);
        int minute = 0;
        int hour = 6;
        if(wakeup > 0){
            wakeup = Math.abs(wakeup) - 1;
            minute = wakeup % 60;
            hour = wakeup / 60;
        }
        Calendar wakeupTime = sleep.calculateSleepTime(hour,minute);
        displayResults(tips, wakeupTime);
        if(user.getAge() == 0 || query == null) {
            String update = "Please update your information to get the full recommendation.";
            if(user.getAge() == 0){
                tvStart.setText("");
                update += "\n-Please include your age in your personal information";
            }
            if(query == null) update += "\n-Please submit your recent sleep times in chronological order";
            tvTips.setText(update);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTips = view.findViewById(R.id.tv_tips);
        tvStart= view.findViewById(R.id.tv_sleepTime);
        tvIntro = view.findViewById(R.id.textview_first);
        querySleep();

        view.findViewById(R.id.bt_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.bt_sleepInput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_sleepInput);
            }
        });

        view.findViewById(R.id.bt_toWake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_wakeUp);
            }
        });

    }
}