package com.example.android.graphapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.viewHolder.ScenarioSectionViewHolder;

import java.util.List;

public class ScenarioSectionAdapter extends RecyclerView.Adapter<ScenarioSectionAdapter.MyViewHolder> {

    private Context context;
    private List<ScenarioSectionViewHolder> mScenarioSectionViewHolderList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private RecyclerView mRecyclerView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mRecyclerView = view.findViewById(R.id.recycerView);
        }

    }

    public ScenarioSectionAdapter(Context context, List<ScenarioSectionViewHolder> scenarioSectionViewHolderList) {
        this.context = context;
        this.mScenarioSectionViewHolderList = scenarioSectionViewHolderList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sectioned_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ScenarioSectionViewHolder scenarioSectionViewHolder = mScenarioSectionViewHolderList.get(position);
        holder.mTitle.setText(scenarioSectionViewHolder.getTitle());

        //recycler view for items
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        ScenarioAdapter scenarioAdapter = new ScenarioAdapter(scenarioSectionViewHolder.getScenarioViewHolderList());
        holder.mRecyclerView.setAdapter(scenarioAdapter);
    }

    @Override
    public int getItemCount() {
        return mScenarioSectionViewHolderList.size();
    }

    public List<ScenarioSectionViewHolder> getAllScenario() {
        return mScenarioSectionViewHolderList;
    }
}