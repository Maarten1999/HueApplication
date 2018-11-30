package com.mpapps.hueapplication.Volley;

import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.Models.ScheduleTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static String UsernameParse(JSONArray response)
    {
        try {
            return response.getJSONObject(0).getJSONObject("success").getString("username");
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

    public static JSONObject setLight(boolean state){
        JSONObject json = new JSONObject();
        try {
            json.put("on", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject setLight(int brightness){
        JSONObject json = new JSONObject();
        try {
            json.put("bri", brightness);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject addOrChangeGroup(String name, int[] lights){
        JSONObject json = new JSONObject();
        try {
            JSONArray jsonLights = new JSONArray(lights);
            json.put("lights", jsonLights);
            json.put("name", name);
            json.put("type", "LightGroup");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject addSchedule(String name, ScheduleTime time, String address, boolean isOn, int brightness, int hue, int saturation){
        JSONObject json = new JSONObject();
        JSONObject jsonCommand = new JSONObject();
        try {
            jsonCommand.put("address", address);
            jsonCommand.put("method", "PUT");
            //todo maybe body line is not possible
            jsonCommand.put("body", HueProtocol.setLight(isOn, brightness, hue, saturation));
            json.put("name", name);
            json.put("time", time.getTimeString());
            json.put("command", jsonCommand);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static List<Group> GroupsParse(JSONObject response){
        List<Group> tempGroups = new ArrayList<>();
        for (int i = 0; i < response.names().length(); i++) {
            try{
                int id = Integer.parseInt(response.names().getString(i));
                String name = response.getJSONObject(response.names().getString(i)).getString("name");
                JSONObject lightJson = response.getJSONObject(response.names().getString(i)).getJSONObject("action");
                HueLight groupState = LightParse(lightJson);
                JSONArray lightIds = response.getJSONObject(response.names().getString(i)).getJSONArray("lights");
                int[] lights = new int[lightIds.length()];
                for (int j = 0; j < lightIds.length(); j++) {
                    lights[j] = lightIds.getInt(j);
                }
                Group group = new Group(id, name, lights, groupState);
                tempGroups.add(group);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return tempGroups;
    }
    public static List<HueLight> LightsParse(JSONObject response){
        List<HueLight> tempLights = new ArrayList<>();
        for (int i = 0; i < response.names().length(); i++) {
            try {
                int id = Integer.parseInt(response.names().getString(i));
                String name = response.getJSONObject(response.names().getString(i)).getString("name");
                JSONObject lightJson = response.getJSONObject(response.names().getString(i)).getJSONObject("state");
                HueLight light = LightParse(lightJson);
                light.setId(id);
                light.setName(name);
                tempLights.add(light);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tempLights;
    }
    public static List<Schedule> SchedulesParse(JSONObject response){
        List<Schedule> schedules = new ArrayList<>();
        JSONArray namesArray = response.names();
        if(namesArray == null)
            return schedules;
        for (int i = 0; i < namesArray.length(); i++) {
            try{
                int id = Integer.parseInt(response.names().getString(i));
                JSONObject scheduleJson = response.getJSONObject(namesArray.getString(i));//response.getJSONObject(response.names().getString(i));
                String name = scheduleJson.getString("name");
                Log.i("HueProtocol", scheduleJson.getString("time"));
                String timeString = scheduleJson.getString("time");
                ScheduleTime scheduleTime = new ScheduleTime(timeString);

                String address = scheduleJson.getJSONObject("command").getString("address");
                boolean isOn = scheduleJson.getJSONObject("command").getJSONObject("body").getBoolean("on");
                Schedule schedule = new Schedule(name, id, scheduleTime, address, isOn);
                schedules.add(schedule);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        return schedules;
    }

    private static HueLight LightParse(JSONObject lightJson) throws JSONException{
        boolean state = lightJson.getBoolean("on");
        int saturation = lightJson.getInt("sat");
        int brightness = lightJson.getInt("bri");
        int hue = lightJson.getInt("hue");
        return new HueLight(-1, "default", state, brightness, hue, saturation);
    }



}
