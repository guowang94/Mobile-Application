package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.CommonTitleModel;

import java.util.List;

public class CommonTitleAdapter extends RecyclerView.Adapter<CommonTitleAdapter.MyViewHolder> {

    private static final String TAG = "CommonTitleAdapter";
    private List<CommonTitleModel> commonTitleModels;

    public CommonTitleAdapter(List<CommonTitleModel> commonTitleModels) {
        this.commonTitleModels = commonTitleModels;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
        }

    }

    @Override
    @NonNull
    public CommonTitleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonTitleAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        CommonTitleModel commonTitleModel = this.commonTitleModels.get(position);
        Log.i(TAG, "onBindViewHolder: " + commonTitleModel.toString());
        holder.mTitle.setText(commonTitleModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return commonTitleModels.size();
    }

    public void removeItem(int position) {
        commonTitleModels.remove(position);
        // notify the item removed by position to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
}
