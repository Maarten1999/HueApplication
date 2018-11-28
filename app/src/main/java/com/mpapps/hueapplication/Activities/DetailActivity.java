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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
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
    private double x, y;
    private int lightId;
    private VolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        volleyService = VolleyService.getInstance(this, this);

        Intent intent = getIntent();
        light = intent.getParcelableExtra("LAMP");
        thisBridge = intent.getParcelableExtra("BRIDGE");
        x = light.getX();
        y = light.getY();
        lightId = light.getId();

        colorPickerView = findViewById(R.id.colorPicker);
        float[] hsv = {light.getHue()/182.04f, light.getBrightness() / 254f, light.getSaturation() / 254f};
        colorPickerView.setInitialColor(Color.HSVToColor(hsv));

        colorPickerView.setInitialColor(Color.WHITE);
        colorPickerView.subscribe((color, fromUser) -> {

            Color.colorToHSV(color, hsv);
            volleyService.changeRequest(VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, lightId),
                    HueProtocol.setLight(light.isState(), (int) (hsv[2] * 254f), (int) (hsv[0] * 182.04f), (int) (hsv[1] * 245)), Request.Method.PUT);

        });
    }

    @Override
    public void GetLightsReceived(List<HueLight> lights) {

    }

    @Override
    public void ChangeRequestReceived(JSONArray response) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }
}
