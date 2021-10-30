package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DetalleProducto extends AppCompatActivity {
    Retrofit retrofit; servicesRetrofit miserviceretrofit;
    EditText edtnombre; EditText edtprecio; Productos miproducto; ImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        final String url = getString(R.string.midominio); Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
        edtnombre=findViewById(R.id.edtnomprod); edtprecio=findViewById(R.id.edtprecio);
        preview=findViewById(R.id.preview);
        buscar(getIntent().getExtras().getString("midato"));
    }
    public void buscar(final String mibusqueda) {
        Call<Productos> call = miserviceretrofit.getproducto(mibusqueda);
        call.enqueue(new Callback<Productos>() {
            @Override
            public void onResponse(Call<Productos> call, Response<Productos> response) {
                miproducto = new Productos(response.body().getName(), response.body().getPrice(),
                        response.body().getId(), response.body().getImg());
                edtnombre.setText(mibusqueda); edtprecio.setText(String.valueOf(miproducto.getPrice()));
                Glide.with(DetalleProducto.this).load(miproducto.getImg()).into(preview);
            }
            @Override
            public void onFailure(Call<Productos> call, Throwable t) { Log.e("cargarproducto",t.toString()); }
        });
    }
}