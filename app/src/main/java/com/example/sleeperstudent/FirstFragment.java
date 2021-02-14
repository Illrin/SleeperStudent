package com.example.sleeperstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FirstFragment extends Fragment {
    TextView tvTips, tvSleep, tvStart, tvIntro;


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
     *                  startTime: String representing recommended bedtime, "" if there is none
     *                  type: What kind of display the app should be using
     *                      0: standard, display startTime and sleepAmount
     *                      1: no wakeup time, just use sleepAmount
     *                      2: nap rec, mention that (implement later)
     *                      3: issue due to lack of data
     *  Post Condition: Sets first fragment's TextViews to properly display the determined
     *                  sleep time and tips.
     ************************************************************************************/
    public void displayResults(ArrayList<String> tips, int sleepAmount, String startTime, int type){
        StringBuilder fullTips = new StringBuilder("Sleep Improvement Tips:\n");
        for(int i = 0; i < tips.size(); i++) {
            fullTips.append(i).append(". ").append(tips.get(i)).append("\n");
        }
        tvTips.setText(fullTips.toString());
        ViewGroup.LayoutParams startParams = tvStart.getLayoutParams();
        ViewGroup.LayoutParams sleepParams = tvSleep.getLayoutParams();
        String rec;
        switch(type){
            case 0:
                sleepParams.height += startParams.height;
                startParams.height = 0;
                tvStart.setLayoutParams(startParams);
                tvSleep.setLayoutParams(sleepParams);
                rec = "You should get " + sleepAmount + " hours of sleep tonight";
                tvSleep.setText(rec);
                break;
            case 1:
                sleepParams.height -= 206;
                startParams.height = 206;
                tvStart.setLayoutParams(startParams);
                tvSleep.setLayoutParams(sleepParams);
                rec = "Your recommended bedtime is " + startTime;
                tvStart.setText(rec);
                int minutes = sleepAmount % 60;
                int hours = sleepAmount / 60;
                rec = "For a total of " + hours + " hour(s) and " + minutes + " minute(s).";
                tvSleep.setText(rec);
                break;
            case 3:
                String issue = "Looks like you haven't been updating us on your sleep times. Please submit times you've slept recently and try again.";
                tvTips.setText(issue);
        }
    }

    public void querySleep(){
        Tips tipCalculator = new Tips();
        User user = new User();
        user.inputData(requireContext());
        if(!user.getRealName().equals("")) {
            String intro = "Welcome back, " + user.getRealName();
            tvIntro.setText(intro);
        }
        Integer[] query = user.buildQuery();
        if(user.getAge() == 0 || query.length != 8) {
            String update = "Please update your information properly calculate your recommended amount of sleep.";
            if(user.getAge() == 0) update += "\n-Please include your age in your personal information";
            if(query.length != 8) update += "\n-Please submit your recent sleep times";
            tvTips.setText(update);
            return;
        }
        ArrayList<String> tips = tipCalculator.topTenRecs(query);
        //TODO call sleep calculation function here
        //TODO call displayResults() with that info
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTips = view.findViewById(R.id.tv_tips);
        tvStart= view.findViewById(R.id.tv_sleepTime);
        tvSleep = view.findViewById(R.id.tv_sleepAmount);
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