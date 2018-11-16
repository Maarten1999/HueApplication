package com.mpapps.hueapplication;

import android.net.sip.SipSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private VolleyService volleyService;
    private List<HueLight> lights;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyService = VolleyService.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewLights);
        LightsAdapter lightsAdapter = new LightsAdapter(this, lights);
        recyclerView.setAdapter(lightsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }
}
