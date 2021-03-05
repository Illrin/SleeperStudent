package com.example.sleeperstudent;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
    public User(){
        age = 0;
        userName = "User";
        realName = "Name";
        weight = 0;
        height = 0;
        wakeups = new int[7];
        for(int i = 0; i < 7; i++){
            wakeups[i] = 1;
        }
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

            out.write(printName.getBytes());
            out.write(printAge.getBytes());
            out.write(printHeight.getBytes());
            out.write(printWeight.getBytes());
            for(int i = 0; i < 7; i++){
                out.write(printWakeup[i].getBytes());
            }
            out.write(printReal.getBytes());

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
     *        Function: BuildQuery
     *  Post Condition: Returns a vector that can be compared to Tip tag vectors
     ************************************************************************************/
    public Integer[] buildQuery(){
        //stub for now
        Integer[] query = {0,0,0,0,0,0,0,1};
        if(age > 21) query[3] = 1;
        /*  Get current time/day
        *   Get all sleep periods within the last 72 hours
        *   If there isn't a single one within the last 72 hours, return an array of a single 0 ([0])
        *       For now just assume that they never inputted their data, and not that they're insane
        *       just gonna say it's an arraylist of objects for now, sleepPeriods
        *   Sleep Breaks
        *       for all except last period,
        *           check if distance between current period end and next period start are < 1 hour apart
        *               increment query[0] every time
        *   Screen Time
        *       for all periods,
        *           call ScreenExposure's calculateScreenTime from 30min before startTime to startTime
        *           sum up
        *           query[1] = sum / 15? maybe like 20 or 30
        *   Extreme Lack
        *       sum up total sleep time, divide by 3 for average
        *       if < default recc sleeping time / 2? 3? 4?, set query[2] to 5
        *   Long Naps
        *       for all periods,
        *           check if sleep amount is > 30 min and < 2 hrs? 3 hrs?
        *           increment query[4] for each one
        *   Sleep Schedule
        *       for all periods except first and last
        *           skip if nap (see above)
        *           if diff in sleep amount between neighbors > 1 hour, increment query[5]
        *   Stress
        *       get average stress, increment query[6] if > 6
        *       for all periods, increment query[6] if stress >= 8
        *
        * */
        return query;
    }



}
