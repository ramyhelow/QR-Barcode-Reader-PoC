package com.ramyhelow.qrreader.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramyhelow.qrreader.Model.Code;
import com.ramyhelow.qrreader.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Code> data;
    private Activity activity;

    public RecyclerAdapter(ArrayList<Code> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.code_list_item, viewGroup, false);
        return new RecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.code_text.setText(String.valueOf(data.get(i).getText()));
        myViewHolder.code_time.setText(data.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView code_text;
        public TextView code_time;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            code_text = itemView.findViewById(R.id.code_text);
            code_time = itemView.findViewById(R.id.code_time);

        }
    }
}