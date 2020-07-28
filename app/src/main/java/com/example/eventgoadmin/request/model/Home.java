package com.example.eventgoadmin.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Home {
    @SerializedName("jum_kategori")
    @Expose
    private String jumKategori;

    @SerializedName("jum_masuk")
    @Expose
    private String jumMasuk;

    @SerializedName("jum_terima")
    @Expose
    private String jumTerima;

    @SerializedName("jum_tolak")
    @Expose
    private String jumTolak;

    @SerializedName("jum_user")
    @Expose
    private String jumUser;

    public Home() {
    }

    public Home(String jumKategori, String jumMasuk, String jumTerima, String jumTolak, String jumUser) {
        this.jumKategori = jumKategori;
        this.jumMasuk = jumMasuk;
        this.jumTerima = jumTerima;
        this.jumTolak = jumTolak;
        this.jumUser = jumUser;
    }

    public String getJumKategori() {
        return jumKategori;
    }

    public void setJumKategori(String jumKategori) {
        this.jumKategori = jumKategori;
    }

    public String getJumMasuk() {
        return jumMasuk;
    }

    public void setJumMasuk(String jumMasuk) {
        this.jumMasuk = jumMasuk;
    }

    public String getJumTerima() {
        return jumTerima;
    }

    public void setJumTerima(String jumTerima) {
        this.jumTerima = jumTerima;
    }

    public String getJumTolak() {
        return jumTolak;
    }

    public void setJumTolak(String jumTolak) {
        this.jumTolak = jumTolak;
    }

    public String getJumUser() {
        return jumUser;
    }

    public void setJumUser(String jumUser) {
        this.jumUser = jumUser;
    }
}
