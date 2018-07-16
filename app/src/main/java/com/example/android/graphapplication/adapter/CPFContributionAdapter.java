package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.CPFContribution;

import java.util.List;

public class CPFContributionAdapter extends RecyclerView.Adapter<CPFContributionAdapter.MyViewHolder> {

    private static final String TAG = "CPFContributionAdapter";
    private List<CPFContribution> cpfContribution;

     class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mValue;

         MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mValue = view.findViewById(R.id.value);
        }

    }

    public CPFContributionAdapter(List<CPFContribution> cpfContribution) {
        this.cpfContribution = cpfContribution;
    }

    @Override
    @NonNull
    public CPFContributionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_cpf_contribution, parent, false);

        return new CPFContributionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CPFContributionAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        CPFContribution cpfContribution = this.cpfContribution.get(position);
        Log.i(TAG, "onBindViewHolder: " + cpfContribution.toString());
        holder.mTitle.setText(cpfContribution.getTitle());
        holder.mValue.setText(cpfContribution.getValue());
    }

    @Override
    public int getItemCount() {
        return cpfContribution.size();
    }
}