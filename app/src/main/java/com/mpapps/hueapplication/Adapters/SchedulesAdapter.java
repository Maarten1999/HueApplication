package com.mpapps.hueapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpapps.hueapplication.LightManager;
import com.mpapps.hueapplication.Models.Schedule;
import com.mpapps.hueapplication.Models.ScheduleTime;
import com.mpapps.hueapplication.R;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.ViewHolder>
{
    private LayoutInflater mInflater;
    private LightManager manager;
    private OnChangeListener mClickListener;
    public SchedulesAdapter(Context context)
    {
        this.mInflater = LayoutInflater.from(context);
        manager = LightManager.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.recyclerview_item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Schedule schedule = manager.getSchedules().get(position);
        holder.nameTextView.setText(schedule.getName());

        ScheduleTime time = schedule.getLocalTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'hh:mm:ss");

        String hours = String.valueOf(time.getDateTime().getHourOfDay());
        String minutes = String.valueOf(time.getDateTime().getMinuteOfHour());
        if(time.getDateTime().getHourOfDay() < 9)
            hours = "0" + hours;
        if(time.getDateTime().getMinuteOfHour() < 9)
            minutes = "0" + minutes;
        holder.timeTextView.setText(hours + ":" + minutes);
        holder.dateTextView.setText(formatter.print(time.getDateTime()).split("T")[0]);
    }

    @Override
    public int getItemCount()
    {
        return manager.getSchedules().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView timeTextView;
        TextView dateTextView;
        TextView nameTextView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.cardview_schedule_time);
            dateTextView = itemView.findViewById(R.id.cardview_schedule_date);
            nameTextView = itemView.findViewById(R.id.cardview_schedule_name);
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
