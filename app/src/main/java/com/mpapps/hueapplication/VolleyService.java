package com.mpapps.hueapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyService
{
    private static VolleyService sInstance = null;
    private Context context;
    private String requestResponse;
    private RequestQueue requestQueue;

    public VolleyService (Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyService getInstance(Context context){
        if (sInstance == null){
            sInstance = new VolleyService(context);
        }
        return sInstance;
    }

    public void doRequest(String requestUrl, final JSONObject requestBody, int requestMethod){
        JsonObjectRequest request = new JsonObjectRequest(
                requestMethod, requestUrl, requestBody,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

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
}
