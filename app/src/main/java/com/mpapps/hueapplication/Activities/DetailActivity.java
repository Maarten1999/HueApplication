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

import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;

import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    HueLight light;
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();
        light = intent.getParcelableExtra("LAMP");

        colorPickerView = findViewById(R.id.colorPicker);

        colorPickerView.subscribe((color, fromUser) -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(color);
            }
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(color));
            }
        });
    }

    @Override
    public void onClick(View v) {
        new ColorPickerPopup.Builder(this)
                .initialColor(colorPickerView.getColor())
                .enableAlpha(true)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        v.setBackgroundColor(color);
                        colorPickerView.setInitialColor(color);
                    }

                    @Override
                    public void onColor(int color, boolean fromUser) {

                    }
                });
    }
}
