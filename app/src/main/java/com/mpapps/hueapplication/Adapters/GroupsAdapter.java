package com.mpapps.hueapplication.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.mpapps.hueapplication.R;
import com.mpapps.hueapplication.Volley.Notifier;
import com.mpapps.hueapplication.Volley.VolleyHelper;

import org.json.JSONArray;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> implements Notifier
{
    private LayoutInflater mInflater;
    private GroupsAdapter.OnChangeListener mClickListener;
    private Context ctx;
    private LightManager manager;
    private VolleyHelper volleyHelper;
    private Bridge thisBridge;

    public GroupsAdapter(Context context, Bridge bridge){
        this.mInflater = LayoutInflater.from(context);
        //lights = LightManager.getInstance().getLights();
        manager = LightManager.getInstance();
        ctx = context;
        thisBridge = bridge;
        volleyHelper = VolleyHelper.getInstance(ctx, this, bridge);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recyclerview_item_light, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Group group = manager.getGroups().get(position);
        if(group.getGroupState() != null) {
            float[] hsv = {group.getGroupState().getHue() / 182.04f, group.getGroupState().getSaturation() / 254f, group.getGroupState().getBrightness() / 254f};

            if (group.getGroupState().getSaturation() < 100 && group.getGroupState().getBrightness() > 100) {
                holder.groupName.setTextColor(Color.BLACK);
                holder.groupIcon.setImageResource(R.drawable.hue_light_black);
            } else {
                holder.groupName.setTextColor(Color.WHITE);
                holder.groupIcon.setImageResource(R.drawable.hue_light);
            }

            holder.cardView.setCardBackgroundColor(Color.HSVToColor(hsv));
            holder.groupName.setText(group.getName());
            holder.groupSwitch.setChecked(group.getGroupState().isState());
            holder.brightness.setProgress(group.getGroupState().getBrightness());
        }else{
            HueLight light = manager.getLights().get(group.getLightIds()[0]);
            volleyHelper.setGroupState(group.getId(), light.isState(),light.getBrightness(), light.getHue(), light.getSaturation());
        }
    }

    @Override
    public int getItemCount()
    {
        return manager.getGroups().size();
    }

    @Override
    public void NotifyManagerDataChanged()
    {
        notifyDataSetChanged();
    }

    @Override
    public void ChangeRequestReceived(JSONArray response)
    {
       // volleyHelper.getGroupsRequest();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cardView;
        Switch groupSwitch;
        SeekBar brightness;
        TextView groupName;
        ImageView groupIcon;
        private boolean isTouched = false;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View itemView)
        {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardview_light);
            groupName = itemView.findViewById(R.id.recycleview_item_textview);
            brightness = itemView.findViewById(R.id.recycleview_item_seekBar);
            groupSwitch = itemView.findViewById(R.id.recycleview_item_switch);
            groupIcon = itemView.findViewById(R.id.recycleview_item_image);

            groupSwitch.setOnTouchListener((v, event) ->
            {
                isTouched = true;
                return false;
            });
            groupSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
            {
                if (isTouched) {
                    isTouched = false;
                    Group group = manager.getGroups().get(getAdapterPosition());
                    volleyHelper.setGroupOnOff(group.getId(), isChecked);

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
                    Group group = manager.getGroups().get(getAdapterPosition());
                    volleyHelper.setGroupBrightness(group.getId(), seekBar.getProgress());
                }
            });
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v)
        {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(OnChangeListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface OnChangeListener {
        void onItemClick(View view, int position);
    }
}
