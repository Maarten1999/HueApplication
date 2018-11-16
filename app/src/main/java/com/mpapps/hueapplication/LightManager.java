package com.mpapps.hueapplication;

import org.json.JSONArray;
import org.json.JSONObject;

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
