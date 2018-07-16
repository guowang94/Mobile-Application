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
import com.example.android.graphapplication.model.UserInfo;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {

    private static final String TAG = "UserInfoAdapter";
    private List<UserInfo> userInfoList;

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

    public UserInfoAdapter(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_user_info, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        UserInfo userInfo = userInfoList.get(position);
        holder.mImage.setImageResource(userInfo.getImage());
        holder.mTitle.setText(userInfo.getTitle());
        holder.mValue.setText(userInfo.getValue());
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }
}
