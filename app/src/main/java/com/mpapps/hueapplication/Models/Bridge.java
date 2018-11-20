package com.mpapps.hueapplication.Models;

public class Bridge
{
    private int ID;
    private String Name;
    private String IP;

    public Bridge(int id, String name, String IP)
    {
        ID = id;
        Name = name;
        this.IP = IP;
    }

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getIP()
    {
        return IP;
    }

    public void setIP(String IP)
    {
        this.IP = IP;
    }
}
