package com.example.eventgoadmin.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ItemEventBinding;
import com.example.eventgoadmin.request.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    List<Event> eventResponseList = new ArrayList<>();
    Context context;

    public EventAdapter(Context context) {
        this.context = context;
    }

    public void setEventResponseList(List<Event> eventResponseList){
        this.eventResponseList.clear();
        this.eventResponseList.addAll(eventResponseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_event, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventResponseList.get(position);
        String tanggal = DateFormat.format("EEEE, dd MMM yyyy", event.getTglEvent()).toString();
        String jam = DateFormat.format("HH:mm", event.getTglEvent()).toString();
        holder.binding.setJam(jam);
        holder.binding.setTanggal(tanggal);
        holder.binding.setEvent(event);

    }

    @Override
    public int getItemCount() {
        return eventResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemEventBinding binding;
        public ViewHolder(@NonNull ItemEventBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
