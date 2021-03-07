package com.example.sleeperstudent;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Sleep
{
    private final int CONSTANT_DAYS_NEEDED = 5;
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

        double diff = sugguestTime(startHour, startMin, endHour, endMin);
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);

        if(maxAmt < hours)
            return ("We don't recommend " + hours + " hours and " + minutes + " minutes of sleep, it is too much sleep!");
        if(minAmt > hours)
            return ("We don't recommend " + hours + " hours and " + minutes + " minutes of sleep, it is not enough!");
        return "Sleep schedule may work, it falls between the minimum and maximum amt of sleep recommended for your age";


    }


    public double sugguestTime(int startHour, int startMin, int endHour, int endMin)
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





    /*************************************************************************************
     *        Function: calculateSleepTime
     *  Post Condition: returns a Calendar object that contains the date and time the user
     *                  should sleep.
     ************************************************************************************/

    public Calendar calculateSleepTime(int endHour, int endMin)
    {

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMin);
        endTime.set(Calendar.SECOND, 0);

        endTime.add(Calendar.HOUR_OF_DAY,  -1 * hours);
        endTime.add(Calendar.MINUTE, -1 * minutes);

        return endTime;

    }




    /*************************************************************************************
     *        Function: getLastFiveBedTimes()
     *  Post Condition: returns an array of type long containing the times the user WENT
     *                  TO BED for the last 5 PREVIOUS days.
     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
     *                  time. In other words, the long contains date, and time.
     *
     *  you'll need to do this andrew since this requires database calls
     ************************************************************************************/
    private long[] getLastFiveBedTimes()
    {
        return null;
    }


    /*************************************************************************************
     *        Function: getlastFiveWakeUpTimes()
     *  Post Condition: returns an array of type long containing the times the user WOKE UP
     *                  for the 5 PREVIOUS days.
     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
     *                  time. In other words, the long contains date, and time.
     *
     *  you'll need to do this andrew since this requires database calls
     ************************************************************************************/
    private long[] getlastFiveWakeUpTimes()
    {
        return null;
    }


    private long minutesSlept(long start, long end)
    {
        return (end - start) * 1000 * 60;
    }





//    /*************************************************************************************
//     *        Function: isConsistent
//     *  Post Condition: Checks whether or not the
//     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
//     *                  time. In other words, the long contains date, and time.
//     *
//     *  you'll need to do this andrew since this requires database calls
//     ************************************************************************************/
//    private int isConsistent()
//    {
//        long bedTimes[] = getLastFiveBedTimes();
//        long wakeTimes[] = getlastFiveWakeUpTimes();
//
//        Integer startTimes[] = new Integer[CONSTANT_DAYS_NEEDED];
//        Integer endTimes[] = new Integer[CONSTANT_DAYS_NEEDED];
//
//        if(bedTimes.length < 5 || wakeTimes.length < 5)
//            return -2;
//
//        long minutesSugguested = (hours * 60) + minutes;
//        long maxVar = minutesSugguested + 30;
//        long minVar = minutesSugguested - 30;
//        Calendar calendar = Calendar.getInstance();
//
//        for(int i = 0; i < CONSTANT_DAYS_NEEDED; ++i)
//        {
//            long sleepTime = minutesSlept(bedTimes[i], wakeTimes[i]);
//            if(sleepTime > maxVar || sleepTime < minVar)
//                return -1;
//
//            calendar.setTimeInMillis(bedTimes[i]);
//            startTimes[i] = new Integer(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));
//
//            calendar.setTimeInMillis(wakeTimes[i]);
//            endTimes[i] = new Integer(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));
//        }
//
//
//        boolean wrappedAround = false;
//        boolean nonWrapedVals = false;
//
//        ArrayList<Integer> normalTimes = new ArrayList<>();
//        ArrayList<Integer> weirdTimes = new ArrayList<>();
//        long earliest = 0;
//        long latest = 0;
//
//        for(int i = 0; i < CONSTANT_DAYS_NEEDED; ++i)
//        {
//            if((startTimes[i]/60)/10 == 2 || (startTimes[i]/60)/10 == 1)
//            {
//                wrappedAround = true;
//                normalTimes.add(startTimes[i]);
//            }
//            else
//            {
//                nonWrapedVals = true;
//                weirdTimes.add(startTimes[i]);
//            }
//        }
//        if(wrappedAround)
//            Collections.sort(normalTimes);
//        if(nonWrapedVals)
//            Collections.sort(weirdTimes);
//
//        if(wrappedAround && nonWrapedVals)
//        {
//            earliest= Collections.min(normalTimes);
//            latest = Collections.max(weirdTimes);
//        }
//        else
//        {
//            //person is a night owl
//            if(wrappedAround)
//            {
//                earliest = Collections.min(weirdTimes);
//                latest = Collections.max(weirdTimes);
//            }
//            else
//            {
//                earliest = Collections.min(normalTimes);
//                latest = Collections.max(normalTimes);
//            }
//        }
//
//        Calendar earliestSleep = Calendar.getInstance();
//        Calendar latestSleep = Calendar.getInstance();
//
//        earliestSleep.set(Calendar.SECOND, 0);
//        earliestSleep.set(Calendar.MILLISECOND, 0);
//
//        latestSleep.set(Calendar.SECOND, 0);
//        latestSleep.set(Calendar.MILLISECOND, 0);
//
//        Calendar.set(Calendar.HOUR_OF_DAY, earliest / 60);
//
//    }

    /*************************************************************************************
     *        Function: recalibrateSleep
     *  Post Condition: Tries to recalibrate the amt of sleep the user needs. Returns true
     *                  if sucuessful, adding 30 minutes to the amount of sleep they need.
     *                  Returns false otherwise.
     *           Note:  If this function returns false, it is highly unlikely that their
     *                  sleep quantity is the cause of poor sleep. Their circadian ryhthm
     *                  makes them either night owls, or early risers. The user needs to
     *                  re-adjust their wake up time if possible. You should call this
     *                  every 5 days...
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
