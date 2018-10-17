package com.example.android.graphapplication.adapter;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.ScenarioSectionModel;

import java.util.List;

public class ScenarioSectionAdapter extends RecyclerView.Adapter<ScenarioSectionAdapter.MyViewHolder> {

    private Context context;
    private List<ScenarioSectionModel> scenarioSectionModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private RecyclerView mRecyclerView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mRecyclerView = view.findViewById(R.id.recycerView);
        }

    }

    public ScenarioSectionAdapter(Context context, List<ScenarioSectionModel> scenarioSectionModelList) {
        this.context = context;
        this.scenarioSectionModelList = scenarioSectionModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sectioned_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ScenarioSectionModel scenarioSectionModel = scenarioSectionModelList.get(position);
        holder.mTitle.setText(scenarioSectionModel.getTitle());

        //recycler view for items
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        ScenarioAdapter scenarioAdapter = new ScenarioAdapter(scenarioSectionModel.getScenarioModelList());
        holder.mRecyclerView.setAdapter(scenarioAdapter);
    }

    @Override
    public int getItemCount() {
        return scenarioSectionModelList.size();
    }

    public List<ScenarioSectionModel> getAllScenario() {
        return scenarioSectionModelList;
    }
}