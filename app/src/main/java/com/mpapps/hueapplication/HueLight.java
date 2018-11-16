package com.mpapps.hueapplication;

public class HueLight
{
    private boolean state;
    private int brightness;
    private int hue;
    private int saturation;

    public HueLight(boolean state, int brightness, int hue, int saturation)
    {
        this.state = state;
        this.brightness = brightness;
        this.hue = hue;
        this.saturation = saturation;
    }

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
}
