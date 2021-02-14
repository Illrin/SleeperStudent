package com.example.sleeperstudent;

import java.util.Calendar;

public class Sleep
{
    private int hours;
    private int minutes;

    public Sleep()
    {
        this.hours = 7;
        this.minutes = 30;
    }

    public Sleep(int hours, int minutes)
    {
        this.hours = hours;
        this.minutes = minutes;
    }



    public String isUserSleepRec(int startHour, int startMin, int endHour, int endMin, int age)
    {
        int maxAmt = getMaxSleep(age);
        int minAmt = getMinSleep(age);

        double diff = calculateSleepTime(startHour, startMin, endHour, endMin);
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);

        if(maxAmt >= hours)
            return ("We don't recommend " + hours + " hours and " + minutes + " minutes of sleep, it is too much sleep!");
        if(minAmt > hours)
            return ("We don't recommend " + hours + " hours and " + minutes + " minutes of sleep, it is not enough!");
        return "not finished atm";


    }

    private double calculateSleepTime(int startHour, int startMin, int endHour, int endMin)
    {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMin);
        startTime.set(Calendar.SECOND, 0);


        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMin);
        endTime.set(Calendar.SECOND, 0);


        return (endTime.getTimeInMillis() -  startTime.getTimeInMillis());
    }

    public Calendar getSleepRec(int hour, int min)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 30);

        calendar.add(Calendar.HOUR_OF_DAY, -7);
        calendar.add(Calendar.MINUTE, -30);

        return calendar;
    }

    /*************************************************************************************
     *        Function: recalibrateSleep
     *  Post Condition: Tries to recalibrate the amt of sleep the user needs. Returns true
     *                  if sucuessful, adding 30 minutes to the amount of sleep they need.
     *                  Returns false otherwise.
     *           Note:  If this function returns false, it is highly unlikely that their
     *                  sleep quantity is the cause of poor sleep. Their circadian ryhthm
     *                  makes them either night owls, or early risers. The user needs to
     *                  re-adjust their wake up time if possible.
     *
     ************************************************************************************/
    public boolean recalibrateSleep(int age)
    {
        if(hours == getMaxSleep(age))
            return false;
        minutes+=30;
        if(minutes == 60)
        {
            minutes = 0;
            hours+=1;
        }
        return true;
    }

    private int getMaxSleep(int age)
    {
        if(age <= 2) return 14;
        if(age <= 5) return 13;
        if(age <= 13) return 11;
        if(age <= 17) return 10;
        if(age <= 64) return 9;
        return 8;
    }

    private int getMinSleep(int age)
    {
        if(age <= 2) return 11;
        if(age <= 5) return 10;
        if(age <= 13) return 9;
        if(age <= 17) return 8;
        return 7;
    }
}
