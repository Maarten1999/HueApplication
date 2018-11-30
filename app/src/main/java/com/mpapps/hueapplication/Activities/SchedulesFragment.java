package com.mpapps.hueapplication.Activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpapps.hueapplication.Adapters.RecyclerViewAdapter;
import com.mpapps.hueapplication.Adapters.SchedulesAdapter;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchedulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulesFragment extends Fragment implements Notifier, SchedulesAdapter.OnChangeListener
{

    private Bridge thisBridge;
    private VolleyHelper volleyHelper;
    private LightManager manager;
    private SchedulesAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    public SchedulesFragment()
    {

    }


    public static SchedulesFragment newInstance(Bridge bridge)
    {
        SchedulesFragment fragment = new SchedulesFragment();
        Bundle args = new Bundle();
        args.putParcelable("BRIDGE", bridge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thisBridge = getArguments().getParcelable("BRIDGE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        volleyHelper = VolleyHelper.getInstance(getContext(), this, thisBridge);
        manager = LightManager.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewSchedulesFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SchedulesAdapter(getContext());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.SwipeContainerSchedulesFragment);
        swipeContainer.setOnRefreshListener(() ->
        {
            volleyHelper.setListener(this);
            volleyHelper.getSchedulesRequest();

            Handler mHandler = new Handler();
            mHandler.postDelayed(()->
            swipeContainer.setRefreshing(false), 5000);
        });

        volleyHelper.getSchedulesRequest();
    }

//    @Override
//    public void GetLightsReceived(List<HueLight> lights)
//    {
//
//    }
//
//    @Override
//    public void GetSchedulesReceived(List<Schedule> schedules)
//    {
//
//    }
//
//    @Override
//    public void GetGroupsReceived(List<Group> groups)
//    {
//
//    }

    @Override
    public void NotifyManagerDataChanged()
    {
        swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {

    }

    @Override
    public void onDestroy(){
        VolleyService.Detach();
        volleyHelper = null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        volleyHelper.Detach();
        volleyHelper = null;
    }

    @Override
    public void onItemClick(View view, int position)
    {
        //todo start maybe new intent
    }
}
