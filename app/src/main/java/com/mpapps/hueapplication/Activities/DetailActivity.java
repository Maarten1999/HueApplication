package com.mpapps.hueapplication.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;

public class DetailActivity extends AppCompatActivity {
    ImageView lightImage;
    SeekBar redSlider, greenSlider, blueSlider;
    TextView redValue, greenValue, blueValue;
    HueLight light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        light = (HueLight) intent.getParcelableExtra("LAMP");

        lightImage = findViewById(R.id.detailLampImage);

        float[] hsv = new float[]{light.getHue(),light.getSaturation(), light.getBrightness()};
        lightImage.setBackgroundColor(Color.HSVToColor(hsv));

        redSlider = findViewById(R.id.redSlider);
        greenSlider = findViewById(R.id.greenSlider);
        blueSlider = findViewById(R.id.blueSlider);

        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);

        redSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValue.setText(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
