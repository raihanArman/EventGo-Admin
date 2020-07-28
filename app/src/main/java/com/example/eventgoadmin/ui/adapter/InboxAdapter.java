package com.example.eventgoadmin.ui.adapter;

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
import com.example.eventgoadmin.databinding.ItemEventUserBinding;
import com.example.eventgoadmin.request.model.Event;
import com.example.eventgoadmin.ui.DetailEventActivity;

import java.util.ArrayList;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {
    List<Event> eventResponseList = new ArrayList<>();
    Context context;

    public InboxAdapter(Context context) {
        this.context = context;
    }

    public void setEventResponseList(List<Event> eventResponseList){
        this.eventResponseList.clear();
        this.eventResponseList.addAll(eventResponseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InboxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_event_user, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxAdapter.ViewHolder holder, int position) {
        Event event = eventResponseList.get(position);
        String tanggal = DateFormat.format("EEEE, dd MMM yyyy", event.getTglEvent()).toString();
        String jam = DateFormat.format("HH:mm", event.getTglEvent()).toString();
        holder.binding.setJam(jam);
        holder.binding.setTanggal(tanggal);
        holder.binding.setEvent(event);

        String tanggalUpload = DateFormat.format("EEEE, dd MMM yyyy", event.getTglUpload()).toString();
        holder.binding.setTanggalUpload(tanggalUpload);

        String tanggalEvent = DateFormat.format("yyyy-MM-dd HH:mm:ss", event.getTglEvent()).toString();
        event.setTanggal(tanggalEvent);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailEventActivity.class);
                intent.putExtra("event", event);
                intent.putExtra("id_event", event.getIdEvent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemEventUserBinding binding;
        public ViewHolder(@NonNull ItemEventUserBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
