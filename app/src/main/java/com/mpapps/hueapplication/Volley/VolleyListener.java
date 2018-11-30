package com.mpapps.hueapplication.Volley;

import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;

import org.json.JSONArray;

import java.util.List;

public interface VolleyListener
{
    void GetLightsReceived(List<HueLight> lights);
    void GetSchedulesReceived(List<Schedule> schedules);
    void GetGroupsReceived(List<Group> groups);
    void ChangeRequestReceived(JSONArray response);

}
