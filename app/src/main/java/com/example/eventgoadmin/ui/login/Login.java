package com.example.eventgoadmin.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.eventgoadmin.MainActivity;
import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ActivityLoginBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.util.Utils;

public class Login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    ApiRequest apiRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        sharedPreferences = getSharedPreferences(Utils.LOGIN_KEY, Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon tunggu");
        progressDialog.setMessage("Proses ...");
        progressDialog.setCancelable(false);


        cekLogin();
    }


    private void cekLogin() {
        progressDialog.show();
        boolean statusLogin = sharedPreferences.getBoolean(Utils.LOGIN_STATUS, false);
        if (statusLogin){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            progressDialog.dismiss();
            setFragment(new SignInFragment());
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLogin.getId(), fragment);
        transaction.commit();
    }

}