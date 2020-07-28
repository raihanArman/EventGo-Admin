package com.example.eventgoadmin.ui.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.FragmentProfilBinding;
import com.example.eventgoadmin.databinding.ItemRiwayatBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.model.Event;
import com.example.eventgoadmin.ui.DetailEventActivity;
import com.example.eventgoadmin.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    List<Event> eventResponseList = new ArrayList<>();
    Context context;

    public RiwayatAdapter(Context context) {
        this.context = context;
    }

    public void setEventResponseList(List<Event> eventResponseList){
        this.eventResponseList.clear();
        this.eventResponseList.addAll(eventResponseList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRiwayatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_riwayat, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdapter.ViewHolder holder, int position) {
        Event event = eventResponseList.get(position);
        String tanggal = DateFormat.format("EEEE, dd MMM yyyy", event.getTglRiwayat()).toString();
        String jam = DateFormat.format("HH:mm", event.getTglRiwayat()).toString();
        holder.binding.setJam(jam);
        holder.binding.setTanggal(tanggal);
        holder.binding.setEvent(event);

        if (event.getStatus().equals("ditolak")){
            holder.binding.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red_main));
            holder.binding.tvStatus.setText("Di tolak");
        }else if(event.getStatus().equals("diterima")){
            holder.binding.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.success));
            holder.binding.tvStatus.setText("Di terima");
        }else if(event.getStatus().equals("sedang proses")){
            holder.binding.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.proses));
            holder.binding.tvStatus.setText("sedang proses");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Proses ...");
                progressDialog.show();
                Intent intent = new Intent(context, DetailEventActivity.class);
                intent.putExtra("type_intent", Utils.TYPE_INTENT_RIWAYAT);
                intent.putExtra("id_riwayat_admin", event.getIdRiwayat());
                intent.putExtra("id_event", event.getIdEvent());
                context.startActivity(intent);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemRiwayatBinding binding;
        public ViewHolder(@NonNull ItemRiwayatBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
