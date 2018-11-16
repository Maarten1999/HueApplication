package com.mpapps.hueapplication;

public class HueLight
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

}
