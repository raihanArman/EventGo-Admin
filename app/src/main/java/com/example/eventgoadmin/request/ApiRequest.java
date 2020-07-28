package com.example.eventgoadmin.request;


import com.example.eventgoadmin.request.model.Event;
import com.example.eventgoadmin.request.model.EventResponse;
import com.example.eventgoadmin.request.model.Home;
import com.example.eventgoadmin.request.model.Lampiran;
import com.example.eventgoadmin.request.model.Pesan;
import com.example.eventgoadmin.request.model.Value;
import com.example.eventgoadmin.request.response.KategoriResponse;
import com.example.eventgoadmin.request.response.LoginResponse;
import com.example.eventgoadmin.request.response.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiRequest {

    @GET("tampil_kategori.php")
    Call<KategoriResponse> kategoriAllRequest();

    @GET("tampil_event.php")
    Call<EventResponse> eventSearchRequest(
            @Query("cari") String cari
    );

    @GET("tampil_event.php")
    Call<EventResponse> eventAllRequest();

    @GET("admin/tampil_riwayat.php")
    Call<EventResponse> riwayatUserRequest();

    @POST("admin/input_event.php")
    Call<Value> inputEvent(
            @Body Event event
    );

    @GET("tampil_aktivitas.php")
    Call<EventResponse> aktivitasByIdRequest(
            @Query("id_event") String idEvent
    );

    @POST("admin/edit_event.php")
    Call<Value> editEventRequest(
            @Body Event event
    );

    @FormUrlEncoded
    @POST("admin/login_admin.php")
    Call<LoginResponse> loginUserRequest(
            @Field("username") String email,
            @Field("password") String password
    );

    @GET("admin/home_admin.php")
    Call<Home> homeRequest();

    @GET("admin/tampil_inbox.php")
    Call<EventResponse> eventInboxRequest(
    );

    @GET("admin/tampil_user.php")
    Call<UserResponse> userAllRequest(
    );

    @GET("admin/tampil_event.php")
    Call<EventResponse> eventByIdRequest(
            @Query("id_event") String idEvent
    );

    @GET("tampil_lampiran.php")
    Call<List<Lampiran>> allLampiranRequest(
            @Query("id_event") String id_event
    );


    @GET("tampil_user.php")
    Call<UserResponse> userRequest(
            @Query("id_user") String idUser
    );

    @POST("admin/update_event.php")
    Call<Value> updateEventRequest(
            @Body Event event
    );

    @FormUrlEncoded
    @POST("admin/input_pesan.php")
    Call<Value> inputPesanRequest(
            @Field("id_user") String id_user,
            @Field("id_event") String id_event,
            @Field("pesan") String pesan
    );

    @GET("tampil_event.php")
    Call<EventResponse> eventByUserRequest(
            @Query("id_user") String idUser
    );

    @FormUrlEncoded
    @POST("admin/update_user.php")
    Call<Value> updateUserRequest(
            @Field("id_user") String idUser,
            @Field("status") String status
    );

//    Crud kategori
    @FormUrlEncoded
    @POST("admin/input_kategori.php")
    Call<Value> inputKategoriRequest(
            @Field("nama_kategori") String namaKategori,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("admin/edit_kategori.php")
    Call<Value> editKategoriRequest(
            @Field("id_kategori") String idKategori,
            @Field("nama_kategori") String namaKategori,
            @Field("gambar") String gambar
    );

    @FormUrlEncoded
    @POST("admin/hapus_kategori.php")
    Call<Value> hapusKategoriRequest(
            @Field("id_kategori") String idKategori
    );

    @FormUrlEncoded
    @POST("admin/hapus_riwayat.php")
    Call<Value> hapusRiwayatRequest(
            @Field("id_riwayat_admin") String idRiwayat
    );

    @GET("admin/tampil_kategori.php")
    Call<KategoriResponse> kategoriRequest(
            @Query("id_kategori") String idKategori
    );

}
