package com.mpapps.hueapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Adapters.LightsAdapter;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.List;

public class MainActivity extends AppCompatActivity implements VolleyListener
{

    private VolleyService volleyService;
    private LightManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyService = VolleyService.getInstance(this.getApplicationContext(), this);
        manager = new LightManager();

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewLights);
        LightsAdapter lightsAdapter = new LightsAdapter(this, manager.getLights());
        recyclerView.setAdapter(lightsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        volleyService.getRequest("http://192.168.178.38:80/api/93e934e1ac5531c48ebf7838af52e94/lights", null);
        volleyService.putRequest(VolleyService.basicRequestUrlMaartenHome + "/lights/1/state", HueProtocol.setLight(false, 1,5000,1));
    }

    @Override
    public void GetLightsReceived(List<HueLight> lights)
    {
        manager.setLights(lights);
    }

    @Override
    public void PutLightsReceived(JSONArray response)
    {

    }
}
