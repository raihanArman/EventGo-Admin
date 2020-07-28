package com.example.eventgoadmin.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ActivityEventAllBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.ui.adapter.ExploreAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAllActivity extends AppCompatActivity {

    ActivityEventAllBinding binding;
    ApiRequest apiRequest;
    ExploreAdapter exploreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_all);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event_all);


        binding.btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPencarian();
            }
        });

        loadDataSemua();

        binding.setTitle("Semua Event");

    }

    private void loadPencarian() {
        String cari = binding.etCari.getText().toString();
        Call<EventResponse> eventResponseCall = apiRequest.eventSearchRequest(cari);
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.body().getValue() == 1){
                    exploreAdapter.setEventList(response.body().getEventList());
                    binding.tvHasil.setVisibility(View.VISIBLE);
                    binding.tvHasil.setText("Hasil pencarian '"+cari+"' ...");

                    if (response.body().getEventList().size() <= 0){
                        Toast.makeText(EventAllActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });

    }

    private void loadDataSemua() {
        Call<EventResponse> eventResponseCall = apiRequest.eventAllRequest();
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.body().getValue() == 1){
                    exploreAdapter.setEventList(response.body().getEventList());
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

}