package com.example.eventgoadmin.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ItemUserBinding;
import com.example.eventgoadmin.request.model.User;
import com.example.eventgoadmin.ui.DetailUserActivity;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;

    public UserAdapter(Context context) {
        this.context = context;
    }

    public void setUserList(List<User> userList){
        this.userList.clear();
        this.userList.addAll(userList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_user, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.binding.setEvent(user);
        String tanggal = DateFormat.format("EEEE, dd MMM yyyy", user.getTglUpdate()).toString();
        holder.binding.setTanggalMasuk(tanggal);
        if (user.getStatus().equals("Aktif")){
            holder.binding.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.quantum_lightblue800));
            holder.binding.tvStatus.setText("Di tolak");
        }else if(user.getStatus().equals("Tidak Aktif")){
            holder.binding.lvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.red_main));
            holder.binding.tvStatus.setText("Di terima");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailUserActivity.class);
                intent.putExtra("id_user", user.getIdUser());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemUserBinding binding;
        public ViewHolder(@NonNull ItemUserBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
