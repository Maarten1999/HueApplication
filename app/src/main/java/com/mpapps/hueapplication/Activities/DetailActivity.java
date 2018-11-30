package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.List;

import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class DetailActivity extends AppCompatActivity implements VolleyListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        light = intent.getParcelableExtra("LAMP");
        thisBridge = intent.getParcelableExtra("BRIDGE");
        volleyHelper = new VolleyHelper(this, this, thisBridge);
        lightId = light.getId();

        manager = LightManager.getInstance();

        lampName = findViewById(R.id.detail_name);
        lampName.setText(light.getName());

        pickedColor = findViewById(R.id.detail_pickedcolor);


        aSwitch = findViewById(R.id.detail_switch);
        aSwitch.setChecked(light.isState());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                light.setState(isChecked);
                volleyHelper.turnLightOnOff(lightId,isChecked);
            }
        });

        colorPickerView = findViewById(R.id.colorPicker);
        float[] hsv = {light.getHue() / 182.04f, light.getBrightness() / 254f, light.getSaturation() / 254f};
        colorPickerView.setInitialColor(Color.HSVToColor(hsv));
        colorPickerView.subscribe((color, fromUser) -> {
            Color.colorToHSV(color, hsv);
            if (fromUser) {
                volleyHelper.setLight(lightId, light.isState(), (int) (hsv[1] * 254.0f), (int) (hsv[0] * 182.04f), (int) (hsv[2] * 254.0f));
                pickedColor.setBackgroundColor(color);
            }
        });
    }

    @Override
    public void GetLightsReceived(List<HueLight> lights) {

    }

    @Override
    public void ChangeRequestReceived(JSONArray response) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        volleyHelper = null;
        VolleyService.Detach();
    }
}
