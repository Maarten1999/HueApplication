package com.mpapps.hueapplication.Volley;

import org.json.JSONArray;

public interface Notifier
{
    void NotifyManagerDataChanged();
    void ChangeRequestReceived(JSONArray response);
}
