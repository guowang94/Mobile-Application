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
import com.example.android.graphapplication.model.SelectedScenarioSectionModel;

import java.util.List;

public class SelectedScenarioSectionAdapter extends RecyclerView.Adapter<SelectedScenarioSectionAdapter.MyViewHolder> {

    private Context context;
    private List<SelectedScenarioSectionModel> selectedScenarioSectionModelList;
    private SelectedScenarioAdapter selectedScenarioAdapter;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private RecyclerView mRecyclerView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mRecyclerView = view.findViewById(R.id.recycerView);
        }

    }

    public SelectedScenarioSectionAdapter(Context context, List<SelectedScenarioSectionModel> selectedScenarioSectionModelList) {
        this.context = context;
        this.selectedScenarioSectionModelList = selectedScenarioSectionModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sectioned_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SelectedScenarioSectionModel scenarioSectionModel = selectedScenarioSectionModelList.get(position);
        holder.mTitle.setText(scenarioSectionModel.getTitle());

        //recycler view for items
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        holder.mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        selectedScenarioAdapter = new SelectedScenarioAdapter(scenarioSectionModel.getSelectedScenarioModelList());
        holder.mRecyclerView.setAdapter(selectedScenarioAdapter);
    }

    @Override
    public int getItemCount() {
        return selectedScenarioSectionModelList.size();
    }
}