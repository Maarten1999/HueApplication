package com.mpapps.hueapplication.Models;

import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;

public class ScheduleTime
{
    private String timeString;
    private Date date;

    public ScheduleTime(String timeString)
    {
        this.timeString = timeString;
        String[] dateAndTime = timeString.split("T");

        Time time = Time.valueOf(dateAndTime[1]);
        String[] date = dateAndTime[0].split("-");
        Date tempDate = new Date();
        tempDate.setTime(time.getTime());
        Log.i("HueProtocol", date[0] + "-" + date[1] + "-" + date[2]);
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        tempDate.setDate(day);
        tempDate.setMonth(month);
        tempDate.setYear(year);
        this.date = tempDate;
    }


    public String getTimeString()
    {
        return timeString;
    }

    public void setTimeString(String timeString)
    {
        this.timeString = timeString;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }
}
