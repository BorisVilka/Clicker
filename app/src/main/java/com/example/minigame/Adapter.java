package com.example.minigame;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    List<Double> data;
    int pos;

    private Context context;

    private double[] a = new double[]{0.01,0.02,0.03,0.04,0.05,0.06,0.07,0.08,0.09};

    public Adapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        pos = -1;
        for(int i = 0;i<300;i++) data.add(a[i%9]);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(data.get(position), pos==position, context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void bind(double num, boolean b,Context context) {
            if(b) {
                itemView.setBackground(context.getDrawable(R.drawable.list_select));
                ((TextView)itemView.findViewById(R.id.count)).setTextColor(Color.WHITE);
            }
            ((TextView)itemView.findViewById(R.id.count)).setText(String.format("%.2f",num));
        }


    }
}
