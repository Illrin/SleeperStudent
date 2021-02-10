package com.example.sleeperstudent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

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
     *        Function: checkValidity
     *  Post Condition: Determines if current saved data is enough for a query, returns
     *                  True if enough, False otherwise.
     ************************************************************************************/
    public boolean checkValidity(){
        User user = new User();
        user.inputData(getActivity());
        //im not too sure if height/weight actually impacts sleep, but ill have them
        //invalidate for now
        if(user.getUserName().equals("") || user.getAge() == -1
                || user.getHeight() == -1 || user.getWeight() == -1) return false;
        //check for at least 1 database entry in sleep
        return true;
    }

    /*************************************************************************************
     *        Function: displayResults
     *       Variables: tips: ArrayList of Strings, coming from Tips.topTenRecs()
     *                  sleepAmount: total amount of sleep recommended
     *                  startTime: String representing recommended bedtime, "" if there is none
     *  Post Condition: Sets first fragment's TextViews to properly display the determined
     *                  sleep time and tips.
     ************************************************************************************/
    public void displayResults(ArrayList<String> tips, int sleepAmount, String startTime){
        StringBuilder fullTips = new StringBuilder("Sleep Improvement Tips:\n");
        for(int i = 0; i < tips.size(); i++) {
            fullTips.append(i).append(". ").append(tips.get(i)).append("\n");
        }
        tvTips.setText(fullTips.toString());
        if(startTime.equals("")){
            ViewGroup.LayoutParams startParams = tvStart.getLayoutParams();
            ViewGroup.LayoutParams sleepParams = tvSleep.getLayoutParams();
            sleepParams.height += startParams.height;
            startParams.height = 0;
            tvStart.setLayoutParams(startParams);
            tvSleep.setLayoutParams(sleepParams);
            String rec = "You should get " + sleepAmount + " hours of sleep tonight";
            tvSleep.setText(rec);
        }
        else {
            ViewGroup.LayoutParams startParams = tvStart.getLayoutParams();
            ViewGroup.LayoutParams sleepParams = tvSleep.getLayoutParams();
            sleepParams.height -= 206;
            startParams.height = 206;
            tvStart.setLayoutParams(startParams);
            tvSleep.setLayoutParams(sleepParams);
            String rec = "Your recommended bedtime is " + startTime;
            tvStart.setText(rec);
            int minutes = sleepAmount % 60;
            int hours = sleepAmount / 60;
            rec = "For a total of " + hours + " hour(s) and " + minutes + " minute(s).";
            tvSleep.setText(rec);
        }
    }

    //Todo: add button to refresh
    public void querySleep(){
        Tips tipCalculator = new Tips();
        User user = new User();
        user.inputData(getActivity());
        ArrayList<String> tips = tipCalculator.topTenRecs(user.buildQuery());
        String intro = "Welcome back, " + user.getUserName();
        tvIntro.setText(intro);
        //TODO call sleep calculation function here
        //TODO call displayResults() with that info
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTips = view.findViewById(R.id.tv_tips);
        tvStart= view.findViewById(R.id.tv_sleepTime);
        tvSleep = view.findViewById(R.id.tv_sleepAmount);
        tvIntro = view.findViewById(R.id.textview_first);
        if(checkValidity()) querySleep();//essentially a "returning user" check

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