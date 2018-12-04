package com.mpapps.hueapplication.Models;

import android.util.Log;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class ScheduleTime
{
    private String timeString;
    //private Date date;
    private DateTime dateTime;

    public ScheduleTime(String timeString)
    {
        this.timeString = timeString;
        String[] dateAndTime = timeString.split("T");
        String timeString2 = dateAndTime[0] + " " + dateAndTime[1];
        Log.i("ScheduleTime", timeString2);
        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        dateTime  = DateTime.parse(timeString, DateTimeFormat.forPattern(pattern));
        Log.i("ScheduleTime1", dateTime.toString("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public ScheduleTime(int hour, int minute, LocalDate scheduleDate){

        dateTime = new DateTime()
                .withTime(hour, minute, 0,0)
                .withDate(scheduleDate);
        String tempTimeString = dateTime.toString("yyyy-MM-dd HH:mm:ss");
        String[] dateAndTime = tempTimeString.split(" ");
        timeString = dateAndTime[0] + "T" + dateAndTime[1];
        Log.i("ScheduleTime", timeString);
    }
    public String getTimeString()
    {
        return timeString;
    }

    public void setTimeString(String timeString)
    {
        this.timeString = timeString;
    }

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime)
    {
        this.dateTime = dateTime;
    }
}
