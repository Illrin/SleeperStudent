package com.example.sleeperstudent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class PersonalInfo extends Fragment {
    Button btSave;
    EditText etHeight, etWeight, etAge, etName;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setup widgets
        btSave = view.findViewById(R.id.bt_save);
        etHeight = view.findViewById(R.id.et_height);
        etWeight = view.findViewById(R.id.et_weight);
        etAge = view.findViewById(R.id.et_age);
        etName = view.findViewById(R.id.et_name);

        //Initalize user, set saved text
        User user = new User();
        user.inputData(this.getContext());
        etHeight.setText(String.valueOf(user.getHeight()));
        etWeight.setText(String.valueOf(user.getWeight()));
        etAge.setText(String.valueOf(user.getAge()));
        etName.setText(user.getRealName());

        //Save Changes
        btSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int height = Integer.parseInt(etHeight.getText().toString());
                int weight = Integer.parseInt(etWeight.getText().toString());
                int age = Integer.parseInt(etAge.getText().toString());
                String name = etName.getText().toString();
                if(name.equals("")) name = "Name";
                user.setHeight(height);
                user.setWeight(weight);
                user.setAge(age);
                user.setRealName(name);
                user.writeToFile(view.getContext());
                NavHostFragment.findNavController(PersonalInfo.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });


        //Move to initial screen
        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PersonalInfo.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}