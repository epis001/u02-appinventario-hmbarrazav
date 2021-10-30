package com.example.appinventario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface servicesRetrofit {
    @POST("products/create")
    Call<String> newproducto(@Body Productos producto);
    @GET("products/list")
    Call<List<Productos>> getlist();
    @GET("/products/buscar/{name}")
    Call < Productos > getproducto(@Path("name") String name);
    @PUT("/products/{id}/update")
    Call<String> updateproducto(@Path("id") String id,@Body Productos producto);
    @DELETE("/products/{id}/delete")
    Call<String> deleteproducto(@Path("id") String id);

}