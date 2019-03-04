package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.viewHolder.SelectedScenarioViewHolder;

import java.util.List;

public class SelectedScenarioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SelectedScenarioAdapter";
    private List<SelectedScenarioViewHolder> mSelectedScenarioViewHolderList;

    public SelectedScenarioAdapter(List<SelectedScenarioViewHolder> selectedScenarioViewHolderList) {
        this.mSelectedScenarioViewHolderList = selectedScenarioViewHolderList;
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
        private TextView mPlanStatus;

        PlanViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mType = view.findViewById(R.id.type);
            mAgeOccurred = view.findViewById(R.id.milestone_age);
            mAmount = view.findViewById(R.id.amount);
            mDuration = view.findViewById(R.id.duration);
            mPOAgeOccurred = view.findViewById(R.id.po_age);
            mPOAmount = view.findViewById(R.id.po_amount);
            mPODuration = view.findViewById(R.id.po_duration);
            mPlanStatus = view.findViewById(R.id.plan_status);
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
            mAgeOccurred = view.findViewById(R.id.milestone_age);
            mAmount = view.findViewById(R.id.amount);
            mDuration = view.findViewById(R.id.duration);
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        private TextView mSectionTitle;

        SectionViewHolder(View view) {
            super(view);
            mSectionTitle = view.findViewById(R.id.section_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mSelectedScenarioViewHolderList.get(position).getScenarioType()) {
            case 0:
                return SelectedScenarioViewHolder.OTHER_SCENARIO;
            case 1:
                return SelectedScenarioViewHolder.PLAN_SCENARIO;
            case 2:
                return SelectedScenarioViewHolder.SECTION_HEADER;
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
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_section_header, parent, false);
                return new SectionViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_selected_scenario, parent, false);
                return new OtherViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        final SelectedScenarioViewHolder selectedScenarioViewHolder = this.mSelectedScenarioViewHolderList.get(position);

        Log.i(TAG, "onBindViewHolder: " + selectedScenarioViewHolder.toString());
        switch (selectedScenarioViewHolder.getScenarioType()) {
            case 0:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                otherViewHolder.mTitle.setText(selectedScenarioViewHolder.getTitle());
                otherViewHolder.mType.setText(selectedScenarioViewHolder.getType());
                otherViewHolder.mAgeOccurred.setText(selectedScenarioViewHolder.getAge());
                otherViewHolder.mAmount.setText(selectedScenarioViewHolder.getAmount());
                otherViewHolder.mDuration.setText(selectedScenarioViewHolder.getDuration());
                break;
            case 1:
                PlanViewHolder planViewHolder = (PlanViewHolder) holder;
                planViewHolder.mTitle.setText(selectedScenarioViewHolder.getTitle());
                planViewHolder.mType.setText(selectedScenarioViewHolder.getType());
                planViewHolder.mAgeOccurred.setText(selectedScenarioViewHolder.getAge());
                planViewHolder.mAmount.setText(selectedScenarioViewHolder.getAmount());
                planViewHolder.mDuration.setText(selectedScenarioViewHolder.getDuration());
                planViewHolder.mPOAgeOccurred.setText(selectedScenarioViewHolder.getPoAge());
                planViewHolder.mPOAmount.setText(selectedScenarioViewHolder.getPoAmount());
                planViewHolder.mPODuration.setText(selectedScenarioViewHolder.getPoDuration());
                planViewHolder.mPlanStatus.setText(selectedScenarioViewHolder.getPlanStatus());
                break;
            case 2:
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                sectionViewHolder.mSectionTitle.setText(selectedScenarioViewHolder.getSectionTitle());
                break;
            default:
                Log.d(TAG, "onBindViewHolder: in default");
        }
    }

    @Override
    public int getItemCount() {
        return mSelectedScenarioViewHolderList.size();
    }
}
