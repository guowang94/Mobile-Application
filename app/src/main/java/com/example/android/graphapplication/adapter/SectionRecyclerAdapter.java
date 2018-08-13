package com.example.android.graphapplication.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.SectionModel;

import java.util.ArrayList;
import java.util.List;

public class SectionRecyclerAdapter extends RecyclerView.Adapter<SectionRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "SectionRecyclerAdapter";
    private Context context;
    private List<SectionModel> sectionModelList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private RecyclerView mRecyclerView;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mRecyclerView = view.findViewById(R.id.recycerView);
        }

    }

    public SectionRecyclerAdapter(Context context, List<SectionModel> sectionModelList) {
        this.context = context;
        this.sectionModelList = sectionModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_sectioned_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SectionModel sectionModel = sectionModelList.get(position);
        holder.mTitle.setText(sectionModel.getTitle());

        //recycler view for items
        holder.mRecyclerView.setHasFixedSize(true);
        holder.mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        holder.mRecyclerView.setLayoutManager(linearLayoutManager);
        SummaryAdapter adapter = new SummaryAdapter(sectionModel.getItemList());
        holder.mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionModelList.size();
    }
}