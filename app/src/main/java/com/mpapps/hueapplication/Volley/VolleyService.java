package com.mpapps.hueapplication.Volley;

import android.content.Context;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VolleyService
{
    private static VolleyService sInstance = null;
    private RequestQueue requestQueue;
    private VolleyListener listener;
//    public static final String basicRequestUrlMaartenHome = "http://192.168.178.38:80/api/3a6a380415175c7acbe40b95b25c104";
//    public static final String basicRequestUrlMaartenSchool = "http://145.49.21.167:80/api/93e934e1ac5531c48ebf7838af52e94";


    private VolleyService (Context context, VolleyListener listener){
        requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    private void DetachListener(){
        listener = null;
    }
    public static void Detach(){
        //sInstance.DetachListener();
        sInstance = null;
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
                    error.printStackTrace();
                    Log.i("VolleyService", "Volley error");
                }
        );
        requestQueue.add(request);
    }

    public void getRequest(String requestUrl, final JSONObject requestBody, VolleyGetType type){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, requestBody,
                response ->
                {
                    switch (type) {
                        case GROUPS:
                            listener.GetGroupsReceived(HueProtocol.GroupsParse(response));
                            break;
                        case LIGHTS:
                            listener.GetLightsReceived(HueProtocol.LightsParse(response));
                            break;
                        case SCHEDULES:
                            listener.GetSchedulesReceived(HueProtocol.SchedulesParse(response));
                            break;
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.i("VolleyService", "Volley error");
                }
        );

        requestQueue.add(request);
    }

    public void emptyRequestQueue(){
        requestQueue.cancelAll(request -> true);
    }

    public enum VolleyGetType{
        GROUPS,
        LIGHTS,
        SCHEDULES
    }
}


