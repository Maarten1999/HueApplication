package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mpapps.hueapplication.Adapters.BridgesAdapter;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.SQLite.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class BridgeActivity extends AppCompatActivity implements BridgeFragment.OnFragmentInteractionListener, BridgesAdapter.OnItemClickListener
{

    private DatabaseHandler database;
    List<Bridge> bridges;
    private BridgesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        database = new DatabaseHandler(this);
        bridges = database.getAllBridges();
        bridges.add(new Bridge("hello World", "34234234"));

        RecyclerView recyclerView = findViewById(R.id.RecyclerViewBridges);
        adapter = new BridgesAdapter(this, bridges);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FloatingActionButton addBridgeButton = findViewById(R.id.bridgeactivity_addBridgeButton);
        addBridgeButton.setOnClickListener((View v) -> OnFloatingActionButtonClick(v));
    }

    @Override
    public void onFragmentInteraction(Bridge bridge)
    {
        database.addBridge(bridge);
        bridges.add(bridge);
        adapter.notifyItemInserted(bridges.size() - 1);
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void OnItemClick(View view, int position)
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("HUE_BRIDGE_OBJECT", bridges.get(position));
        startActivity(intent);
    }

    public void OnFloatingActionButtonClick(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.bridgeactivity_bridge_fragment);
        if(fragment == null){
            BridgeFragment bridgeFragment = BridgeFragment.newInstance(this);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.bridgeactivity_bridge_fragment, bridgeFragment).addToBackStack(null).commit();
        }

    }
}
