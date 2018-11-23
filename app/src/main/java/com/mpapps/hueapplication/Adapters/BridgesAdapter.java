package com.mpapps.hueapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpapps.hueapplication.Models.Bridge;
import com.mpapps.hueapplication.Models.HueLight;
import com.mpapps.hueapplication.R;

import java.util.List;

public class BridgesAdapter extends RecyclerView.Adapter<BridgesAdapter.ViewHolder>
{

    private Context context;
    private List<Bridge> bridges;
    private OnItemClickListener clickListener;

    public BridgesAdapter(Context context, List<Bridge> bridges)
    {
        this.context = context;
        this.bridges = bridges;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recyclerview_item_bridge, parent, false);
        return new BridgesAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Bridge bridge = bridges.get(position);

        //todo some stuff
    }

    @Override
    public int getItemCount()
    {
        return bridges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView bridgeTextView;
        public ViewHolder(View itemView, final Context ctx)
        {
            super(itemView);
            context = ctx;

            bridgeTextView = itemView.findViewById(R.id.recyclerview_item_bridge_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if(clickListener != null) clickListener.OnItemClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }
}
