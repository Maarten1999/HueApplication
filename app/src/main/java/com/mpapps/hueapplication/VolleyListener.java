package com.mpapps.hueapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface VolleyListener
{
    void GetLightsReceived(List<HueLight> lights);
    void PutLightsReceived(JSONArray response);

}
