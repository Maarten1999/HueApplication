package com.mpapps.hueapplication.Models;

import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

public class Bridge implements Parcelable
{
    private String Name;
    private String IP;
    private String Username;

    public Bridge(String name, String IP)
    {
        Name = name;
        this.IP = IP;
    }

    public Bridge(String name, String IP, String username)
    {
        Name = name;
        this.IP = IP;
        Username = username;
    }

    protected Bridge(Parcel in)
    {
        Name = in.readString();
        IP = in.readString();
        Username = in.readString();
    }

    public static final Creator<Bridge> CREATOR = new Creator<Bridge>()
    {
        @Override
        public Bridge createFromParcel(Parcel in)
        {
            return new Bridge(in);
        }

        @Override
        public Bridge[] newArray(int size)
        {
            return new Bridge[size];
        }
    };


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

    public String getUsername()
    {
        return Username;
    }

    public void setUsername(String username)
    {
        Username = username;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(Name);
        dest.writeString(IP);
        dest.writeString(Username);
    }
}
