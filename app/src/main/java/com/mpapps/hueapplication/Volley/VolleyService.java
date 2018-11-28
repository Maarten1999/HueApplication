package com.mpapps.hueapplication.Volley;

import android.content.Context;

import android.graphics.Color;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VolleyService
{
    private static VolleyService sInstance = null;
    private RequestQueue requestQueue;
    private VolleyListener listener;
    public static final String basicRequestUrlMaartenHome = "http://192.168.178.38:80/api/3a6a380415175c7acbe40b95b25c104";
    public static final String basicRequestUrlMaartenSchool = "http://145.49.21.167:80/api/93e934e1ac5531c48ebf7838af52e94";


    private VolleyService (Context context, VolleyListener listener){
        requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public static VolleyService getInstance(Context context, VolleyListener listener){
        if (sInstance == null){
            sInstance = new VolleyService(context, listener);
        }
        return sInstance;
    }

    public void changeRequest(String requestUrl, final JSONObject requestBody, int method)
    {
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                method, requestUrl, requestBody,
                response -> listener.ChangeRequestReceived(response),
                error ->
                {
                    Log.i("VolleyService", "Volley error");
                }
        );
        requestQueue.add(request);
    }

    public void getRequest(String requestUrl, final JSONObject requestBody){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, requestBody,
                response ->
                {
                    List<HueLight> tempLights = new ArrayList<>();
                    for (int i = 0; i < response.names().length(); i++) {
                        try {
                            int id = Integer.parseInt(response.names().getString(i));
                            String name = response.getJSONObject(response.names().getString(i)).getString("name");
                            JSONObject lightJson = response.getJSONObject(response.names().getString(i)).getJSONObject("state");
                            boolean state = lightJson.getBoolean("on");
                            int saturation = lightJson.getInt("sat");
                            int brightness = lightJson.getInt("bri");
                            int hue = lightJson.getInt("hue");

                            HueLight light = new HueLight(id, name, state, brightness, hue, saturation);
                            tempLights.add(light);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    listener.GetLightsReceived(tempLights);
                },
                error -> Log.i("VolleyService", "Volley error")
        );

        requestQueue.add(request);
    }

    public void emptyRequestQueue(){
        requestQueue.cancelAll(request -> true);
    }

    public static String getUrl(Bridge bridge, VolleyType type, int lightId){
        String url = "http://" + bridge.getIP() + "/api";
        switch (type) {
            case GETLIGHTS:
                return url + "/" + bridge.getUsername() + "/lights";
            case PUTLIGHTS:
                return url + "/" + bridge.getUsername() + "/lights/" + lightId + "/state";
            case GETSCHEDULES:
                return url + "/" + bridge.getUsername() + "/schedules";
            case USERNAME:
                return url;
            default:
                return url + "/" + bridge.getUsername() + "/lights";

        }
    }
    public enum VolleyType{
        GETLIGHTS,
        PUTLIGHTS,
        GETSCHEDULES,
        USERNAME,
    }
}


