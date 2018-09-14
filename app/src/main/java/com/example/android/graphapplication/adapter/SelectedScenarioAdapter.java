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
import com.example.android.graphapplication.model.SelectedScenarioModel;

import java.util.List;

public class SelectedScenarioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SelectedScenarioAdapter";
    private List<SelectedScenarioModel> selectedScenarioModelList;

    public SelectedScenarioAdapter(List<SelectedScenarioModel> selectedScenarioModelList) {
        this.selectedScenarioModelList = selectedScenarioModelList;
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mType;
        private TextView mAgeOccurred;
        private TextView mAmount;
        private TextView mDuration;
        private TextView mPOAgeOccurred;
        private TextView mPOAmount;
        private TextView mPODuration;

        PlanViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mType = view.findViewById(R.id.type);
            mAgeOccurred = view.findViewById(R.id.age);
            mAmount = view.findViewById(R.id.amount);
            mDuration = view.findViewById(R.id.duration);
            mPOAgeOccurred = view.findViewById(R.id.po_age);
            mPOAmount = view.findViewById(R.id.po_amount);
            mPODuration = view.findViewById(R.id.po_duration);
        }
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mType;
        private TextView mAgeOccurred;
        private TextView mAmount;
        private TextView mDuration;

        OtherViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mType = view.findViewById(R.id.type);
            mAgeOccurred = view.findViewById(R.id.age);
            mAmount = view.findViewById(R.id.amount);
            mDuration = view.findViewById(R.id.duration);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (selectedScenarioModelList.get(position).getScenarioType()) {
            case 0:
                return SelectedScenarioModel.OTHER_SCENARIO;
            case 1:
                return SelectedScenarioModel.PLAN_SCENARIO;
            default:
                return -1;
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_selected_scenario, parent, false);
                return new OtherViewHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_selected_plan_scenario, parent, false);
                return new PlanViewHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        final SelectedScenarioModel selectedScenarioModel = this.selectedScenarioModelList.get(position);

        Log.i(TAG, "onBindViewHolder: " + selectedScenarioModel.toString());
        switch (selectedScenarioModel.getScenarioType()) {
            case 0:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                otherViewHolder.mTitle.setText(selectedScenarioModel.getTitle());
                otherViewHolder.mType.setText(selectedScenarioModel.getType());
                otherViewHolder.mAgeOccurred.setText(selectedScenarioModel.getAge());
                otherViewHolder.mAmount.setText(selectedScenarioModel.getAmount());
                otherViewHolder.mDuration.setText(selectedScenarioModel.getDuration());
                break;
            case 1:
                PlanViewHolder planViewHolder = (PlanViewHolder) holder;
                planViewHolder.mTitle.setText(selectedScenarioModel.getTitle());
                planViewHolder.mType.setText(selectedScenarioModel.getType());
                planViewHolder.mAgeOccurred.setText(selectedScenarioModel.getAge());
                planViewHolder.mAmount.setText(selectedScenarioModel.getAmount());
                planViewHolder.mDuration.setText(selectedScenarioModel.getDuration());
                planViewHolder.mPOAgeOccurred.setText(selectedScenarioModel.getPoAge());
                planViewHolder.mPOAmount.setText(selectedScenarioModel.getPoAmount());
                planViewHolder.mPODuration.setText(selectedScenarioModel.getPoDuration());
                break;
            default:
                Log.d(TAG, "onBindViewHolder: in default");
        }
    }

    @Override
    public int getItemCount() {
        return selectedScenarioModelList.size();
    }
}
