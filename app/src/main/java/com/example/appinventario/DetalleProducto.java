package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    public void Delete(View view) {
        Call<String> call = miserviceretrofit.deleteproducto(miproducto.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int poscode=miproducto.getImg().indexOf("uploads");
                String miimagen= miproducto.getImg().substring(poscode);
                miproducto.setImg(miimagen);
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                Call<ServerResponse> callupload = getResponse.delete(miproducto);
                callupload.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> callupload, Response<ServerResponse> response) {
                        ServerResponse serverResponse = response.body();
                        if (serverResponse != null) {
                            Log.i("mieliminacion", serverResponse.getMessage());
                            Toast.makeText(DetalleProducto.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                        } else {
                            assert serverResponse != null;
                            Log.v("Response", serverResponse.toString());
                        }
                        finish();
                    }
                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) { }
                });
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) { Log.e("deleteproducto",t.toString()); }
        });
    }
    public void update(View view) {
        final Productos producto= new Productos(edtnombre.getText().toString(),
                Integer.parseInt(edtprecio.getText().toString()));
        Call<String> call = miserviceretrofit.updateproducto(miproducto.getId(),producto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(DetalleProducto.this, "Datos del Producto Actualizados",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {Log.e("updateproducto",t.toString()); }
        });
    }
}