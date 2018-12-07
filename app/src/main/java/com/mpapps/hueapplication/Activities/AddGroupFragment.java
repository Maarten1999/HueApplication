package com.mpapps.hueapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class AddGroupFragment extends DialogFragment
{
    private OnFragmentInteractionListener listener;
    public static AddGroupFragment newInstance(){
        return new AddGroupFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_group_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        LightManager manager = LightManager.getInstance();
        String[] values = new String[manager.getLights().size()];
        for (int i = 0; i < manager.getLights().size(); i++) {
            String lightString = manager.getLights().get(i).getId()
                    + ": " + manager.getLights().get(i).getName();
            values[i] = lightString;

        }

        ListView listView = view.findViewById(R.id.fragment_addgroup_listview);
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, values));
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        EditText inputName = view.findViewById(R.id.fragment_addgroup_inputname);
        Button cancelButton = view.findViewById(R.id.fragment_addgroup_cancel);
        cancelButton.setOnClickListener(v -> dismiss());
        Button addButton = view.findViewById(R.id.fragment_addgroup_add);
        addButton.setOnClickListener((v -> {
            SparseBooleanArray checked = listView.getCheckedItemPositions();
            List<Integer> lights = new ArrayList<>();
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if(checked.valueAt(i))
                    lights.add(manager.getLights().get(position).getId());
            }

            int[] lightIds = new int[lights.size()];
            for (int i = 0; i < lights.size(); i++) {
                lightIds[i] = lights.get(i);
            }
            Fragment fragment = getTargetFragment();
            if(fragment != null){
                Intent data = new Intent();
                data.putExtra("NAME", inputName.getText().toString());
                data.putExtra("LIGHTIDS", lightIds);
                fragment.onActivityResult(getTargetRequestCode(), 1, data);
            }
            //listener.onFragmentInteraction(inputName.getText().toString(), lightIds);
            dismiss();
        }));
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
//        if(context instanceof OnFragmentInteractionListener){
//            listener = (OnFragmentInteractionListener) context;
//        }else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(String name, int[] lightIds);
    }
}
