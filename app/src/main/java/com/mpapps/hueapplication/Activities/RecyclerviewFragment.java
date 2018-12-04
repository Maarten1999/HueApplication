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
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.SQLite.DatabaseHandler;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewFragment extends Fragment implements Notifier, RecyclerViewAdapter.OnChangeListener
{

    private Bridge thisBridge;
    private VolleyHelper volleyHelper;
    private boolean isWaitingForHandshake = false;
    private LightManager manager;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView.LayoutManager layoutManager;
    private Parcelable mListState;
    private Toast toast;

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

        toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);

        //volleyService = VolleyService.getInstance(getContext(), this);
        volleyHelper = VolleyHelper.getInstance(getContext(),this, thisBridge);
        manager = LightManager.getInstance();

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
                    volleyHelper.setListener(this);
                    if (isWaitingForHandshake)
                        volleyHelper.getUsername();
                    else
                        volleyHelper.getLightsRequest();

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(() ->
                    {
                        swipeContainer.setRefreshing(false);
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
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void NotifyManagerDataChanged()
    {
        swipeContainer.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
        boolean succeeded = true;
        boolean isScheduleError = false;
        swipeContainer.setRefreshing(false);
        for (int i = 0; i < response.length(); i++) {
            try {

                if (response.getJSONObject(i).getString("success") == null) {
                    succeeded = false;
                    if(response.getJSONObject(i).getJSONObject("error").getString("description") != null)
                        if(response.getJSONObject(i).getJSONObject("error").getString("description").contains("invalid value"))
                            Toast.makeText(getContext(), "Scheduled time is not in the future", Toast.LENGTH_SHORT);
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
        if (!succeeded){
            toast.setText("Request not succeeded");
            toast.show();
        }
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

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        volleyHelper.Detach();
        volleyHelper = null;
    }
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
