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
import com.example.android.graphapplication.model.SummaryModel;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.MyViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private List<SummaryModel> summaryModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mValue;
        private ImageView mImageView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mValue = view.findViewById(R.id.value);
            mImageView = view.findViewById(R.id.imageView);
        }

    }

    public SummaryAdapter(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_summary, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        SummaryModel summaryModel = this.summaryModelList.get(position);
        Log.i(TAG, "onBindViewHolder: " + summaryModel.toString());
        holder.mTitle.setText(summaryModel.getTitle());
        holder.mValue.setText(summaryModel.getValue());
        holder.mImageView.setImageResource(summaryModel.getImage());
    }

    @Override
    public int getItemCount() {
        return summaryModelList.size();
    }
}