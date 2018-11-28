package com.mpapps.hueapplication;

import com.mpapps.hueapplication.Models.HueLight;

import java.util.ArrayList;
import java.util.List;

public class LightManager
{
    private List<HueLight> lights;
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
