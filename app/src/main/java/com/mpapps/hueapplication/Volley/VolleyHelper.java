package com.mpapps.hueapplication.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.mpapps.hueapplication.Models.Bridge;

public class VolleyHelper
{
    VolleyService volleyService;
    Bridge bridge;

    public VolleyHelper(Context context, VolleyListener listener, Bridge bridge)
    {
        volleyService = VolleyService.getInstance(context, listener);
        this.bridge = bridge;
    }

    public void getLightsRequest(){
        volleyService.getRequest(VolleyService.getUrl(bridge, VolleyService.VolleyType.GETLIGHTS, 0), null);
    }

    public void turnLightOnOff(int lightId, boolean isOn){
        volleyService.changeRequest(VolleyService.getUrl(bridge, VolleyService.VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(isOn), Request.Method.PUT);
    }

    public void setLightBrightness(int lightId, int bri){
        volleyService.changeRequest(VolleyService.getUrl(bridge, VolleyService.VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(bri), Request.Method.PUT);
    }

    public void setLight(int lightId, boolean isOn, int bri, int hue, int sat)
    {
        volleyService.changeRequest(VolleyService.getUrl(bridge, VolleyService.VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(isOn, bri, hue, sat), Request.Method.PUT);
    }

    public void getUsername(){
        volleyService.changeRequest("http://" +
                        bridge.getIP() + "/api",
                HueProtocol.getUsername("HueApplication"),
                Request.Method.POST);
    }
}
