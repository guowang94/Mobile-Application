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
import com.example.android.graphapplication.viewHolder.SummaryViewHolder;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private List<SummaryViewHolder> mSummaryViewHolderList;

    public SummaryAdapter(List<SummaryViewHolder> summaryViewHolderList) {
        this.mSummaryViewHolderList = summaryViewHolderList;
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        private TextView mSectionTitle;

        SectionViewHolder(View view) {
            super(view);
            mSectionTitle = view.findViewById(R.id.section_title);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mValue;
        private ImageView mImageView;

        ContentViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
            mValue = view.findViewById(R.id.value);
            mImageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mSummaryViewHolderList.get(position).getCellType()) {
            case 0:
                return SummaryViewHolder.SECTION_HEADER;
            case 1:
                return SummaryViewHolder.CONTENT;
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
                        .inflate(R.layout.list_row_section_header, parent, false);
                return new SectionViewHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_summary, parent, false);
                return new ContentViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_section_header, parent, false);
                return new SectionViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        SummaryViewHolder summaryViewHolder = this.mSummaryViewHolderList.get(position);

        Log.i(TAG, "onBindViewHolder: " + summaryViewHolder.toString());
        switch (summaryViewHolder.getCellType()) {
            case 0:
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                sectionViewHolder.mSectionTitle.setText(summaryViewHolder.getSectionTitle());
                break;
            case 1:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mTitle.setText(summaryViewHolder.getTitle());
                contentViewHolder.mValue.setText(summaryViewHolder.getValue());
                contentViewHolder.mImageView.setImageResource(summaryViewHolder.getImage());
                break;
            default:
                Log.d(TAG, "onBindViewHolder: in default");
        }
    }

    @Override
    public int getItemCount() {
        return mSummaryViewHolderList.size();
    }
}