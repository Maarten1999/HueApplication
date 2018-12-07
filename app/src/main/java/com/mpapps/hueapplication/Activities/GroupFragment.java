package com.mpapps.hueapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mpapps.hueapplication.Adapters.GroupsAdapter;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.List;

public class GroupFragment extends Fragment implements Notifier, GroupsAdapter.OnChangeListener
{
    private Bridge thisBridge;
    private VolleyHelper volleyHelper;
    private LightManager manager;
    private GroupsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;


    public GroupFragment()
    {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(Bridge thisBridge)
    {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putParcelable("BRIDGE", thisBridge);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            thisBridge = getArguments().getParcelable("BRIDGE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        volleyHelper = VolleyHelper.getInstance(getContext(), this, thisBridge);
        manager = LightManager.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewGroupsFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new GroupsAdapter(getContext(), thisBridge);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.SwipeContainerGroupsFragment);
        swipeContainer.setOnRefreshListener(() ->
                {
                    volleyHelper.setListener(this);
                    volleyHelper.getGroupsRequest();

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(() ->
                    {
                        swipeContainer.setRefreshing(false);
                    }, 5000);
                }
        );

        FloatingActionButton addGroup = view.findViewById(R.id.group_fragment_add_group_fab);
        addGroup.setOnClickListener((v) -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.group_fragment_add_new_group);
            if(fragment == null){
                AddGroupFragment addGroupFragment = AddGroupFragment.newInstance();
                addGroupFragment.setTargetFragment(this, 1234);
                addGroupFragment.show(fragmentManager, "addgroup");
            }
        });

        volleyHelper.getGroupsRequest();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1234){
            String inputName = data.getStringExtra("NAME");
            int[] lightIds = data.getIntArrayExtra("LIGHTIDS");
            volleyHelper.addGroupRequest(inputName, lightIds);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        //volleyHelper.getGroupsRequest();
    }

    @Override
    public void onItemClick(View view, int position)
    {
        List<Group> groups = manager.getGroups();

        Intent intent = new Intent(getContext(), DetailedGroupActivity.class);
        intent.putExtra("GROUP", groups.get(position));
        intent.putExtra("BRIDGE", thisBridge);
        startActivity(intent);
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
        volleyHelper = null;

        super.onDestroy();
    }
}
