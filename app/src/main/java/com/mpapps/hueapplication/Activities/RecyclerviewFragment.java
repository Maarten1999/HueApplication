package com.mpapps.hueapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewFragment extends Fragment implements VolleyListener, RecyclerViewAdapter.OnChangeListener
{

    private Bridge thisBridge;
    private VolleyHelper volleyHelper;
    private boolean isWaitingForHandshake = false;
    private LightManager manager;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView.LayoutManager layoutManager;
    private Parcelable mListState;

    private OnFragmentInteractionListener mListener;

    public RecyclerviewFragment()
    {
        // Required empty public constructor
    }

    public static RecyclerviewFragment newInstance(Bridge bridge)
    {
        RecyclerviewFragment fragment = new RecyclerviewFragment();
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

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //volleyService = VolleyService.getInstance(getContext(), this);
        volleyHelper = new VolleyHelper(getContext(), this, thisBridge);
        manager = LightManager.getInstance();
        manager.setLights(new ArrayList<>());

        if (thisBridge.getUsername() == null) {
            isWaitingForHandshake = true;
            volleyHelper.getUsername();
//            VolleyService.getInstance(getContext(), this).changeRequest("http://" + thisBridge.getIP() + "/api", HueProtocol.getUsername("HueApplication"), Request.Method.POST);
        }

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewLightsFragment);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(getContext(), thisBridge);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Switch mainSwitch = view.findViewById(R.id.MainSwitchFragrment);
        mainSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            for (HueLight light : manager.getLights()) {
                volleyHelper.turnLightOnOff(light.getId(), isChecked);
//            volleyService.changeRequest(
//                    VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, light.getId()),
//                    HueProtocol.setLight(isChecked), Request.Method.PUT);
        }
        });

        swipeContainer = view.findViewById(R.id.SwipeContainerFragment);
        swipeContainer.setOnRefreshListener(() ->
                {
                    if (isWaitingForHandshake)
                        volleyHelper.getUsername();
                    else
                        volleyHelper.getLightsRequest();

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(() ->
                    {
                        swipeContainer.setRefreshing(false);
                        //volleyService.emptyRequestQueue();
                    }, 5000);
                }
        );

        if (!isWaitingForHandshake)
            volleyHelper.getLightsRequest();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void GetLightsReceived(List<HueLight> lights)
    {
        swipeContainer.setRefreshing(false);
        manager.setLights(lights);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
        boolean succeeded = true;
        swipeContainer.setRefreshing(false);
        for (int i = 0; i < response.length(); i++) {
            try {

                if (response.getJSONObject(i).getString("success") == null) {
                    succeeded = false;
                } else {
                    if (isWaitingForHandshake) {
                        thisBridge.setUsername(HueProtocol.UsernameParse(response));
                        DatabaseHandler.getInstance(getContext()).updateBridge(thisBridge);
                        isWaitingForHandshake = false;
                        Toast.makeText(getContext(), "Paired with Bridge", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                succeeded = false;
            }
        }
        if (!succeeded)
            Toast.makeText(getContext(), "Request not succeeded", Toast.LENGTH_SHORT).show();
        volleyHelper.getLightsRequest();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        List<HueLight> lights = manager.getLights();

        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("LAMP", lights.get(position));
        intent.putExtra("BRIDGE", thisBridge);
        startActivity(intent);

    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

//    private void GetLights()
//    {
//        volleyService.getRequest(VolleyService.getUrl(thisBridge, VolleyService.VolleyType.GETLIGHTS, 0), null);
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState)
//    {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//
//            thisBridge = savedInstanceState.getParcelable("BRIDGE");
//            adapter = new RecyclerViewAdapter(getContext(), thisBridge);
//            manager.setLights(savedInstanceState.getParcelableArrayList("LIGHTS"));
//            mListState = savedInstanceState.getParcelable("LIST_MANAGER");
//            layoutManager.onRestoreInstanceState(mListState);
//        }
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
//    {
//        inflater.inflate(R.menu.mainactivity_menu, menu);
//
//        View view = menu.findItem(R.id.menu_switch).getActionView();
//
//        Switch actionBarSwitch = view.findViewById(R.id.switchForActionBar);
//        actionBarSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
//        {
//            for (HueLight light : manager.getLights()) {
//                volleyService.changeRequest(
//                        VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, light.getId()),
//                        HueProtocol.setLight(isChecked), Request.Method.PUT);
//            }
//        });
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public void onSaveInstanceState(Bundle outState)
//    {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable("BRIDGE", thisBridge);
//        ArrayList<HueLight> tempLights = new ArrayList<>(manager.getLights());
//        outState.putParcelableArrayList("LIGHTS", tempLights);
//        mListState = layoutManager.onSaveInstanceState();
//        outState.putParcelable("LIST_MANAGER", mListState);
//    }

//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
//        if (mListState != null)
//            layoutManager.onRestoreInstanceState(mListState);
//    }

    @Override
    public void onDestroy()
    {
        VolleyService.Detach();
        layoutManager = null;
        mListState = null;
        DatabaseHandler.Detach();
        volleyHelper = null;

        super.onDestroy();
    }
}
