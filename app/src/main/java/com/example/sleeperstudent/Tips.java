package com.example.sleeperstudent;

import android.util.Pair;

import java.util.ArrayList;
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


        public static final int TAG_SIZE = 7;

        private ArrayList<String> allTips;
        private ArrayList<Integer[]> graph;


        public Tips()
        {
            allTips = new ArrayList<>();
            graph = new ArrayList<>();
        }


        /*************************************************************************************
         *        Function: initContents
         *  Post Condition: Initialize all the tips and index them
         *
         ************************************************************************************/
        private void initContents()
        {
            //everything in java auto inits to 0

            Integer arr[] = new Integer[TAG_SIZE];
            allTips.add("Reduce blue light Intake before bed; do not use your device before bed!");
            arr[PHONE_USED] = 1;
            graph.add(arr);


            arr = new Integer[TAG_SIZE];
            allTips.add("Try changing the temperature to b/t 60-67 degrees F");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("Try new sleep positions");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("Focus on relaxation, clear your mind, do something non simulating, like counting sheep. Avoid looking at the clock!");
            arr[SLEEP_BREAK] = 1;
            arr[STRESSED_OUT] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("Avoid eating and drinking copious amounts of liquid prior to bedtime");
            arr[SLEEP_BREAK] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("Have a calming/de-stressing ritual prior to sleeping");
            arr[STRESSED_OUT] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("Your naps are too long! Generally good naps are around 10 - 20 minutes");
            arr[LONG_NAPS] = 1;
            graph.add(arr);

            arr = new Integer[TAG_SIZE];
            allTips.add("You are severely sleep deprived and need a full nights sleep");
            arr[EXTREME_LACK] = 1;
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
                return this.priority - o.priority;
            }
        }



        /*************************************************************************************
         *        Function: topTenRecs
         *       Variables: userProfile: A vector representing the user's sleep attributes
         *  Post Condition: Retrieves the top 10 most relevant tips stored in a list
         *
         ************************************************************************************/
        private int dotProduct(Integer user[], Integer tipTypes[])
        {
            int sum = 0;
            for(int i = 0; i < TAG_SIZE; ++i)
                sum+=(user[i] * tipTypes[i]);
            return sum;

        }

        /*************************************************************************************
         *        Function: topTenRecs
         *       Variables: userProfile: A vector representing the user's sleep attributes
         *  Post Condition: Retrieves the top 10 most relevant tips stored in a list
         *
         ************************************************************************************/
        public ArrayList<String> topTenRecs(Integer userProfile[])
        {

            Set<pair> items = new TreeSet<>();

            for(int i = 0; i < allTips.size(); ++i)
            {
                items.add(new pair(dotProduct(userProfile, graph.get(i)), i));
            }
            int counter = 0;
            ArrayList<String> toRec = new ArrayList<>();
            Iterator<pair> it = items.iterator();
            pair currItem;
            while(it.hasNext() && counter < 10)
            {
                currItem = it.next();
                toRec.add(allTips.get(currItem.tipNum));
            }
            return toRec;
        }

    }
