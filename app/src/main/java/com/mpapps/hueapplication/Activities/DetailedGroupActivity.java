package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.ScheduleTime;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.joda.time.LocalDate;
import org.json.JSONArray;

import top.defaults.colorpicker.ColorPickerView;

public class DetailedGroupActivity extends AppCompatActivity implements Notifier, ScheduleFragment.OnFragmentInteractionListener
{

    private Bridge thisBridge;
    private Group group;
    private ColorPickerView colorPickerView;
    private float[] hsv = new float[3];
    private int groupId;
    private VolleyHelper volleyHelper;
    private Switch aSwitch;
    private TextView groupName;
    private TextView lightsNames;
    private View pickedColor;
    private LightManager manager;
    private Button scheduleButton;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_group);

        Intent intent = getIntent();
        group = intent.getParcelableExtra("GROUP");
        thisBridge = intent.getParcelableExtra("BRIDGE");

        volleyHelper = VolleyHelper.getInstance(this, this, thisBridge);
        manager = LightManager.getInstance();

        groupId = group.getId();
        pickedColor = findViewById(R.id.detailed_group_pickedcolor);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);


        aSwitch = findViewById(R.id.detailed_group_switch);
        aSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            group.getGroupState().setState(isChecked);
            volleyHelper.setGroupOnOff(groupId, isChecked);
        }));

        colorPickerView = findViewById(R.id.colorPickerGroup);
        colorPickerView.subscribe(((color, fromUser) -> {
            Color.colorToHSV(color, hsv);
            if (fromUser && aSwitch.isChecked() && group.getGroupState() != null) {
                volleyHelper.setGroupState(groupId, group.getGroupState().isState(), (int) (hsv[2] * 254.0f), (int) (hsv[0] * 182.04f), (int) (hsv[1] * 254.0f));
                pickedColor.setBackgroundColor(color);
            }else if(fromUser && !aSwitch.isChecked()){
                toast.setText(R.string.toast_text_turn_light_on);
                toast.show();
            }

        }));

        if(group.getGroupState() != null){
            aSwitch.setChecked(group.getGroupState().isState());
            float[] hsv = {group.getGroupState().getHue() / 182.04f, group.getGroupState().getSaturation() / 254f, group.getGroupState().getBrightness() / 254f};
            colorPickerView.setInitialColor(Color.HSVToColor(hsv));
            pickedColor.setBackgroundColor(Color.HSVToColor(hsv));
        }else{
            HueLight light = manager.getLights().get(group.getLightIds()[0]);
            volleyHelper.setGroupState(groupId, light.isState(), light.getBrightness(), light.getHue(), light.getSaturation());
        }
        groupName = findViewById(R.id.detailed_group_name);
        groupName.setText(group.getName());

        scheduleButton = findViewById(R.id.detailed_group_schedule_button);
        scheduleButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.detailed_group_schedule_fragment);
            if(fragment == null){
                ScheduleFragment bridgeFragment = ScheduleFragment.newInstance();
                bridgeFragment.show(fragmentManager, "dialog");
            }
        });

        lightsNames = findViewById(R.id.detailed_group_lights);
        lightsNames.setMovementMethod(new ScrollingMovementMethod());
        String lightsString = "";
        for (int i : group.getLightIds()) {
            for (HueLight light : manager.getLights()) {
                if(light.getId() == i)
                    lightsString += light.getName() + "\n";
            }
        }
        lightsNames.setText(lightsString);

    }

    @Override
    public void onFragmentInteraction(String name, int hour, int minute, boolean isOn, LocalDate scheduleDate)
    {
        ScheduleTime time = new ScheduleTime(hour, minute, scheduleDate);
        volleyHelper.addScheduleRequest(true, groupId, name, time, isOn);
    }

    @Override
    public void NotifyManagerDataChanged()
    {

    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {

    }

    @Override
    protected void onDestroy()
    {
        volleyHelper = null;
        VolleyService.Detach();
        super.onDestroy();
    }
}
