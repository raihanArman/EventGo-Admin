package com.example.eventgoadmin.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.eventgoadmin.R;
import com.example.eventgoadmin.databinding.ActivityKategoriMoreBinding;
import com.example.eventgoadmin.databinding.AlertAddKategoriBinding;
import com.example.eventgoadmin.request.ApiRequest;
import com.example.eventgoadmin.request.RetrofitRequest;
import com.example.eventgoadmin.request.model.Kategori;
import com.example.eventgoadmin.request.model.Value;
import com.example.eventgoadmin.request.model.response.KategoriResponse;
import com.example.eventgoadmin.ui.adapter.KategoriMoreAdapter;
import com.example.eventgoadmin.util.ConvertBitmap;
import com.example.eventgoadmin.util.EditKategoriListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriMoreActivity extends AppCompatActivity implements ConvertBitmap, EditKategoriListener {

    ActivityKategoriMoreBinding binding;
    AlertAddKategoriBinding bindingAlert;
    KategoriMoreAdapter kategoriMoreAdapter;
    ApiRequest apiRequest;
    Bitmap bitmap = null;
    String pamflet = null;
    ProgressDialog progressDialog;
    boolean hasPamflet = false;


    public static final int GALLERY_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_more);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kategori_more);

        apiRequest = RetrofitRequest.getInstance().create(ApiRequest.class);
        kategoriMoreAdapter = new KategoriMoreAdapter(this, this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");

        binding.rvKategori.setLayoutManager(new LinearLayoutManager(this));
        binding.rvKategori.setAdapter(kategoriMoreAdapter);

        loadData();


        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAdd();
            }
        });

    }

    private void showDialogAdd() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Input Kategori");
        LayoutInflater inflater =getLayoutInflater();
        bindingAlert = DataBindingUtil.inflate(inflater, R.layout.alert_add_kategori, null, false);
        builder.setView(bindingAlert.getRoot());
        bindingAlert.tvGantiKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        bindingAlert.layoutBtnAddImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });
        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hasPamflet){
                    if (!TextUtils.isEmpty(bindingAlert.etKategori.getText())){
                        prosesInput(dialog);
                    }else {
                        Toast.makeText(KategoriMoreActivity.this, "Isi kategori", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(KategoriMoreActivity.this, "Masukkan gambar kategori", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void prosesInput(DialogInterface dialog) {
        Call<Value> valueCall = apiRequest.inputKategoriRequest(
                bindingAlert.etKategori.getText().toString(),
                pamflet
        );
        valueCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                Toast.makeText(KategoriMoreActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getValue() == 1){
                    dialog.dismiss();
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(KategoriMoreActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void bitmapToString(String imgConvert) {
//        Toast.makeText(getActivity(), ""+imgConvert, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        pamflet = imgConvert;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE){
            if (resultCode == RESULT_OK && data != null){
                hasPamflet = true;
                Uri imageUri = data.getData();
                bindingAlert.ivKategori.setVisibility(View.VISIBLE);
                bindingAlert.layoutBtnAddImage2.setVisibility(View.INVISIBLE);
                bindingAlert.tvGantiKategori.setVisibility(View.VISIBLE);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new LoadBitmapConvertAsync(this, KategoriMoreActivity.this::bitmapToString).execute();
                bindingAlert.ivKategori.setImageBitmap(bitmap);

            }
        }
    }

    @Override
    public void showDialogEdit(String idKategori) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Edit Kategori");
        LayoutInflater inflater =getLayoutInflater();
        bindingAlert = DataBindingUtil.inflate(inflater, R.layout.alert_add_kategori, null, false);
        builder.setView(bindingAlert.getRoot());

        setDataKategori(idKategori);

        bindingAlert.tvGantiKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        bindingAlert.layoutBtnAddImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hasPamflet){
                    if (!TextUtils.isEmpty(bindingAlert.etKategori.getText())){
                        prosesEdit(dialog, idKategori);
                    }else {
                        Toast.makeText(KategoriMoreActivity.this, "Isi kategori", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(KategoriMoreActivity.this, "Masukkan gambar kategori", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void showDialogHapus(String idKategori) {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Edit Kategori");
        builder.setMessage("Apakah anda yakin ingin menghapus kategori ini ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prosesHapus(dialog, idKategori);
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void prosesHapus(DialogInterface dialog, String idKategori) {
        Call<Value> valueCall = apiRequest.hapusKategoriRequest(idKategori);
        valueCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                Toast.makeText(KategoriMoreActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getValue() == 1){
                    dialog.dismiss();
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(KategoriMoreActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void prosesEdit(DialogInterface dialog, String idKategori) {
        Call<Value> valueCall = apiRequest.editKategoriRequest(
                idKategori,
                bindingAlert.etKategori.getText().toString(),
                pamflet
        );
        valueCall.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                Toast.makeText(KategoriMoreActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                if (response.body().getValue() == 1){
                    dialog.dismiss();
                    loadData();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(KategoriMoreActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void setDataKategori(String idKategori) {
        Call<KategoriResponse> kategoriResponseCall = apiRequest.kategoriRequest(idKategori);
        kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.isSuccessful()){
                    Kategori kategori = response.body().getKategoriList().get(0);
                    bindingAlert.etKategori.setText(kategori.getNamaKategori());
                    bindingAlert.layoutBtnAddImage2.setVisibility(View.INVISIBLE);
                    bindingAlert.tvGantiKategori.setVisibility(View.VISIBLE);
                    bindingAlert.ivKategori.setVisibility(View.VISIBLE);
                    bindingAlert.setGambar(kategori.getGambar());
                    hasPamflet = true;
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {
                Toast.makeText(KategoriMoreActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class LoadBitmapConvertAsync extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> weakContext;
        ConvertBitmap convertBitmap;

        public LoadBitmapConvertAsync(Context context, ConvertBitmap convertBitmap) {
            this.weakContext = new WeakReference<>(context);
            this.convertBitmap = convertBitmap;
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String imageBitmap = Base64.encodeToString(imgByte, Base64.DEFAULT);
            return imageBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakContext.get();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            convertBitmap.bitmapToString(s);
        }
    }

    private void loadData() {
        Call<KategoriResponse> kategoriResponseCall = apiRequest.kategoriAllRequest();
        kategoriResponseCall.enqueue(new Callback<KategoriResponse>() {
            @Override
            public void onResponse(Call<KategoriResponse> call, Response<KategoriResponse> response) {
                if (response.body().getValue() == 1) {
                    kategoriMoreAdapter.setKategoriList(response.body().getKategoriList());
                } else {
                    Toast.makeText(KategoriMoreActivity.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KategoriResponse> call, Throwable t) {

            }
        });
    }
}