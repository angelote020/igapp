package com.angelote.testimagenes.Interfaces;

import com.angelote.testimagenes.Clases.ImagenesServicio;
import com.angelote.testimagenes.Clases.Photos;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @GET("photos/")
    Call<ArrayList<ImagenesServicio>> ObtenerImagenes(@QueryMap Map<String, String> params);

    @GET
    Call<ArrayList<Photos>> ObtenerUsuario(@Url String url);
}
