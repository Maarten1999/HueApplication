package com.mpapps.hueapplication.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class HueLight implements Parcelable
{
    private int id;
    private boolean state;
    private int brightness;
    private int hue;
    private int saturation;
    private double x;
    private double y;

    public HueLight(int id, boolean state, int brightness, int hue, int saturation)
    {
        this.id = id;
        this.state = state;
        this.brightness = brightness;
        this.hue = hue;
        this.saturation = saturation;
    }

    protected HueLight(Parcel in)
    {
        id = in.readInt();
        state = in.readByte() != 0;
        brightness = in.readInt();
        hue = in.readInt();
        saturation = in.readInt();
        x = in.readDouble();
        y = in.readDouble();
    }

    public static final Creator<HueLight> CREATOR = new Creator<HueLight>()
    {
        @Override
        public HueLight createFromParcel(Parcel in)
        {
            return new HueLight(in);
        }

        @Override
        public HueLight[] newArray(int size)
        {
            return new HueLight[size];
        }
    };

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public int getBrightness()
    {
        return brightness;
    }

    public void setBrightness(int brightness)
    {
        this.brightness = brightness;
    }

    public int getHue()
    {
        return hue;
    }

    public void setHue(int hue)
    {
        this.hue = hue;
    }

    public int getSaturation()
    {
        return saturation;
    }

    public void setSaturation(int saturation)
    {
        this.saturation = saturation;
    }
    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(id);
        dest.writeByte((byte) (state ? 1 : 0));
        dest.writeInt(brightness);
        dest.writeInt(hue);
        dest.writeInt(saturation);
        dest.writeDouble(x);
        dest.writeDouble(y);
    }
}
