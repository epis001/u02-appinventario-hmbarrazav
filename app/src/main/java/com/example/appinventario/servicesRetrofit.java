package com.example.appinventario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface servicesRetrofit {
    @POST("products/create")
    Call<String> newproducto(@Body Productos producto);
}