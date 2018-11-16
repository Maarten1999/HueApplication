package com.mpapps.hueapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;

import java.util.List;

public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.ViewHolder>
{
    private Context context;
    private List<HueLight> lights;

    public LightsAdapter(Context context, List<HueLight> lights)
    {
        this.context = context;
        this.lights = lights;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recyclerview_item_light, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        HueLight hueLight = lights.get(position);

        // TODO: 16-11-2018 load image here

    }

    @Override
    public int getItemCount()
    {
        return lights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public ViewHolder(View itemView, final Context ctx)
        {
            super(itemView);
            context = ctx;

            // TODO: 16-11-2018 itemview.setonclicklistener
        }
    }

}
