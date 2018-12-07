package com.mpapps.hueapplication.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.Models.ScheduleTime;

import org.json.JSONArray;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public class VolleyHelper implements VolleyListener
{
    VolleyService volleyService;
    Bridge bridge;
    Notifier listener;
    private static VolleyHelper sInstance;

    public static VolleyHelper getInstance(Context context, Notifier notifier, Bridge bridge){
        if (sInstance == null){
            sInstance = new VolleyHelper(context, notifier, bridge);
        }
        return sInstance;
    }

    private VolleyHelper(Context context, Notifier notifier, Bridge bridge)
    {
        listener = notifier;
        volleyService = VolleyService.getInstance(context, this);
        this.bridge = bridge;
    }

    public void Detach(){
        VolleyService.Detach();
        sInstance = null;
    }
    public void getLightsRequest(){
        volleyService.getRequest(getUrl(bridge, VolleyType.GETLIGHTS, 0), null, VolleyService.VolleyGetType.LIGHTS);
    }

    public void setListener(Notifier notifier){
        listener = notifier;
    }

    public void turnLightOnOff(int lightId, boolean isOn){
        volleyService.changeRequest(getUrl(bridge, VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(isOn), Request.Method.PUT);
    }

    public void setLightBrightness(int lightId, int bri){
        volleyService.changeRequest(getUrl(bridge, VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(bri), Request.Method.PUT);
    }

    public void setLight(int lightId, boolean isOn, int bri, int hue, int sat)
    {
        volleyService.changeRequest(getUrl(bridge, VolleyType.PUTLIGHTS, lightId), HueProtocol.setLight(isOn, bri, hue, sat), Request.Method.PUT);
    }

    public void getUsername(){
        volleyService.changeRequest("http://" +
                        bridge.getIP() + "/api",
                HueProtocol.getUsername("HueApplication"),
                Request.Method.POST);
    }

    public void getGroupsRequest()
    {
        volleyService.getRequest(getUrl(bridge, VolleyType.GETorADDGROUPS, 0), null, VolleyService.VolleyGetType.GROUPS);
    }

    public void addGroupRequest(String name, int[] lightIds){
        volleyService.changeRequest(getUrl(bridge, VolleyType.GETorADDGROUPS, 0), HueProtocol.addOrChangeGroup(name, lightIds), Request.Method.POST);
    }

    public void setGroupRequest(int groupId, String name, int[] lightIds){
        volleyService.changeRequest(getUrl(bridge, VolleyType.EDITGROUP, groupId), HueProtocol.addOrChangeGroup(name, lightIds), Request.Method.PUT);
    }

    public void setGroupOnOff(int groupId, boolean isOn){
        volleyService.changeRequest(getUrl(bridge, VolleyType.SETGROUPSTATE, groupId), HueProtocol.setLight( isOn), Request.Method.PUT);
    }

    public void setGroupBrightness(int groupId, int bri){
        volleyService.changeRequest(getUrl(bridge, VolleyType.SETGROUPSTATE, groupId), HueProtocol.setLight( bri), Request.Method.PUT);
    }
    public void setGroupState(int groupId, boolean isOn, int bri, int hue, int sat){
        volleyService.changeRequest(getUrl(bridge, VolleyType.SETGROUPSTATE, groupId), HueProtocol.setLight(isOn, bri, hue, sat), Request.Method.PUT);
    }

    public void deleteGroup(int groupId){
        volleyService.changeRequest(getUrl(bridge, VolleyType.EDITGROUP, groupId), null, Request.Method.DELETE);

    }

    public void getSchedulesRequest()
    {
        volleyService.getRequest(getUrl(bridge, VolleyType.GETorADDSCHEDULES, 0), null, VolleyService.VolleyGetType.SCHEDULES);
    }

    public void addScheduleRequest(boolean isGroup, int id, String name, ScheduleTime time, boolean isOn, int brightness, int hue, int saturation){
        String address;
        if(isGroup)
            address = getUrlWithoutIP(bridge, VolleyType.SETGROUPSTATE, id);
        else
            address = getUrlWithoutIP(bridge, VolleyType.PUTLIGHTS, id);
        volleyService.changeRequest(getUrl(bridge, VolleyType.GETorADDSCHEDULES, 0),
                HueProtocol.addSchedule(name, time, address, isOn, brightness, hue, saturation), Request.Method.POST);
    }

    public void addScheduleRequest(boolean isGroup, int id, String name, ScheduleTime time, boolean isOn){
        String address;
        if(isGroup)
            address = getUrlWithoutIP(bridge, VolleyType.SETGROUPSTATE, id);
        else
            address = getUrlWithoutIP(bridge, VolleyType.PUTLIGHTS, id);
        volleyService.changeRequest(getUrl(bridge, VolleyType.GETorADDSCHEDULES, 0),
                HueProtocol.addSchedule(name, time, address, isOn), Request.Method.POST);
    }

    public void deleteSchedule(int scheduleId){
        volleyService.changeRequest(getUrl(bridge, VolleyType.DELETESCHEDULE, scheduleId), null, Request.Method.DELETE);

    }
    public static String getUrl(Bridge bridge, VolleyType type, int id){
        String url = "http://" + bridge.getIP() + "/api";
        switch (type) {
            case GETLIGHTS:
                return url + "/" + bridge.getUsername() + "/lights";
            case PUTLIGHTS:
                return url + "/" + bridge.getUsername() + "/lights/" + id + "/state";
            case GETorADDSCHEDULES:
                return url + "/" + bridge.getUsername() + "/schedules";
            case DELETESCHEDULE:
                return url + "/" + bridge.getUsername() + "/schedules" + id;
            case GETorADDGROUPS:
                return url + "/" + bridge.getUsername();// + "/groups"
            case EDITGROUP:
                return url + "/" + bridge.getUsername() + "/groups/" + id;
            case SETGROUPSTATE:
                return url + "/" + bridge.getUsername() + "/groups/" + id + "/action";
            case USERNAME:
                return url;
            default:
                return url + "/" + bridge.getUsername() + "/lights";

        }
    }

    public static String getUrlWithoutIP(Bridge bridge, VolleyType type, int id){
        String url = getUrl(bridge, type, id);
        return url.substring(url.lastIndexOf("/api"));

    }

    @Override
    public void GetLightsReceived(List<HueLight> lights)
    {
        LightManager.getInstance().setLights(lights);
        listener.NotifyManagerDataChanged();
    }

    @Override
    public void GetSchedulesReceived(List<Schedule> schedules)
    {
        LightManager.getInstance().setSchedules(schedules);
        listener.NotifyManagerDataChanged();
    }

    @Override
    public void GetGroupsReceived(List<Group> groups)
    {
        LightManager.getInstance().setGroups(groups);
        listener.NotifyManagerDataChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
        listener.ChangeRequestReceived(response);
    }

    public enum VolleyType{
        GETLIGHTS,
        PUTLIGHTS,
        GETorADDSCHEDULES,
        DELETESCHEDULE,
        GETorADDGROUPS,
        EDITGROUP,
        SETGROUPSTATE,
        USERNAME,
    }
}
