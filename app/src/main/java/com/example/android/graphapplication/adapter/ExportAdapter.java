package com.example.android.graphapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.ExportModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ExportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ExportAdapter";
    private List<ExportModel> exportModelList;
    private Context context;

    public ExportAdapter(List<ExportModel> exportModelList, Context context) {
        this.exportModelList = exportModelList;
        this.context = context;
    }

    public class ReportTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTimestamp;

        ReportTitleViewHolder(View view) {
            super(view);

            mTimestamp = view.findViewById(R.id.timestamp);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView mHeader;

        HeaderViewHolder(View view) {
            super(view);

            mHeader = view.findViewById(R.id.header);
        }
    }

    public class TitleAndValueViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mValue;

        TitleAndValueViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.title);
            mValue = view.findViewById(R.id.value);
        }
    }

    public class MilestoneViewHolder extends RecyclerView.ViewHolder {

        private TextView mMilestoneName;
        private TextView mMilestoneType;
        private TextView mMilestoneAge;
        private TextView mMilestoneStatus;
        private TextView mMilestoneAmount;

        MilestoneViewHolder(View view) {
            super(view);
            mMilestoneName = view.findViewById(R.id.milestone_name);
            mMilestoneType = view.findViewById(R.id.milestone_type);
            mMilestoneAge = view.findViewById(R.id.milestone_age);
            mMilestoneStatus = view.findViewById(R.id.milestone_status);
            mMilestoneAmount = view.findViewById(R.id.milestone_total_amount);
        }
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        private TextView mPlanName;
        private TextView mPaymentAge;
        private TextView mPremiumStatus;
        private TextView mTotalPremiumPayment;
        private TextView mPayoutAge;
        private TextView mPayoutStatus;
        private TextView mTotalPayout;
        private RecyclerView mRecyclerView;

        PlanViewHolder(View view) {
            super(view);
            mPlanName = view.findViewById(R.id.plan_name);
            mPaymentAge = view.findViewById(R.id.payment_start_end);
            mPremiumStatus = view.findViewById(R.id.premium_status);
            mTotalPremiumPayment = view.findViewById(R.id.total_premium_payment);
            mPayoutAge = view.findViewById(R.id.payout_start_end);
            mPayoutStatus = view.findViewById(R.id.payout_status);
            mTotalPayout = view.findViewById(R.id.total_payout);
            mRecyclerView = view.findViewById(R.id.recycler_view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (exportModelList.get(position).getExportType()) {
            case 0:
                return ExportModel.REPORT_TITLE;
            case 1:
                return ExportModel.REPORT_HEADER;
            case 2:
                return ExportModel.REPORT_TOTAL_SECTION;
            case 3:
                return ExportModel.CLIENT_DETAIL;
            case 4:
                return ExportModel.MILESTONE_DETAIL;
            case 5:
                return ExportModel.PLAN_DETAIL;
            case 6:
                return ExportModel.PLAN_TYPE_DETAIL;
            case 7:
                return ExportModel.ROW_VALUE_WITH_COLOR;
            case 8:
                return ExportModel.REPORT_TOTAL_SECTION_WITH_COLOR;
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
                        .inflate(R.layout.list_row_export_report_title, parent, false);
                return new ReportTitleViewHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_header, parent, false);
                return new HeaderViewHolder(itemView);
            case 2:
            case 3:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_title_and_value, parent, false);
                return new TitleAndValueViewHolder(itemView);
            case 4:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_milestone, parent, false);
                return new MilestoneViewHolder(itemView);
            case 5:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_plan, parent, false);
                return new PlanViewHolder(itemView);
            case 6:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_plan_category, parent, false);
                return new TitleAndValueViewHolder(itemView);
            case 7:
            case 8:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_title_and_value, parent, false);
                return new TitleAndValueViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row_export_report_title, parent, false);
                return new ReportTitleViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        final ExportModel exportModel = this.exportModelList.get(position);

        Log.i(TAG, "onBindViewHolder: " + exportModel.toString());
        switch (exportModel.getExportType()) {
            case 0:
                ReportTitleViewHolder reportTitleViewHolder = (ReportTitleViewHolder) holder;
                reportTitleViewHolder.mTimestamp.setText(exportModel.getHeader());
                break;
            case 1:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.mHeader.setText(exportModel.getHeader());
                break;
            case 2:
                // Total Title and Value
                TitleAndValueViewHolder totalSectionViewHolder = (TitleAndValueViewHolder) holder;
                totalSectionViewHolder.mTitle.setText(exportModel.getTitle());
                totalSectionViewHolder.mValue.setText(exportModel.getValue());

                totalSectionViewHolder.mTitle.setTextAppearance(R.style.export_total_text);
                totalSectionViewHolder.mValue.setTextAppearance(R.style.export_total_amount);
                break;
            case 3:
                // Client Details
                TitleAndValueViewHolder titleAndValueViewHolder = (TitleAndValueViewHolder) holder;
                titleAndValueViewHolder.mTitle.setText(exportModel.getTitle());
                titleAndValueViewHolder.mValue.setText(exportModel.getValue());

                titleAndValueViewHolder.mTitle.setTextAppearance(R.style.export_title);
                titleAndValueViewHolder.mValue.setTextAppearance(R.style.export_value);
                break;
            case 4:
                MilestoneViewHolder milestoneViewHolder = (MilestoneViewHolder) holder;
                milestoneViewHolder.mMilestoneName.setText(exportModel.getMilestoneName());
                milestoneViewHolder.mMilestoneType.setText(exportModel.getMilestoneType());
                milestoneViewHolder.mMilestoneAge.setText(exportModel.getMilestoneAgeRange());
                milestoneViewHolder.mMilestoneStatus.setText(exportModel.getMilestoneStatus());
                milestoneViewHolder.mMilestoneAmount.setText(exportModel.getMilestoneAmount());
                break;
            case 5:
                PlanViewHolder planViewHolder = (PlanViewHolder) holder;
                planViewHolder.mPlanName.setText(exportModel.getPlanName());
                planViewHolder.mPaymentAge.setText(exportModel.getPaymentAgeRange());
                planViewHolder.mPremiumStatus.setText(exportModel.getPremiumStatus());
                planViewHolder.mTotalPremiumPayment.setText(exportModel.getTotalPremiumPayment());
                planViewHolder.mPayoutAge.setText(exportModel.getPayoutAgeRange());
                planViewHolder.mPayoutStatus.setText(exportModel.getPayoutStatus());
                planViewHolder.mTotalPayout.setText(exportModel.getTotalPayout());

                //recycler view for items
                planViewHolder.mRecyclerView.setHasFixedSize(true);
                planViewHolder.mRecyclerView.setNestedScrollingEnabled(false);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                planViewHolder.mRecyclerView.setLayoutManager(linearLayoutManager);
                ExportAdapter exportAdapter = new ExportAdapter(exportModel.getPlanTypeList(), context);
                planViewHolder.mRecyclerView.setAdapter(exportAdapter);
                break;
            case 6:
                // Plan Type
                TitleAndValueViewHolder planTypeViewHolder = (TitleAndValueViewHolder) holder;
                planTypeViewHolder.mTitle.setText(exportModel.getTitle());
                planTypeViewHolder.mValue.setText(exportModel.getValue());
                break;
            case 7:
                // Row's value with color
                TitleAndValueViewHolder valueWithColorViewHolder = (TitleAndValueViewHolder) holder;
                valueWithColorViewHolder.mTitle.setText(exportModel.getTitle());
                valueWithColorViewHolder.mValue.setText(String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(exportModel.getValue()))));

                valueWithColorViewHolder.mTitle.setTextAppearance(R.style.export_title);
                valueWithColorViewHolder.mValue.setTextAppearance(R.style.export_value);
                if (Float.valueOf(exportModel.getValue()) > 0f) {
                    valueWithColorViewHolder.mValue.setTextColor(context.getColor(R.color.listRowGreen));
                } else {
                    valueWithColorViewHolder.mValue.setTextColor(context.getColor(R.color.listRowRed));
                }
                break;
            case 8:
                // Total Section's value with color
                TitleAndValueViewHolder totalSectionValueWithColorViewHolder = (TitleAndValueViewHolder) holder;
                totalSectionValueWithColorViewHolder.mTitle.setText(exportModel.getTitle());

                totalSectionValueWithColorViewHolder.mTitle.setTextAppearance(R.style.export_total_text);
                totalSectionValueWithColorViewHolder.mValue.setTextAppearance(R.style.export_total_amount);

                String totalValue = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US)
                        .format(Float.valueOf(exportModel.getValue())));
                if (Float.valueOf(exportModel.getValue()) > 0f) {
                    totalSectionValueWithColorViewHolder.mValue.setTextColor(context.getColor(R.color.listRowGreen));
                    totalValue = "+" + totalValue;
                    totalSectionValueWithColorViewHolder.mValue.setText(totalValue);
                } else {
                    totalSectionValueWithColorViewHolder.mValue.setTextColor(context.getColor(R.color.listRowRed));
                    totalSectionValueWithColorViewHolder.mValue.setText(totalValue);
                }
                break;
            default:
                Log.d(TAG, "onBindViewHolder: in default");
        }
    }

    @Override
    public int getItemCount() {
        return exportModelList.size();
    }
}
