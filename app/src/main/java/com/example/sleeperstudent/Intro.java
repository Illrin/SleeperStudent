package com.example.sleeperstudent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import io.realm.Realm;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.io.File;

public class Intro extends Fragment {
    int progress = 0;
    Button btNext;
    TextView tvIntro;
    EditText etUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        File folder = new File(requireContext().getFilesDir() + "/userData");
        File txtFile = new File(folder.getAbsolutePath() + "/" + "userData.txt");
        if(txtFile.exists()){
            NavHostFragment.findNavController(Intro.this)
                    .navigate(R.id.action_intro_to_FirstFragment);
        }
        btNext = view.findViewById(R.id.bt_next);
        tvIntro = view.findViewById(R.id.tv_intro);
        etUsername = view.findViewById(R.id.et_username);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(progress){
                    case 0:
                        String next = "First, we'll need a username to associate you with.\n(You won't be able to change this later, choose wisely)";
                        etUsername.setVisibility(View.VISIBLE);
                        etUsername.setEnabled(true);
                        tvIntro.setText(next);
                        progress++;
                        break;
                    case 1:
                        //pull up docs to see if username exists already
                        Realm realm = Realm.getDefaultInstance();
                        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
                        if(userentry!=null){
                            String retry = "This username is already taken, please use another.";
                            tvIntro.setText(retry);
                        }
                        else{
                            String done = "Perfect! Next up, we just need some physical information and you should be good to go!";
                            User user = new User();
                            user.inputData(view.getContext());
                            user.setUserName(etUsername.getText().toString());
                            user.writeToFile(view.getContext());

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    DbUserInfo userentry = realm.createObject(DbUserInfo.class, etUsername.getText().toString());
                                    realm.insertOrUpdate(userentry);
                                }
                            });

                            /*userentry.setStartDate(new RealmList<Long>());
                            userentry.setEndDate(new RealmList<Long>());
                            userentry.setSleepTime(new RealmList<Integer>());
                            userentry.setMood(new RealmList<Integer>());
                            userentry.setQuality(new RealmList<Integer>());
                            userentry.setStress(new RealmList<Integer>());*/

                            Log.d("testinfo","creating user");
                            etUsername.setVisibility(View.INVISIBLE);
                            etUsername.setEnabled(false);
                            tvIntro.setText(done);
                            progress++;
                        }
                        break;
                    case 2:
                        NavHostFragment.findNavController(Intro.this)
                                .navigate(R.id.action_intro_to_SecondFragment);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }
}