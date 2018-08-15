package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.ScenarioModel;

import java.util.List;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.MyViewHolder> {

    private static final String TAG = "ScenarioAdapter";
    private List<ScenarioModel> scenarioModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private ImageView mImageView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mImageView = view.findViewById(R.id.imageView);
        }

    }

    public ScenarioAdapter(List<ScenarioModel> scenarioModelList) {
        this.scenarioModelList = scenarioModelList;
    }

    @Override
    @NonNull
    public ScenarioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_scenario, parent, false);
        return new ScenarioAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScenarioAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        ScenarioModel scenarioModel = this.scenarioModelList.get(position);
        Log.i(TAG, "onBindViewHolder: " + scenarioModel.toString());
        holder.mTitle.setText(scenarioModel.getTitle());
        if (scenarioModel.isSelected()) {
            holder.mImageView.setVisibility(View.VISIBLE);
        } else {
            holder.mImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return scenarioModelList.size();
    }
}