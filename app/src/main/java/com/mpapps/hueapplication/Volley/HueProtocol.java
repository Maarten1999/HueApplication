package com.mpapps.hueapplication.Volley;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HueProtocol
{
    public static JSONObject getUsername(String appName){
        JSONObject json = new JSONObject();
        try {
            json.put("devicetype", appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String UsernameParse(JSONObject response)
    {
        try {
            return response.getJSONObject("success").getString("username");
        } catch (JSONException e) {
            return null;
        }
    }

    public static JSONObject setLight(boolean state, int brightness, int hue, int saturation, double x, double y)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("on", state);
            json.put("bri", brightness);
            json.put("hue", hue);
            json.put("sat", saturation);
            JSONArray xy = new JSONArray();
            xy.put(x);
            xy.put(y);
            json.put("xy", xy);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject setLight(boolean state, int brightness, int hue, int saturation)
    {
        JSONObject json = new JSONObject();
        try {
            json.put("on", state);
            json.put("bri", brightness);
            json.put("hue", hue);
            json.put("sat", saturation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
