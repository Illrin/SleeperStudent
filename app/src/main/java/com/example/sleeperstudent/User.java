package com.example.sleeperstudent;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import io.realm.Realm;
import io.realm.RealmList;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class User
{

    private static final String FILE_PATH = "userData.txt";
    private static final char DATA_SEPERATOR = (int) 7;

    //add more fields down here
    private String userName;
    private String realName;
    private int age;
    private int weight;//in pounds
    private int height;//in inches
    private int[] wakeups;
    private int hours;
    private int minutes;
    public User(){
        age = 0;
        userName = "User";
        realName = "Name";
        weight = 0;
        height = 0;
        wakeups = new int[7];
        for(int i = 0; i < 7; i++){
            wakeups[i] = -1;
        }
        hours = 7;
        minutes = 30;
    }

    //Setters
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public void setRealName(String realName) { this.realName = realName; }
    public void setAge(int age)
    {
        this.age = age;
    }
    public void setWeight(int weight){ this.weight = weight; }
    public void setHeight(int height){ this.height = height; }
    public void setWakeups(int wakeup, int weekday){ this.wakeups[weekday] = wakeup; }
    public void setHours(int hours){this.hours = hours;}
    public void setMinutes(int minutes){this.minutes = minutes;}

    //Getters
    public String getUserName()
    {
        return this.userName;
    }
    public String getRealName()
    {
        return this.realName;
    }
    public int getAge()
    {
        return age;
    }
    public int getWeight() { return weight; }
    public int getHeight() { return height; }
    public int getWakeup(int weekday) { return wakeups[weekday]; }//doesn't check for oob
    public int getHours(){return hours;}
    public int getMinutes(){return minutes;}


    /*************************************************************************************
     *        Function: writeToFile
     *       Variables: Context: Application contextual info.
     *  Post Condition: Writes user data to the system.
     *            Note: To pass context to this function, pass the "this" keyword. If you are
     *                  accessing this object in a listener function, pass view.getContext();
     ************************************************************************************/
    public void writeToFile(Context context)
    {
        FileOutputStream out = null;
        try
        {
            File folder = new File(context.getFilesDir() + "/userData");
            File txtFile = new File(folder.getAbsolutePath() + "/" + FILE_PATH);

            if(!txtFile.exists())
                folder.mkdir();
            if(!txtFile.exists())
                txtFile.createNewFile();

            out = new FileOutputStream(txtFile);
            String printName = "Name: " + DATA_SEPERATOR + userName + "\n";
            String printAge = "Age: " + DATA_SEPERATOR + Integer.toString(age) + "\n";
            String printHeight = "Height: " + DATA_SEPERATOR + Integer.toString(height) + "\n";
            String printWeight = "Weight: " + DATA_SEPERATOR + Integer.toString(weight) + "\n";
            String[] printWakeup = {"Sunday: ", "Monday: ", "Tuesday: ", "Wednesday: ", "Thursday: ",
                    "Friday: ", "Saturday: "};
            String printReal = "Real: " + DATA_SEPERATOR + realName + "\n";
            for(int i = 0; i < 7; i++){
                printWakeup[i] = printWakeup[i] + DATA_SEPERATOR + Integer.toString(wakeups[i]) + "\n";
            }
            String printHours = "Hours: " + DATA_SEPERATOR + hours + "\n";
            String printMin = "Minutes: " + DATA_SEPERATOR + minutes + "\n";

            out.write(printName.getBytes());
            out.write(printAge.getBytes());
            out.write(printHeight.getBytes());
            out.write(printWeight.getBytes());
            for(int i = 0; i < 7; i++){
                out.write(printWakeup[i].getBytes());
            }
            out.write(printReal.getBytes());
            out.write(printHours.getBytes());
            out.write(printMin.getBytes());

            out.flush();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*************************************************************************************
     *        Function: InputData
     *       Variables: Context: Application contextual info.
     *  Post Condition: Reads data from file to the object
     *            Note: To pass context to this function, pass the "this" keyword.
     ************************************************************************************/
    public void inputData(Context context)
    {
        File folder = new File(context.getFilesDir() + "/userData");
        File txtFile = new File(folder.getAbsolutePath() + "/" + FILE_PATH);

        try
        {
            FileInputStream fis = new FileInputStream(txtFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String input;
            String parseInput[];
            while ((input = br.readLine()) != null)
            {
                parseInput = input.split("" + DATA_SEPERATOR);
                switch (parseInput[0])
                {
                    case "Name: ":
                        userName = parseInput[1];
                        break;
                    case "Age: ":
                        age = Integer.parseInt(parseInput[1]);
                        break;
                    case "Height: ":
                        height = Integer.parseInt(parseInput[1]);
                        break;
                    case "Weight: ":
                        weight = Integer.parseInt(parseInput[1]);
                        break;
                    case "Sunday: ":
                        wakeups[0] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Monday: ":
                        wakeups[1] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Tuesday: ":
                        wakeups[2] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Wednesday: ":
                        wakeups[3] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Thursday: ":
                        wakeups[4] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Friday: ":
                        wakeups[5] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Saturday: ":
                        wakeups[6] = Integer.parseInt(parseInput[1]);
                        break;
                    case "Real: ":
                        realName = parseInput[1];
                        break;
                    case "Hours: ":
                        hours = Integer.parseInt(parseInput[1]);
                        break;
                    case "Minutes: ":
                        minutes = Integer.parseInt(parseInput[1]);
                        break;
                }

            }
            br.close();
            in.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /*************************************************************************************
     *        Function: getLastFiveBedTimes
     *  Post Condition: Returns an array of at max 5 latest sleep times in ms from the epoch.
     *                  If there are no entries, return null
     ************************************************************************************/
    private long[] getLastTenBedTimes()
    {
        long[] results=new long[10];
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Long> sdates=userentry.getStartDate();
                int index;
                if(sdates.size()>=10){
                    index=10;
                }
                else{
                    index=sdates.size();
                }
                for(int i=0;i<sdates.size();i++){
                    results[i]=sdates.get(sdates.size()-(index-i));
                }
            }
        });
        if(results[0]==0){
            return null;
        }
        return results;
    }

    /*************************************************************************************
     *        Function: getLastFiveWakeUpTimes
     *  Post Condition: Returns an array of at max 5 latest wakeup times in ms from the epoch.
     *                  See getLastFiveBedTimes for calculation
     *                  If there are no entries, return null
     ************************************************************************************/
    private long[] getLastTenWakeUpTimes()
    {
        long[] results=new long[10];
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Long> sdates=userentry.getEndDate();
                int index;
                if(sdates.size()>=10){
                    index=10;
                }
                else{
                    index=sdates.size();
                }
                for(int i=0;i<sdates.size();i++){
                    results[i]=sdates.get(sdates.size()-(index-i));
                }
            }
        });
        if(results[0]==0){
            return null;
        }
        return results;
    }

    /*************************************************************************************
     *        Function: getLastFiveWakeUpTimes
     *  Post Condition: Returns an array of at max 5 latest stress amounts, in range 1-10
     *                  If there are no entries, return null
     ************************************************************************************/
    private int[] getLastTenStressAmounts(){
        int[] results=new int[10];
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Integer> stress=userentry.getStress();
                int index;
                if(stress.size()>=10){
                    index=10;
                }
                else{
                    index=stress.size();
                }
                for(int i=0;i<stress.size();i++){
                    results[i]=stress.get(stress.size()-(index-i));
                }
            }
        });
        if(results[0]==0){
            return null;
        }
        return results;
    }

    /*************************************************************************************
     *        Function: getLastFiveSleepAmounts
     *  Post Condition: Returns an array of at max 5 latest sleep durations, in minutes
     *                  If there are no entries, return null
     ************************************************************************************/
    private int[] getLastTenSleepAmounts(){
        int[] results=new int[10];
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Integer> stress=userentry.getSleepTime();
                int index;
                if(stress.size()>=10){
                    index=10;
                }
                else{
                    index=stress.size();
                }
                for(int i=0;i<stress.size();i++){
                    results[i]=stress.get(stress.size()-(index-i));
                }
            }
        });
        if(results[0]==0){
            return null;
        }
        return results;
    }

    /*************************************************************************************
     *        Function: BuildQuery
     *       Variables: Context: Application contextual info
     *  Post Condition: Returns a vector that can be compared to Tip tag vectors
     *                  If there are no entries, return null
     ************************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Integer[] buildQuery(Context context){
        Integer[] query = {0,0,0,0,0,0,0,1};
        if(age >= 21) query[3] = 2;
        long[] wakeUpMs = getLastTenWakeUpTimes();
        long[] bedTimeMs = getLastTenBedTimes();
        int[] stresses = getLastTenStressAmounts();
        int[] durations = getLastTenSleepAmounts();

        if(wakeUpMs == null || bedTimeMs == null || stresses == null || durations == null) return null;

        int sleepCount = 0;
        for(int i = 0; i < wakeUpMs.length; i++){
            if(wakeUpMs[i] == 0){
                sleepCount = i;
                break;
            }
        }

        for(int i = 1; i < sleepCount; i++){
            //query[0]: sleep breaks

            Period sleepBreak = new Period(wakeUpMs[i-1],bedTimeMs[i]);
            if(sleepBreak.getHours() <= 2 && sleepBreak.getDays() == 0) {
                if(query[0] == 0) query[0]++;
                query[0]++;
            }

            //query[5]: sleep schedule
            if((durations[i-1] <= 90) || (durations[i] <= 90)) continue;
            Period schedule = new Period(bedTimeMs[i-1], bedTimeMs[i]);
            int slept = schedule.getHours();
            if((slept >= 1 && schedule.getDays() > 0) || (slept > 2 && slept <= 22)) {
                if(query[5] == 0) query[5]++;
                query[5]++;
            }
        }

        int stressSum = 0;
        int totalSleep = 0;
        for(int i = 0; i < sleepCount; i++){
            //query[1]: screen exposure
            Calendar start = Calendar.getInstance(), end = Calendar.getInstance();
            start.setTimeInMillis(bedTimeMs[i]);
            end.setTimeInMillis(wakeUpMs[i]);
            if (ScreenExposure.isUsingPhoneHourBefore(start, end, context)) {
                if(query[1] == 0) query[1]++;
                query[1]++;
            }

            //query[2]: extreme lack
            totalSleep += durations[i];

            //query[4]: naps
            if(durations[i] >= 30 && durations[i] <= 90) {
                if(query[4] == 0) query[4]++;
                query[4]++;
            }

            //query[6]: stress
            stressSum += stresses[i];
            if(stresses[i] >= 8) {
                if(query[6] == 0) query[6]++;
                query[6]++;
            }
        }
        //query[6]
        if((double)(stressSum) / sleepCount > 6) query[6]++;

        //query[2]
        Calendar today = Calendar.getInstance();
        Period days = new Period(bedTimeMs[0], today.getTimeInMillis(), PeriodType.days());
        int minutesNeeded = (hours * 60 + minutes) * days.getDays() / 4;
        if(totalSleep < minutesNeeded) query[2] = 5;

        return query;
    }



}
