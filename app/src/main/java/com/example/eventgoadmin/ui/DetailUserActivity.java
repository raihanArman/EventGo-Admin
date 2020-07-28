package com.example.eventgoadmin.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ActivityDetailEventBinding;
import com.example.eventgoadmin.databinding.ActivityDetailUserBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.request.model.User;
import com.example.eventgoadmin.request.model.Value;
import com.example.eventgoadmin.request.response.UserResponse;
import com.example.eventgoadmin.ui.adapter.EventTerbaruAdapter;
import com.example.eventgoadmin.ui.adapter.LampiranAdapter;
import com.example.eventgoadmin.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserActivity extends AppCompatActivity {

    ActivityDetailUserBinding binding;
    String idUser;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    EventTerbaruAdapter eventTerbaruAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_user);
        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        idUser = getIntent().getStringExtra("id_user");
        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);

        loadUser();

        eventTerbaruAdapter = new EventTerbaruAdapter(this);
        LinearLayoutManager layoutManagerHorizontal1 = new LinearLayoutManager(this);
        layoutManagerHorizontal1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvEvent.setLayoutManager(layoutManagerHorizontal1);
        binding.rvEvent.setAdapter(eventTerbaruAdapter);

        loadEvent();


    }

    private void loadEvent(){
        Call<EventResponse> eventResponseCall = apiRequest.eventByUserRequest(idUser);
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                eventTerbaruAdapter.setEventList(response.body().getEventList());
                if (response.body().getEventList().size() <= 0){
                    Toast.makeText(DetailUserActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    private void loadUser() {
        Call<UserResponse> userResponseCall = apiRequest.userRequest(idUser);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                User user = response.body().getUser();
                binding.setUser(user);
                String tanggal = DateFormat.format("dd MMM yyyy", user.getTglUpdate()).toString();
                binding.setTanggal(tanggal);

                if (user.getStatus().equals("Aktif")){
                    binding.tvStatus.setText("Non Aktifkan");
                    binding.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(DetailUserActivity.this, R.color.red_main));
                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateStatusUser("Tidak Aktif", user.getNama());
                        }
                    });
                }else if(user.getStatus().equals("Tidak Aktif")){
                    binding.tvStatus.setText("Aktifkan");
                    binding.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(DetailUserActivity.this, R.color.quantum_lightblue800));
                    binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateStatusUser("Aktif", user.getNama());
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(DetailUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatusUser(String status, String namaUser) {
        if (status.equals("Tidak Aktif")){
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailUserActivity.this);
            builder.setTitle("Pesan");
            builder.setMessage("Apakah anda yakin ingin non aktifkan akun "+namaUser+" ?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prosesNonAktif(status, namaUser);
                }
            });
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }else if(status.equals("Aktif")){
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailUserActivity.this);
            builder.setTitle("Pesan");
            builder.setMessage("Apakah anda yakin ingin aktifkan akun "+namaUser+" ?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prosesNonAktif(status, namaUser);
                }
            });
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void prosesNonAktif(String status, String nama) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Call<Value> updateuserCall = apiRequest.updateUserRequest(
                idUser,
                status
        );
        updateuserCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailUserActivity.this);
                        builder.setTitle("Pesan");
                        builder.setMessage("Anda telah non aktifkan akun "+nama);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(DetailUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void prosesAktif(String status, String nama) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Call<Value> updateuserCall = apiRequest.updateUserRequest(
                idUser,
                status
        );
        updateuserCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailUserActivity.this);
                        builder.setTitle("Pesan");
                        builder.setMessage("Anda telah aktifkan akun "+nama);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(DetailUserActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}