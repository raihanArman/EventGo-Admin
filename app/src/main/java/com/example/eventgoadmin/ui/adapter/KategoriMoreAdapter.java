package com.example.eventgoadmin.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.eventgoadmin.MainActivity;
import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ItemSemuaKategoriBinding;
import com.example.eventgoadmin.request.model.Kategori;
import com.example.eventgoadmin.util.EditKategoriListener;

import java.util.ArrayList;
import java.util.List;

public class KategoriMoreAdapter extends RecyclerView.Adapter<KategoriMoreAdapter.ViewHolder> {

    List<Kategori> kategoriList = new ArrayList<>();
    Context context;
    EditKategoriListener editKategoriListener;

    public KategoriMoreAdapter(Context context, EditKategoriListener editKategoriListener) {
        this.context = context;
        this.editKategoriListener = editKategoriListener;
    }

    public void setKategoriList(List<Kategori> kategoriList){
        this.kategoriList.clear();
        this.kategoriList.addAll(kategoriList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KategoriMoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSemuaKategoriBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_semua_kategori, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriMoreAdapter.ViewHolder holder, int position) {
        holder.binding.setKategori(kategoriList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FrameLayout frameLayout = ((MainActivity)context).binding.frameHome;
//                FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                String idKategori = "", title="";
//
//                title = kategoriList.get(position).getNamaKategori();
//                idKategori = kategoriList.get(position).getIdKategori();
//
//                KategoriFragment kategoriFragment = new KategoriFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("id_kategori", idKategori);
//                bundle.putString("title", title);
//                kategoriFragment.setArguments(bundle);
//
//                transaction.replace(frameLayout.getId(), kategoriFragment);
//                transaction.commit();
            }
        });

        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editKategoriListener.showDialogEdit(kategoriList.get(position).getIdKategori());
            }
        });

        holder.binding.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editKategoriListener.showDialogHapus(kategoriList.get(position).getIdKategori());
            }
        });

    }

    @Override
    public int getItemCount() {
        return kategoriList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemSemuaKategoriBinding binding;
        public ViewHolder(@NonNull ItemSemuaKategoriBinding itemView) {
            super(itemView.getRoot());
            this.binding =itemView;
        }
    }
}
