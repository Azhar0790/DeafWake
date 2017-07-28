package com.deafwake.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.deafwake.AddAlarm;
import com.deafwake.R;
import com.deafwake.model.AlarmWrapper;

import java.util.ArrayList;

/**
 * Created by wel on 14-04-2017.
 */

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private ArrayList<AlarmWrapper> data;
    private int rowLayout;
    private Activity activity;


    public AlarmAdapter(ArrayList<AlarmWrapper> data, int rowLayout, Activity activity) {

        this.data = data;
        this.rowLayout = rowLayout;
        this.activity = activity;
    }

    public void setData(ArrayList<AlarmWrapper> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    public void clearData() {

        if (data != null)
            data.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.time.setText(data.get(position).getTime());
        viewHolder.description.setText(data.get(position).getDescription());
        if (data.get(position).getIsSelected().equalsIgnoreCase("0"))
            viewHolder.switchView.setChecked(false);
        else
            viewHolder.switchView.setChecked(true);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent= new Intent(activity, AddAlarm.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("alarm", data.get(position));
                bundle.putInt("pos",position);
                intent.putExtras(bundle);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });

        viewHolder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b)
                    data.get(position).setIsSelected("1");
                else
                    data.get(position).setIsSelected("0");
            }
        });


    }

    @Override
    public int getItemCount() {

        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, description;
        Switch switchView;

        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            description = (TextView) itemView.findViewById(R.id.description);
            switchView = (Switch) itemView.findViewById(R.id.switchview);
        }
    }
}