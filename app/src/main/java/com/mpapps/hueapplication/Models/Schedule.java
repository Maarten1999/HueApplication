package com.mpapps.hueapplication.Models;

import java.sql.Time;
import java.time.LocalTime;

public class Schedule
{
    private String name;
    private int id;
    private ScheduleTime localTime;
    private String address;
    private boolean isOn;

    public Schedule(String name, int id, ScheduleTime localTime, String address, boolean isOn)
    {
        this.name = name;
        this.id = id;
        this.localTime = localTime;
        this.address = address;
        this.isOn = isOn;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public ScheduleTime getLocalTime()
    {
        return localTime;
    }

    public void setLocalTime(ScheduleTime localTime)
    {
        this.localTime = localTime;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public boolean isOn()
    {
        return isOn;
    }

    public void setOn(boolean on)
    {
        isOn = on;
    }
}
