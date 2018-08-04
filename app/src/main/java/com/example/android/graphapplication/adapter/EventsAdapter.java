package com.example.android.graphapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.graphapplication.R;
import com.example.android.graphapplication.model.Events;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    private static final String TAG = "EventsAdapter";
    private List<Events> events;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        MyViewHolder(View view) {
            super(view);

            mTitle = view.findViewById(R.id.title);
        }

    }

    public EventsAdapter(List<Events> events) {
        this.events = events;
    }

    @Override
    @NonNull
    public EventsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_event, parent, false);

        return new EventsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.MyViewHolder holder, int position) {
        Log.i(TAG, "Element " + position + " set.");

        Events events = this.events.get(position);
        Log.i(TAG, "onBindViewHolder: " + events.toString());
        holder.mTitle.setText(events.getTitle());
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

    public void restoreItem(Events item, int position) {
        events.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
