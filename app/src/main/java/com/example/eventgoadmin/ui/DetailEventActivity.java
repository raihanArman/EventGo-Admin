package com.example.eventgoadmin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ActivityDetailEventBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.FCMRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.Event;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.request.model.Lampiran;
import com.example.eventgoadmin.request.model.Token;
import com.example.eventgoadmin.request.model.Value;
import com.example.eventgoadmin.request.model.push_notif.DataMessage;
import com.example.eventgoadmin.request.model.push_notif.FCMResponse;
import com.example.eventgoadmin.ui.adapter.LampiranAdapter;
import com.example.eventgoadmin.util.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEventActivity extends AppCompatActivity {

    private static final String TAG = "DetailEventActivity";

    ActivityDetailEventBinding binding;
    String idEvent, idUser, idRiwayat;
    int typeIntent;
    ApiRequest apiRequest, fcmRequest;
    LampiranAdapter lampiranAdapter;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_event);
        idEvent = getIntent().getStringExtra("id_event");
        typeIntent = getIntent().getIntExtra("type_intent", 0);
        event = getIntent().getParcelableExtra("event");
        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);
        fcmRequest = FCMRequest.getClient(Utils.fcmUrl).create(ApiRequest.class);

        loadDataEvent();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvLampiran.setLayoutManager(linearLayoutManager);
        lampiranAdapter = new LampiranAdapter(this);
        binding.rvLampiran.setAdapter(lampiranAdapter);

        loadLampiran();

        if (typeIntent == Utils.TYPE_INTENT_RIWAYAT){
            binding.tvEdit.setVisibility(View.VISIBLE);
            idRiwayat = getIntent().getStringExtra("id_riwayat_admin");
            Toast.makeText(this, ""+idRiwayat, Toast.LENGTH_SHORT).show();
            binding.tvEdit.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.red_main));
            binding.tvEdit.setText("Hapus");
        }

        binding.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent("diterima", "");
            }
        });
        binding.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailEventActivity.this);
                builder.setTitle("Pesan");
                EditText etIsi = new EditText(DetailEventActivity.this);
                etIsi.setHint("Isi pesan");
                builder.setView(etIsi);
                builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateEvent("ditolak","Alasan : "+etIsi.getText().toString());
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
        });

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeIntent == Utils.TYPE_INTENT_RIWAYAT){
                    deleteRiwayat();
                }
            }
        });
        
    }
    private void deleteRiwayat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pesan");
        builder.setMessage("Apakah anda yakin ingin menghapus riwayat event ini ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Value> hapusCall = apiRequest.hapusRiwayatRequest(idRiwayat);
                hapusCall.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(DetailEventActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.body().getValue() == 1) {
                                dialog.dismiss();
                                finish();
                            }else {
                                dialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(DetailEventActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void updateEvent(String status, String alasan) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        event.setStatus(status);
        Call<Value> updateEventCall = apiRequest.updateEventRequest(
                event
        );
        updateEventCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                Toast.makeText(DetailEventActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if(response.body().getValue() == 1){
                    Call<Value> inputPesanCall = apiRequest.inputPesanRequest(event.getIdUser(), event.getIdEvent(), "Event yang berjudul "+event.getJudul()+" telah "+event.getStatus()+" "+alasan);
                    inputPesanCall.enqueue(new Callback<Value>() {
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {
                            progressDialog.dismiss();
                            if (response.body().getValue() == 1) {
                                sendMessagingToUser(status);
                            }
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(DetailEventActivity.this, "Error pesan : ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailEventActivity.this, "Error event : "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessagingToUser(String message){
        String tokenAdmin = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(Utils.TOKEN_TBL);
        db.child(event.getIdUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Map<String, String> content = new HashMap<>();
                content.put("title", "usulan");
                content.put("id_event", idEvent);
                content.put("id_user", idUser);
                content.put("token_admin", tokenAdmin);
                content.put("message", message);
                DataMessage dataMessage = new DataMessage(token.getToken(), content);
                fcmRequest.sendMessage(dataMessage)
                        .enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body().getSuccess() == 1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailEventActivity.this);
                                    builder.setTitle("Pesan");
                                    builder.setMessage("Anda telah mengupdate event");
                                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            finish();
                                        }
                                    });
                                    builder.show();
                                }else{
                                    Toast.makeText(DetailEventActivity.this, "Gagal mengirirm", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                Log.d(TAG, "onFailure: ");
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadDataEvent() {
        Call<EventResponse> eventResponseCall = apiRequest.eventByIdRequest(idEvent);
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.body().getValue() == 1){
                    Event event = response.body().getEventList().get(0);
                    binding.setEvent(event);

                    String tanggal = DateFormat.format("EEEE, dd MMM yyyy", event.getTglEvent()).toString();
                    String jam = DateFormat.format("HH:mm", event.getTglEvent()).toString();

                    binding.setJam(jam);
                    binding.setTanggal(tanggal);
                    if (event.getStatus().equals("sedang proses")){
                        binding.layoutKonfirmasi.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    private void loadLampiran() {
        Call<List<Lampiran>> callLampiran = apiRequest.allLampiranRequest(idEvent);
        callLampiran.enqueue(new Callback<List<Lampiran>>() {
            @Override
            public void onResponse(Call<List<Lampiran>> call, Response<List<Lampiran>> response) {
                if (response.isSuccessful()){
                    List<Lampiran> lampiranList = response.body();
                    lampiranAdapter.setKajianList(lampiranList);
                }
            }

            @Override
            public void onFailure(Call<List<Lampiran>> call, Throwable t) {

            }
        });
    }
}