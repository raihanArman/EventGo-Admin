package com.example.eventgoadmin.ui.home;

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
import com.example.eventgoadmin.databinding.FragmentInboxBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.ui.adapter.InboxAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InboxFragment extends Fragment {


    ApiRequest apiRequest;
    FragmentInboxBinding binding;
    InboxAdapter inboxAdapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inbox, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);

        inboxAdapter = new InboxAdapter(getActivity());
        binding.rvEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvEvent.setAdapter(inboxAdapter);
    }


    private void loadData() {
        Call<EventResponse> eventResponseCall = apiRequest.eventInboxRequest();
        eventResponseCall.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                inboxAdapter.setEventResponseList(response.body().getEventList());
                if (response.body().getEventList().size() <= 0){
                    Toast.makeText(getActivity(), "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}