package com.mpapps.hueapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.Group;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;
import com.mpapps.hueapplication.Volley.VolleyListener;

import org.json.JSONArray;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Notifier
{
    private LayoutInflater mInflater;
    private OnChangeListener mClickListener;
    private Context ctx;
    private LightManager manager;
    private VolleyHelper volleyHelper;
    private Bridge thisBridge;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, Bridge bridge) {
        this.mInflater = LayoutInflater.from(context);
        //lights = LightManager.getInstance().getLights();
        manager = LightManager.getInstance();
        ctx = context;
        thisBridge = bridge;
        volleyHelper = VolleyHelper.getInstance(ctx, this, bridge);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_light, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HueLight light = manager.getLights().get(position);
        float[] hsv = {light.getHue() / 182.04f, light.getSaturation() / 254f, light.getBrightness() / 254f};

        if (light.getSaturation() < 100 && light.getBrightness() > 100) {
            holder.lightname.setTextColor(Color.BLACK);
            holder.lightIcon.setImageResource(R.drawable.hue_light_black);
        }
        else {
            holder.lightname.setTextColor(Color.WHITE);
            holder.lightIcon.setImageResource(R.drawable.hue_light);
        }

        holder.cardView.setCardBackgroundColor(Color.HSVToColor(hsv));
        holder.lightname.setText(light.getName());
        holder.lightSwitch.setChecked(light.isState());
        holder.brightness.setProgress(light.getBrightness());
    }

    @Override
    public int getItemCount() {
        return manager.getLights().size();
    }

//    @Override
//    public void GetLightsReceived(List<HueLight> lights) {
//        manager.setLights(lights);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public void GetSchedulesReceived(List<Schedule> schedules)
//    {
//
//    }
//
//    @Override
//    public void GetGroupsReceived(List<Group> groups)
//    {
//
//    }

    @Override
    public void NotifyManagerDataChanged()
    {
        notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response) {
        volleyHelper.getLightsRequest();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        Switch lightSwitch;
        SeekBar brightness;
        TextView lightname;
        ImageView lightIcon;
        private boolean isTouched = false;

        @SuppressLint("ClickableViewAccessibility")
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_light);
            lightname = itemView.findViewById(R.id.recycleview_item_textview);
            brightness = itemView.findViewById(R.id.recycleview_item_seekBar);
            lightSwitch = itemView.findViewById(R.id.recycleview_item_switch);
            lightIcon = itemView.findViewById(R.id.recycleview_item_image);

            lightSwitch.setOnTouchListener((v, event) ->
            {
                isTouched = true;
                return false;
            });
            lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
            {
                if (isTouched) {
                    isTouched = false;
                    HueLight light = manager.getLights().get(getAdapterPosition());
                    volleyHelper.turnLightOnOff(light.getId(), isChecked);

                }
            });

            brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    HueLight light = manager.getLights().get(getAdapterPosition());
                    volleyHelper.setLightBrightness(light.getId(), seekBar.getProgress());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(OnChangeListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface OnChangeListener {
        void onItemClick(View view, int position);
    }
}
