package com.example.sleeperstudent;

import android.app.usage.EventStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/*************************************************************************************
 *         You do not need to create an object to use any functions, just go
 *         ScreenExposure.calculateScreenTime(.....);
 *
 *         IMPORTANT:
 *         - TO USE THESE CLASS'S FUNCTIONS, YOU HAVE TO GIVE IT EXPANDED PRIVLEDGES:
 *           GO to Settings > Security > Apps with usage access.
 ************************************************************************************/
public class ScreenExposure
{

    /*************************************************************************************
     *        Function: createCalendars
     *  Post Condition: Initializes calendars bedTime and wakeTime to the time given by
     *                  the other parameters
     ************************************************************************************/
    private static void createCalendars(int startHour, int startMinute, int endHour, int endMinute, Calendar bedTime, Calendar wakeTime)
    {
        bedTime.set(Calendar.SECOND, 0);
        wakeTime.set(Calendar.SECOND, 0);

        if(startHour > endHour)
            bedTime.add(Calendar.DATE, -1);

        bedTime.set(Calendar.HOUR_OF_DAY, startHour);
        bedTime.set(Calendar.MINUTE, startMinute);
        wakeTime.set(Calendar.HOUR_OF_DAY, endHour);
        wakeTime.set(Calendar.MINUTE, endMinute);

    }


    /*************************************************************************************
     *        Function: isUsingPhoneHourBefore
     *       Variables: Context: Application contextual info
     *                  startHour & startMinute: starting time frame
     *                  endHour   & endMinute: ending time frame
     *  Post Condition: Retrieves whether or not the user is using the device within
     *                  the hour before bedtime.
     ************************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static boolean isUsingPhoneHourBefore(int startHour, int startMinute, int endHour, int endMinute, Context context)
    {
        Calendar bedTime = Calendar.getInstance();
        Calendar wakeTime = Calendar.getInstance();
        createCalendars(startHour, startMinute, endHour, endMinute, bedTime, wakeTime);

        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents tester =  usageStatsManager.queryEvents(bedTime.getTimeInMillis(), wakeTime.getTimeInMillis());

        UsageEvents.Event evento = new UsageEvents.Event();

        boolean stop = tester.hasNextEvent();
        while(stop)
        {
            tester.getNextEvent(evento);
            stop = tester.hasNextEvent();
            if(evento.getEventType() == UsageEvents.Event.SCREEN_INTERACTIVE)
            {
                double timeDiff = ((bedTime.getTimeInMillis() - evento.getTimeStamp())/1000. )/60.;
                if(timeDiff <= 60.)
                    return true;
            }
        }
        return false;


    }


            /* TO DOS */
    // also need to calculate NEW YEARS
    // check if it handles 23:59
    /*************************************************************************************
     *        Function: calculateScreenTime
     *       Variables: Context: Application contextual info
     *                  startHour & startMinute: starting time frame
     *                  endHour   & endMinute: ending time frame
     *  Post Condition: Retrieves the amount of time the user has spent using the device
     *                  from startHour & startMinute to endHour & endMinute.
     *
     ************************************************************************************/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static double calculateScreenTime(int startHour, int startMinute, int endHour, int endMinute, Context context)
    {
        Calendar bedTime = Calendar.getInstance();
        Calendar wakeTime = Calendar.getInstance();
        createCalendars(startHour, startMinute, endHour, endMinute, bedTime, wakeTime);

        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents tester =  usageStatsManager.queryEvents(bedTime.getTimeInMillis(), wakeTime.getTimeInMillis());


        UsageEvents.Event evento = new UsageEvents.Event();
        long timeStart = 0;
        double totalTime = 0;

        boolean stop = tester.hasNextEvent();
        while(stop)
        {
            tester.getNextEvent(evento);
            stop = tester.hasNextEvent();
            switch(evento.getEventType())
            {
                case UsageEvents.Event.SCREEN_INTERACTIVE:
                    timeStart = evento.getTimeStamp();
                    System.out.println("interaction event @ " + evento.getTimeStamp());
                    break;

                case UsageEvents.Event.DEVICE_STARTUP:
                    timeStart = evento.getTimeStamp();
                    break;
                case UsageEvents.Event.SCREEN_NON_INTERACTIVE:
                case UsageEvents.Event.DEVICE_SHUTDOWN:
                    if(timeStart == 0)
                        continue;
                    totalTime+= ((evento.getTimeStamp() - timeStart)/1000.)/60.;
                    break;
            }
        }
        return totalTime;

}
