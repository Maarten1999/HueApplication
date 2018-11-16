package com.mpapps.hueapplication.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mpapps.hueapplication.Models.HueLight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VolleyService
{
    private static VolleyService sInstance = null;
    private Context context;
    private String requestResponse;
    private RequestQueue requestQueue;
    private VolleyListener listener;
    public static final String basicRequestUrlMaartenHome = "http://192.168.178.38:80/api/93e934e1ac5531c48ebf7838af52e94";

    private VolleyService (Context context, VolleyListener listener){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
    }

    public static VolleyService getInstance(Context context, VolleyListener listener){
        if (sInstance == null){
            sInstance = new VolleyService(context, listener);
        }
        return sInstance;
    }

    public void putRequest(String requestUrl, final JSONObject requestBody)
    {
        CustomJsonArrayRequest request = new CustomJsonArrayRequest(
                Request.Method.PUT, requestUrl, requestBody,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        listener.PutLightsReceived(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );
        requestQueue.add(request);
    }

    public void getRequest(String requestUrl, final JSONObject requestBody){
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, requestUrl, requestBody,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        List<HueLight> tempLights = new ArrayList<>();
                        for (int i = 0; i < response.names().length(); i++) {
                            try {
                                int id = Integer.parseInt(response.names().getString(i));
                                JSONObject lightJson = response.getJSONObject(response.names().getString(i)).getJSONObject("state");
                                boolean state = lightJson.getBoolean("on");
                                int saturation = lightJson.getInt("sat");
                                int brightness = lightJson.getInt("bri");
                                int hue = lightJson.getInt("hue");
                                HueLight light = new HueLight(id, state, brightness, hue, saturation);
                                tempLights.add(light);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        listener.GetLightsReceived(tempLights);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("VolleyService", "Volley error");
                    }
                }
        );

        requestQueue.add(request);
    }

}
