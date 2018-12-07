package com.mpapps.hueapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Group implements Parcelable
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
        this.groupState = groupState;
    }

    protected Group(Parcel in)
    {
        id = in.readInt();
        name = in.readString();
        lightIds = in.createIntArray();
        groupState = in.readParcelable(HueLight.class.getClassLoader());
    }

    public static final Creator<Group> CREATOR = new Creator<Group>()
    {
        @Override
        public Group createFromParcel(Parcel in)
        {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size)
        {
            return new Group[size];
        }
    };

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

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeIntArray(lightIds);
        dest.writeParcelable(groupState, flags);
    }
}
