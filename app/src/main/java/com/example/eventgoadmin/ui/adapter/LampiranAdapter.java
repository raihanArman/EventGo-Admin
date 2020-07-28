package com.example.eventgoadmin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ItemLampiranBinding;
import com.example.eventgoadmin.request.model.Lampiran;

import java.util.ArrayList;
import java.util.List;

public class LampiranAdapter extends RecyclerView.Adapter<LampiranAdapter.ViewHolder> {
    Context context;
    List<Lampiran> lampiranList = new ArrayList<>();

    public LampiranAdapter(Context context) {
        this.context = context;
    }

    public void setKajianList(List<Lampiran> lampiranList){
        this.lampiranList.clear();
        this.lampiranList.addAll(lampiranList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LampiranAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLampiranBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_lampiran, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LampiranAdapter.ViewHolder holder, int position) {
        Lampiran lampiran = lampiranList.get(position);
        holder.binding.setLampiran(lampiran);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DetailKajianUsulanActivity activity = (DetailKajianUsulanActivity) context;
//                FragmentManager fm = activity.getSupportFragmentManager();
//
//                PhotoViewFragment photoViewFragment = new PhotoViewFragment(lampiran.getGambar(), PhotoViewFragment.GAMBAR_LAMPIRAN);
//                photoViewFragment.show(fm, "");
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return lampiranList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemLampiranBinding binding;
        public ViewHolder(@NonNull ItemLampiranBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
