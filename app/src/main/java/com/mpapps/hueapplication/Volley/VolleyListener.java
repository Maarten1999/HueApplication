package com.mpapps.hueapplication.Volley;

import com.mpapps.hueapplication.Models.HueLight;

import org.json.JSONArray;

import java.util.List;

public interface VolleyListener
{
    void GetLightsReceived(List<HueLight> lights);
    void ChangeRequestReceived(JSONArray response);

}
