package com.mpapps.hueapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.VolleyService;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<HueLight> mData;
    private LayoutInflater mInflater;
    private OnChangeListener mClickListener;
    private Context ctx;

    // data is passed into the constructor
    public RecyclerViewAdapter(Context context, List<HueLight> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        ctx = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_light, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HueLight light = mData.get(position);
        float[] hsv = {light.getHue()/182.04f, light.getSaturation() / 254f, light.getBrightness() / 254f};
        holder.cardView.setCardBackgroundColor(Color.HSVToColor(hsv));
        holder.lightname.setText(light.getName());
        holder.lightSwitch.setChecked(light.isState());
        holder.brightness.setProgress(light.getBrightness());

        holder.lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> mClickListener.onSwitchCheckedChangeListener(buttonView, isChecked, light.getId()));
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
                mClickListener.onSeekbarProgressChanged(seekBar, seekBar.getProgress(), light.getId());
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        Switch lightSwitch;
        SeekBar brightness;
        TextView lightname;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_light);
            lightname = itemView.findViewById(R.id.recycleview_item_textview);
            brightness = itemView.findViewById(R.id.recycleview_item_seekBar);
            lightSwitch = itemView.findViewById(R.id.recycleview_item_switch);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public HueLight getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(OnChangeListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnChangeListener
    {
        void onItemClick(View view, int position);
        void onSwitchCheckedChangeListener(CompoundButton buttonView, boolean isChecked, int lightId);
        void onSeekbarProgressChanged(SeekBar seekBar, int progress, int lightId);
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<HueLight> lights){
        mData.addAll(lights);
        notifyDataSetChanged();
    }
}
