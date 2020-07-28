package com.example.eventgoadmin.ui.home;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.eventgoadmin.databinding.FragmentProfilBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.ui.adapter.RiwayatAdapter;
import com.example.eventgoadmin.ui.login.Login;
import com.example.eventgoadmin.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilFragment extends Fragment {


    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String idUser;
    FragmentProfilBinding binding;
    RiwayatAdapter riwayatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profil, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        idUser = sharedPreferences.getString(Utils.ID_USER_KEY, "");

        riwayatAdapter = new RiwayatAdapter(getActivity());
        binding.rvRiwayat.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvRiwayat.setAdapter(riwayatAdapter);


        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutProses();
            }
        });
    }

    private void loadRiwayat(){
        Call<EventResponse> eventResponseCall = apiRequest.riwayatUserRequest();
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                riwayatAdapter.setEventResponseList(response.body().getEventList());
                if (response.body().getEventList().size() <= 0){
                    Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadRiwayat();
    }

    private void logoutProses() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();

        editor.putInt(Utils.ID_USER_KEY, 0);
        editor.putBoolean(Utils.LOGIN_STATUS, false);
        editor.commit();
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

}