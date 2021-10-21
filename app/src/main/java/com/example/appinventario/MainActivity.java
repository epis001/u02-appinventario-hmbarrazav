package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends FragmentActivity {
    private FloatingActionButton fabaddproduct;
    private static final int RC_CREATE_PRODUCT = 1;
    private static final int RC_UPDATE_PRODUCT = 2;
    Retrofit retrofit; 
    servicesRetrofit miserviceretrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabaddproduct = findViewById(R.id.fabaddProduct);
        fabaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateProd.class);
                startActivityForResult(intent, RC_CREATE_PRODUCT);
            }
        });
        final String url = getString(R.string.midominio);
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
        cargarproductos();
    }

    private void cargarproductos() {
        Call<List<Productos>> call = miserviceretrofit.getlist();
        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                Log.e("mirespuesta: ", response.toString());
                for(Productos res : response.body()) {
                    Log.e("mirespuesta: ","id="+res.getId()+" prod="+res.getName() +" precio"+res.getPrice());
                }
            }
            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {Log.e("onFailure", t.toString()); }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CREATE_PRODUCT) {
            cargarproductos();
        } else if (requestCode == RC_UPDATE_PRODUCT) {
            cargarproductos();
        }
    }
}
