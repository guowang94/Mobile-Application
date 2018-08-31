package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.CommonModel;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.MyViewHolder> {

    private static final String TAG = "CommonAdapter";
    private List<CommonModel> events;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
        }

    }

    public CommonAdapter(List<CommonModel> events) {
        this.events = events;
    }

    @Override
    @NonNull
    public CommonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        CommonModel commonModel = this.events.get(position);
        Log.i(TAG, "onBindViewHolder: " + commonModel.toString());
        holder.mTitle.setText(commonModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void removeItem(int position) {
        events.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
}
