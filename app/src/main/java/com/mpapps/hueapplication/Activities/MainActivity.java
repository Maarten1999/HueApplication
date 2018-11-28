package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.mpapps.hueapplication.Adapters.RecyclerViewAdapter;
import com.mpapps.hueapplication.LightManager;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements VolleyListener, RecyclerViewAdapter.OnChangeListener
{
    private VolleyService volleyService;
    private LightManager manager;
    RecyclerViewAdapter adapter;
    private Bridge thisBridge;
    private boolean isWaitingForHandshake = false;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volleyService = VolleyService.getInstance(this.getApplicationContext(), this);
        manager = LightManager.getInstance();

        thisBridge = getIntent().getParcelableExtra("HUE_BRIDGE_OBJECT");
        if (thisBridge.getUsername() == null) {
            isWaitingForHandshake = true;
            volleyService.changeRequest("http://" + thisBridge.getIP() + "/api", HueProtocol.getUsername("HueApplication"), Request.Method.POST);
        }

        List<HueLight> lights = new ArrayList<>();

        manager.setLights(lights);
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewLights);
        adapter = new RecyclerViewAdapter(this, thisBridge);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeContainer = findViewById(R.id.SwipeContainer);

        swipeContainer.setOnRefreshListener(() -> {
            if(isWaitingForHandshake)
                volleyService.changeRequest("http://" +
                        thisBridge.getIP() + "/api",
                        HueProtocol.getUsername("HueApplication"),
                        Request.Method.POST);
            else
                GetLights();

            Handler mHandler = new Handler();
            mHandler.postDelayed(() -> {
                swipeContainer.setRefreshing(false);
                volleyService.emptyRequestQueue();
            }, 5000);
                }

        );


    }

    @Override
    public void GetLightsReceived(List<HueLight> lights) {
        swipeContainer.setRefreshing(false);
        manager.setLights(lights);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response) {
        boolean succeeded = true;
        swipeContainer.setRefreshing(false);
        for (int i = 0; i < response.length(); i++) {
            try {

                if (response.getJSONObject(i).getString("success") == null) {
                    succeeded = false;
                } else {
                    if (isWaitingForHandshake) {
                        thisBridge.setUsername(HueProtocol.UsernameParse(response));
                        DatabaseHandler.getInstance(this).updateBridge(thisBridge);
                        isWaitingForHandshake = false;
                    }
                }

            } catch (JSONException e) {
                succeeded = false;
            }
        }
        if (!succeeded)
            Toast.makeText(this, "Request not succeeded", Toast.LENGTH_SHORT).show();
        volleyService.getRequest(VolleyService.getUrl(thisBridge,VolleyService.VolleyType.GETLIGHTS,
                0),null);
    }

    @Override
    public void onItemClick(View view, int position) {
        List<HueLight> lights = manager.getLights();

        if (lights.get(position).isState()) {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra("LAMP", lights.get(position));
            intent.putExtra("BRIDGE", thisBridge);
            startActivity(intent);
        }else
            Toast.makeText(this,"turn on the lamp first", Toast.LENGTH_LONG).show();
    }

    private void GetLights() {
        volleyService.getRequest(VolleyService.getUrl(thisBridge, VolleyService.VolleyType.GETLIGHTS, 0), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);

        View view = menu.findItem(R.id.menu_switch).getActionView();

        Switch actionBarSwitch = view.findViewById(R.id.switchForActionBar);
        actionBarSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            for (HueLight light : manager.getLights()) {
                volleyService.changeRequest(
                        VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, light.getId()),
                        HueProtocol.setLight(isChecked), Request.Method.PUT);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}
