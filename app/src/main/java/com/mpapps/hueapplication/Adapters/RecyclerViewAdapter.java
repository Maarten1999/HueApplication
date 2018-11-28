package com.mpapps.hueapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.HueProtocol;
import com.mpapps.hueapplication.Volley.VolleyListener;
import com.mpapps.hueapplication.Volley.VolleyService;

import org.json.JSONArray;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements VolleyListener
{
    private LayoutInflater mInflater;
    private OnChangeListener mClickListener;
    private Context ctx;
    private LightManager manager;
    private VolleyService volleyService;
    private Bridge thisBridge;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, Bridge bridge) {
        this.mInflater = LayoutInflater.from(context);
        manager = LightManager.getInstance();
        ctx = context;
        thisBridge = bridge;
        volleyService = VolleyService.getInstance(ctx, this);
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_light, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HueLight light = manager.getLights().get(position);
        float[] hsv = {light.getHue()/182.04f, light.getSaturation() / 254f, light.getBrightness() / 254f};
        holder.cardView.setCardBackgroundColor(Color.HSVToColor(hsv));
        holder.lightname.setText(light.getName());
        holder.lightSwitch.setChecked(light.isState());
        holder.brightness.setProgress(light.getBrightness());


        holder.brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
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
                volleyService.changeRequest(VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, light.getId()),
                        HueProtocol.setLight(seekBar.getProgress()),Request.Method.PUT);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return manager.getLights().size();
    }

    @Override
    public void GetLightsReceived(List<HueLight> lights)
    {
//        manager.setLights(lights);
//        notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
        volleyService.getRequest(VolleyService.getUrl(thisBridge,VolleyService.VolleyType.GETLIGHTS,
                0),null);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        Switch lightSwitch;
        SeekBar brightness;
        TextView lightname;
        boolean isTouched = false;

        @SuppressLint("ClickableViewAccessibility")
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_light);
            lightname = itemView.findViewById(R.id.recycleview_item_textview);
            brightness = itemView.findViewById(R.id.recycleview_item_seekBar);
            lightSwitch = itemView.findViewById(R.id.recycleview_item_switch);

            lightSwitch.setOnTouchListener((v, event) ->
            {
                isTouched = true;
                return false;
            });
            lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(isTouched){
                        isTouched = false;
                        volleyService.changeRequest(VolleyService.getUrl(thisBridge, VolleyService.VolleyType.PUTLIGHTS, manager.getLights().get(getAdapterPosition()).getId()),
                                HueProtocol.setLight(isChecked), Request.Method.PUT);

                    }
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public HueLight getItem(int id) {
        return manager.getLights().get(id);
    }

    public void setClickListener(OnChangeListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface OnChangeListener
    {
        void onItemClick(View view, int position);
    }
}
