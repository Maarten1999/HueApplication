package com.mpapps.hueapplication.Models;

public class Group
{
    private int id;
    private String name;
    private int[] lightIds;
    private HueLight groupState;

    public Group(int id, String name, int[] lightIds, HueLight groupState)
    {
        this.id = id;
        this.name = name;
        this.lightIds = lightIds;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int[] getLightIds()
    {
        return lightIds;
    }

    public void setLightIds(int[] lightIds)
    {
        this.lightIds = lightIds;
    }

    public HueLight getGroupState()
    {
        return groupState;
    }

    public void setGroupState(HueLight groupState)
    {
        this.groupState = groupState;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
