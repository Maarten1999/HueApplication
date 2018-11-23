package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mpapps.hueapplication.Adapters.RecyclerViewAdapter;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Adapters.LightsAdapter;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.SQLite.DatabaseHandler;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VolleyListener, RecyclerViewAdapter.ItemClickListener {

    private VolleyService volleyService;
    private LightManager manager;
    RecyclerViewAdapter adapter;
    private Bridge thisBridge;
    //private DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyService = VolleyService.getInstance(this.getApplicationContext(), this);
        manager = new LightManager();

        thisBridge = getIntent().getParcelableExtra("HUE_BRIDGE_OBJECT");

        //tests
        //volleyService.getRequest("http://192.168.178.38:80/api/93e934e1ac5531c48ebf7838af52e94/lights", null);
//        volleyService.putRequest(VolleyService.basicRequestUrlMaartenHome + "/lights/1/state", HueProtocol.setLight(false, 1, 5000, 1));

        ArrayList<HueLight> hueLights = new ArrayList<>();
        hueLights.add(new HueLight(1, true, 4, 345, 2));

        manager.setLights(hueLights);
        //recyclerview
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewLights);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, manager.getLights());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void GetLightsReceived(List<HueLight> lights) {
        manager.setLights(lights);
    }

    @Override
    public void PutLightsReceived(JSONArray response) {
        boolean succeeded = true;
        for (int i = 0; i < response.length(); i++) {
            try {
                if (response.getJSONObject(i).getString("success") == null) {
                    succeeded = false;
                    //todo something with a new request.
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!succeeded)
            Toast.makeText(this, "Light Request not succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("LAMP", manager.getLights().get(position));
        startActivity(intent);
    }
}
