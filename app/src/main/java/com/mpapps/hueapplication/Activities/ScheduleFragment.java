package com.mpapps.hueapplication.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.VolleyHelper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

public class ScheduleFragment extends DialogFragment
{
    private OnFragmentInteractionListener listener;
    public static ScheduleFragment newInstance(){
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_bridgefragment);
        return inflater.inflate(R.layout.fragment_schedule_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Switch onOffSwitch = view.findViewById(R.id.fragment_schedule_switch);
        EditText nameInput = view.findViewById(R.id.fragment_schedule_inputname);

        TimePicker timePicker = view.findViewById(R.id.fragment_schedule_timepicker);
        timePicker.setIs24HourView(true);

        Button cancelButton = view.findViewById(R.id.fragment_schedule_cancel);
        cancelButton.setOnClickListener(v -> dismiss());
        Button addButton = view.findViewById(R.id.fragment_schedule_add);
        addButton.setOnClickListener(v ->
        {
            int hour = 0;
            int minute = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.getHour();
                minute = timePicker.getMinute();
            }else {
                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();
            }

            boolean isToday = true;
            Log.i("ScheduleFragment", minute + ":" + DateTime.now().getMinuteOfHour() + "");
            if(hour >= DateTime.now().getHourOfDay()){
                if(minute > DateTime.now().getMinuteOfHour()){
                    Toast.makeText(getContext(), "Schedule set for today at: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Schedule set for tomorrow at: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
                    isToday = false;
                }
            }
            else {
                isToday = false;
                Toast.makeText(getContext(), "Schedule set for tomorrow at: " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
            }

            LocalDate date = LocalDate.now();
            if(!isToday)
                date = date.plusDays(1);
            if(listener != null){
                listener.onFragmentInteraction(nameInput.getText().toString(), hour, minute, onOffSwitch.isChecked(), date);
            }
            dismiss();
        });
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(String name, int hour, int minute, boolean isOn, LocalDate scheduleDate);
    }
}
