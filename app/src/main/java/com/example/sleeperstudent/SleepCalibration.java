package com.example.sleeperstudent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SleepCalibration extends Fragment {
    Button btYes, btNo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sleep_calibration, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setup widgets
        btYes = view.findViewById(R.id.btYes);
        btNo = view.findViewById(R.id.btNo);

        //Initalize user, set saved text
        User user = new User();
        user.inputData(requireContext());

        //Recalibrate
        btYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Sleep sleep = new Sleep(user.getHours(), user.getMinutes());
                sleep.recalibrateSleep(user.getAge());
                user.writeToFile(view.getContext());
                NavHostFragment.findNavController(SleepCalibration.this)
                        .navigate(R.id.action_sleepCalibration_to_FirstFragment);
            }
        });


        //Move to initial screen
        view.findViewById(R.id.btNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SleepCalibration.this)
                        .navigate(R.id.action_sleepCalibration_to_FirstFragment);
            }
        });
    }
}