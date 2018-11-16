package com.mpapps.hueapplication;

import com.mpapps.hueapplication.Models.HueLight;

import java.util.ArrayList;
import java.util.List;

public class LightManager
{
    private List<HueLight> lights;

    public LightManager()
    {
        this.lights = new ArrayList<>();
    }

    public List<HueLight> getLights()
    {
        return lights;
    }

    public void setLights(List<HueLight> lights)
    {
        this.lights = lights;
    }

}
