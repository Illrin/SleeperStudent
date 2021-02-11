package com.example.sleeperstudent;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Tips
    {
        public static final int SLEEP_BREAK = 0;
        public static final int PHONE_USED = 1;
        public static final int EXTREME_LACK = 2;
        public static final int OF_AGE = 3;
        public static final int LONG_NAPS = 4;
        public static final int NEEDS_SCHEDULE = 5;
        public static final int STRESSED_OUT = 6;
        public static final int GENERAL = 7;


        public static final int TAG_SIZE = 8;

        private ArrayList<String> allTips;
        private ArrayList<int[]> graph;


        public Tips()
        {
            allTips = new ArrayList<>();
            graph = new ArrayList<>();
            initContents();
        }



        /*************************************************************************************
         *        Function: initContents
         *  Post Condition: Initialize all the tips and index them
         *
         ************************************************************************************/
        private void initContents()
        {
            //everything in java auto inits to 0

            int arr[] = new int[TAG_SIZE];
            allTips.add("Reduce blue light Intake before bed; do not use your device before bed!");
            arr[PHONE_USED] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Try changing the temperature to b/t 60-67 degrees F");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Try new sleep positions");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Focus on relaxation, clear your mind, do something non simulating, like counting sheep. Avoid looking at the clock!");
            arr[SLEEP_BREAK] =  1;
            arr[STRESSED_OUT] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Avoid eating and drinking copious amounts of liquid prior to bedtime");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Have a calming/de-stressing ritual prior to sleeping");
            arr[STRESSED_OUT] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Your naps are too long! Generally good naps are around 10 - 20 minutes");
            arr[LONG_NAPS] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("You are severely sleep deprived and need a full nights sleep");
            arr[EXTREME_LACK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Try maintaining a more consistent sleep schedule");
            arr[NEEDS_SCHEDULE] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Consider talking to your doctor about your sleep issues");
            arr[EXTREME_LACK] = 1;
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Try getting out of bed for a few minutes if you wake up in the night. Be sure to keep the lights off!");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Avoid drinking alcohol before going to bed");
            arr[OF_AGE] = 1;
            graph.add(arr);

            //General Tip Series
            arr = new int[TAG_SIZE];
            allTips.add("Try increasing your light exposure during the day");
            arr[GENERAL] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Avoid caffeine later in the day");
            arr[GENERAL] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Make the best sleep environment possible. Quiet, Dark, with a good pillow and mattress");
            arr[GENERAL] = 1;
            graph.add(arr);

            arr = new int[TAG_SIZE];
            allTips.add("Try exercising early in the day. Doing it at night might make your sleep worse");
            arr[GENERAL] = 1;
            graph.add(arr);
            //...

        }


        private class pair implements Comparable<pair>
        {
            public int priority, tipNum;

            public pair()
            {
                priority = tipNum = 0;
            }

            public pair(int priority, int tipNum)
            {
                this.priority = priority;
                this.tipNum = tipNum;
            }

            @Override
            public int compareTo(pair o)
            {
                return o.priority - this.priority ;
            }
        }




        /*************************************************************************************
         *        Function: dotProduct
         *       Variables: user: Vector representing user's sleep attributes
         *                  tipTypes: Vector representing a specific tip and it's tags
         *  Post Condition: Returns the dot product of the two vectors
         *
         ************************************************************************************/
        private int dotProduct(Integer user[], int tipTypes[])
        {
            int sum = 0;
            for(int i = 0; i < TAG_SIZE; ++i)
                sum+=(user[i] * tipTypes[i]);
            return sum;

        }

        /*************************************************************************************
         *        Function: topTenRecs
         *       Variables: userProfile: A vector representing the user's sleep attributes
         *  Post Condition: Retrieves the top 10 most relevant tips stored in a list.
         *            Note: THE TOP TEN RECS WILL BE BASED ON TIPS THAT WHEN TAKING DOT PRODUCT
         *                  WITH THE USER PROFILE YIELDS THE HIGHEST RESULT. LMK IF THIS IS NOT
         *                  WHAT IS NEEDED.
         *
         ************************************************************************************/
        public ArrayList<String> topTenRecs(Integer userProfile[])
        {

            ArrayList<pair> items = new ArrayList<>();
            for(int i = 0; i < allTips.size(); ++i)
                items.add(new pair(dotProduct(userProfile, graph.get(i)), i));

            ArrayList<String> toRec = new ArrayList<>();
            Collections.sort(items);
            for(int i = 0; i < 10; ++i)
                toRec.add(allTips.get(items.get(i).tipNum));

            return toRec;
        }

    }
