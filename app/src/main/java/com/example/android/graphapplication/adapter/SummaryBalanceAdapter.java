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
import com.example.android.graphapplication.model.SummaryBalance;

import java.util.List;

public class SummaryBalanceAdapter extends RecyclerView.Adapter<SummaryBalanceAdapter.MyViewHolder> {

    private static final String TAG = "SummaryBalanceAdapter";
    private List<SummaryBalance> summaryBalanceList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mValue;
        private ImageView mImage;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mValue = view.findViewById(R.id.value);
            mImage = view.findViewById(R.id.imageView);

        }
    }

    public SummaryBalanceAdapter(List<SummaryBalance> summaryBalanceList) {
        this.summaryBalanceList = summaryBalanceList;
    }

    @Override
    @NonNull
    public SummaryBalanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_summary_balance, parent, false);

        return new SummaryBalanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryBalanceAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        SummaryBalance summaryBalance = summaryBalanceList.get(position);
        holder.mImage.setImageResource(summaryBalance.getImage());
        holder.mTitle.setText(summaryBalance.getTitle());
        holder.mValue.setText(summaryBalance.getValue());
    }

    @Override
    public int getItemCount() {
        return summaryBalanceList.size();
    }
}

