package com.example.android.graphapplication.recycleList;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.graphapplication.R;

import java.util.List;

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.MyViewHolder> {

    private static final String TAG = "UserInfoAdapter";
    private List<UserInfo> userInfoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mValue;
        private ImageView mImage;

        public MyViewHolder(View view) {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_info_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
