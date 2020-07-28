package com.example.eventgoadmin.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.FragmentHomeBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.request.model.Home;
import com.example.eventgoadmin.request.response.KategoriResponse;
import com.example.eventgoadmin.ui.EventAllActivity;
import com.example.eventgoadmin.ui.KategoriMoreActivity;
import com.example.eventgoadmin.ui.adapter.EventAdapter;
import com.example.eventgoadmin.ui.adapter.KategoriAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    ApiRequest apiRequest;
    EventAdapter eventAdapter;
    KategoriAdapter kategoriAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);

        eventAdapter = new EventAdapter(getActivity());
        kategoriAdapter = new KategoriAdapter(getActivity());

        LinearLayoutManager layoutManagerHorizontal1 = new LinearLayoutManager(getActivity());
        layoutManagerHorizontal1.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvEvent.setLayoutManager(layoutManagerHorizontal1);
        binding.rvEvent.setAdapter(eventAdapter);

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getActivity());
        layoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.rvKategori.setLayoutManager(layoutManagerHorizontal);
        binding.rvKategori.setAdapter(kategoriAdapter);

        loadHome();
        loadRiwayat();
        loadDataKategori();

        binding.tvKategoriMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KategoriMoreActivity.class);
                startActivity(intent);
            }
        });

        binding.tvEventMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EventAllActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadDataKategori() {
        Call<KategoriResponse> kategoriResponseCall = apiRequest.kategoriAllRequest();
        kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.body().getValue() == 1) {
                    kategoriAdapter.setKategoriList(response.body().getKategoriList());
                } else {
                    Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {

            }
        });
    }

    private void loadRiwayat() {
        Call<EventResponse> eventResponseCall = apiRequest.eventAllRequest();
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.body().getValue() == 1) {
                    eventAdapter.setEventResponseList(response.body().getEventList());
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    private void loadHome() {
        Call<Home> homeCall = apiRequest.homeRequest();
        homeCall.enqueue(new Callback<Home>() {
            @Override
            public void onResponse(Call<Home> call, Response<Home> response) {
                Home home = response.body();
                binding.setHome(home);
            }

            @Override
            public void onFailure(Call<Home> call, Throwable t) {

            }
        });
    }
}