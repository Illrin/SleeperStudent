package com.example.sleeperstudent;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/*
    Andrew has to implement in this class. Please read the comments on what these function
    need.
        1. getLastFiveBedTimes()
        2. getlastFiveWakeUpTimes()


 */
public class Sleep
{
    private final int CONSTANT_DAYS_NEEDED = 5;
    public int hours;
    public int minutes;


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

    public Sleep(int age){
        this.hours = getMinSleep(age);
        this.minutes = 0;
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


    /************************************************************************************
     *        Function: sugguestTime
     *  Post Condition: returns a Calendar object that contains the date and time the user
     *                  should sleep.
     ************************************************************************************/
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
     *                  TO BED for the last 5 PREVIOUS days that ARE NOT NAPS.
     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
     *                  time. In other words, the long contains date, and time.
     *
     *  you'll need to do this andrew since this requires database calls
     ************************************************************************************/
    private long[] getLastFiveBedTimes()
    {
        ArrayList<Long> startDates=new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Long> sdates=userentry.getStartDate();
                for(int i=0;i<sdates.size();i++){
                    startDates.add(sdates.get(i));
                }
            }
        });

        //startDates = arraylist of all longs of startingDate of sleep times
        return null;
    }


    /*************************************************************************************
     *        Function: getlastFiveWakeUpTimes()
     *  Post Condition: returns an array of type long containing the times the user WOKE UP
     *                  for the 5 PREVIOUS days that ARE NOT NAPS.
     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
     *                  time. In other words, the long contains date, and time.
     *
     *  you'll need to do this andrew since this requires database calls
     ************************************************************************************/
    private long[] getlastFiveWakeUpTimes()
    {
        ArrayList<Long> endDates=new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        DbUserInfo userentry=realm.where(DbUserInfo.class).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmList<Long> edates=userentry.getEndDate();
                for(int i=0;i<edates.size();i++){
                    endDates.add(edates.get(i));
                }
            }
        });

        //endDates = arraylist of all the ending date long times of sleep times
        return null;
    }


    private long minutesSlept(long start, long end)
    {
        return (end - start) / (1000 * 60);
    }


    private boolean checkTimes(long sleepTimes[], long wakeTimes[])
    {

        ArrayList<Integer> sameDay = new ArrayList<>();
        ArrayList<Integer> diffDay = new ArrayList<>();
        ArrayList<Integer> sameWakeUpDays = new ArrayList<>();
        ArrayList<Integer> diffWakeUpDays = new ArrayList<>();

        long earliest = 0;
        long latest = 0;

        Calendar sleepStart = Calendar.getInstance();
        Calendar sleepEnd = Calendar.getInstance();

        sleepStart.set(Calendar.SECOND, 0);
        sleepStart.set(Calendar.MILLISECOND, 0);
        sleepEnd.set(Calendar.SECOND, 0);
        sleepEnd.set(Calendar.MILLISECOND, 0);

        for(int i = 0; i < CONSTANT_DAYS_NEEDED; ++i)
        {
            sleepStart.setTimeInMillis(sleepTimes[i]);
            sleepEnd.setTimeInMillis(wakeTimes[i]);

            if(sleepStart.get(Calendar.DAY_OF_YEAR) != sleepEnd.get(Calendar.DAY_OF_YEAR))
            {
                diffWakeUpDays.add( (sleepEnd.get(Calendar.HOUR_OF_DAY) * 60) + sleepEnd.get(Calendar.MINUTE));
                diffDay.add(((sleepStart.get(Calendar.HOUR_OF_DAY) * 60) + (sleepStart.get(Calendar.MINUTE))));
            }
            else
            {
                sameWakeUpDays.add( (sleepEnd.get(Calendar.HOUR_OF_DAY) * 60) + sleepEnd.get(Calendar.MINUTE));
                sameDay.add(((sleepStart.get(Calendar.HOUR_OF_DAY) * 60) + (sleepStart.get(Calendar.MINUTE))));
            }
        }



        if(!sameDay.isEmpty() && !diffDay.isEmpty())
        {
            earliest = Collections.min(diffDay);
            latest = Collections.max(sameDay);
        }
        else
        {
            if(!sameDay.isEmpty())
            {
                earliest = Collections.min(sameDay);
                latest = Collections.max(sameDay);
            }
            else
            {
                earliest = Collections.min(diffDay);
                latest = Collections.max(diffDay);
            }
        }

        Calendar temp1 = Calendar.getInstance();
        Calendar temp2 = Calendar.getInstance();

        temp1.set(Calendar.SECOND, 0);
        temp1.set(Calendar.MILLISECOND, 0);
        temp2.set(Calendar.SECOND, 0);
        temp2.set(Calendar.MILLISECOND, 0);

        temp1.set(Calendar.HOUR_OF_DAY, (int)(earliest / 60) );
        temp1.set(Calendar.MINUTE, (int)(earliest % 60) );

        temp2.set(Calendar.HOUR_OF_DAY, (int)(latest / 60) );
        temp2.set(Calendar.MINUTE, (int)(latest % 60) );

        if(!sameDay.isEmpty() && !diffDay.isEmpty())
            temp2.add(Calendar.DAY_OF_YEAR, 1);

        temp1.add(Calendar.MINUTE, 30);
        return (temp1.getTimeInMillis() - temp2.getTimeInMillis() >= 0);
    }




    /*************************************************************************************
     *        Function: isConsistent
     *  Post Condition: Checks whether or not the user's sleep is consistent or not.
     *
     *   RETURN VALUES:
     *                  -1: System does not have enough info to determine how much sleep
     *                      user needs. I don't have 5 days of sleep info.
     *                   0: User deviated too much from the rec'd sleep time...cannot
     *                      determine if their sleep schedule. Too erratic!
     *                   1: The user's sleep schedule is consistent, and follows sleep rec'd
     *                      hours. You can ask if the sleep rec works.
     *                   2: User's sleep schedule is too random. They might follow the
     *                      rec'd # of hours, but they sleep at random times.
     *
     *           Note:  It is assumed that each entry is the # of seconds since unix epoch
     *                  time. In other words, the long contains date, and time.
     ************************************************************************************/
    public int isConsistent()
    {
        long bedTimes[] = getLastFiveBedTimes();
        long wakeTimes[] = getlastFiveWakeUpTimes();

        Integer startTimes[] = new Integer[CONSTANT_DAYS_NEEDED];
        Integer endTimes[] = new Integer[CONSTANT_DAYS_NEEDED];

        if(bedTimes == null || wakeTimes == null) return -1;

        if(bedTimes.length < 5 || wakeTimes.length < 5)
            return -1;

        long minutesSugguested = (hours * 60) + minutes;

        long maxVar = minutesSugguested + 30;
        long minVar = minutesSugguested - 30;

        for(int i = 0; i < CONSTANT_DAYS_NEEDED; ++i)
        {
            long sleepTime = minutesSlept(bedTimes[i], wakeTimes[i]);
            if(sleepTime > maxVar || sleepTime < minVar)
                return 0;

        }

        if(checkTimes(bedTimes, wakeTimes))
            return 1;
        return 2;
    }

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
