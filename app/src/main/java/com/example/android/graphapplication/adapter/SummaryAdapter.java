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

public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SummaryAdapter";
    private List<SummaryModel> summaryModelList;

    public SummaryAdapter(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
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
        switch (summaryModelList.get(position).getCellType()) {
            case 0:
                return SummaryModel.SECTION_HEADER;
            case 1:
                return SummaryModel.CONTENT;
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

        SummaryModel summaryModel = this.summaryModelList.get(position);

        Log.i(TAG, "onBindViewHolder: " + summaryModel.toString());
        switch (summaryModel.getCellType()) {
            case 0:
                SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
                sectionViewHolder.mSectionTitle.setText(summaryModel.getSectionTitle());
                break;
            case 1:
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mTitle.setText(summaryModel.getTitle());
                contentViewHolder.mValue.setText(summaryModel.getValue());
                contentViewHolder.mImageView.setImageResource(summaryModel.getImage());
                break;
            default:
                Log.d(TAG, "onBindViewHolder: in default");
        }
    }

    @Override
    public int getItemCount() {
        return summaryModelList.size();
    }
}