package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.Models.ScheduleTime;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.joda.time.LocalDate;
import org.json.JSONArray;

import java.util.List;

import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class DetailActivity extends AppCompatActivity implements Notifier, ScheduleFragment.OnFragmentInteractionListener
{

    private Bridge thisBridge;
    private HueLight light;
    private ColorPickerView colorPickerView;
    private float[] hsv = new float[3];
    private int lightId;
    private VolleyHelper volleyHelper;
    private Switch aSwitch;
    private TextView lampName;
    private View pickedColor;
    private LightManager manager;
    private Button scheduleButton;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        light = intent.getParcelableExtra("LAMP");
        thisBridge = intent.getParcelableExtra("BRIDGE");

        volleyHelper = VolleyHelper.getInstance(this, this, thisBridge);

        lightId = light.getId();

        pickedColor = findViewById(R.id.detail_pickedcolor);
        manager = LightManager.getInstance();

        lampName = findViewById(R.id.detail_name);
        lampName.setText(light.getName());
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        aSwitch = findViewById(R.id.detail_switch);
        aSwitch.setChecked(light.isState());
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            light.setState(isChecked);
            volleyHelper.turnLightOnOff(lightId, isChecked);
        });


        colorPickerView = findViewById(R.id.colorPicker);
        float[] hsv = {light.getHue() / 182.04f, light.getSaturation() / 254f, light.getBrightness() / 254f};
        colorPickerView.setInitialColor(Color.HSVToColor(hsv));
        pickedColor.setBackgroundColor(Color.HSVToColor(hsv));
        colorPickerView.subscribe((color, fromUser) -> {
            Color.colorToHSV(color, hsv);
            if (fromUser && aSwitch.isChecked()) {
                volleyHelper.setLight(lightId, light.isState(), (int) (hsv[2] * 254.0f), (int) (hsv[0] * 182.04f), (int) (hsv[1] * 254.0f));
                pickedColor.setBackgroundColor(color);
            }else if(fromUser && !aSwitch.isChecked()){
                toast.setText(R.string.toast_text_turn_light_on);
                toast.show();
            }
        });

        scheduleButton = findViewById(R.id.detail_shedule_button);
        scheduleButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.detailedactivity_schedule_fragment);
            if(fragment == null){
                ScheduleFragment bridgeFragment = ScheduleFragment.newInstance();
                bridgeFragment.show(fragmentManager, "dialog");
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy()
    {
        volleyHelper = null;
        VolleyService.Detach();
        super.onDestroy();
    }

    @Override
    public void NotifyManagerDataChanged()
    {

    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
        //volleyHelper.getLightsRequest();
    }

    @Override
    public void onFragmentInteraction(String name, int hour, int minute, boolean isOn, LocalDate scheduleDate)
    {
        ScheduleTime time = new ScheduleTime(hour, minute, scheduleDate);
        volleyHelper.addScheduleRequest(false, lightId, name, time, isOn);
    }
}
