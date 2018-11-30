package com.mpapps.hueapplication;

import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;

import java.util.ArrayList;
import java.util.List;

public class LightManager
{
    private List<HueLight> lights;
    private List<Schedule> schedules;
    private List<Group> groups;
    private static LightManager sInstance;

    public static LightManager getInstance(){
        if (sInstance == null){
            sInstance = new LightManager();
        }
        return sInstance;
    }

    private LightManager()
    {
        this.lights = new ArrayList<>();
        this.schedules = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public List<HueLight> getLights()
    {
        return lights;
    }

    public void setLights(List<HueLight> lights)
    {
        this.lights = lights;
    }

    public List<Schedule> getSchedules()
    {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules)
    {
        this.schedules = schedules;
    }

    public List<Group> getGroups()
    {
        return groups;
    }

    public void setGroups(List<Group> groups)
    {
        this.groups = groups;
    }
}


