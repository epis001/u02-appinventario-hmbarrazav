package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CreateProd extends AppCompatActivity {
    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;
    EditText edtnombre;
    EditText edtprecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prod);
        final String url = getString(R.string.midominio);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        miserviceretrofit = retrofit.create(servicesRetrofit.class);
        edtnombre = findViewById(R.id.edtnomprod);
        edtprecio = findViewById(R.id.edtprecio);
    }

    public void nuevoproducto(final View view) {
        if (edtnombre.getText().length() != 0 && edtprecio.getText().length() != 0) {
            insertarproducto(view);
        } else
            Toast.makeText(CreateProd.this, "Debe Ingresar un nombre y un precio", Toast.LENGTH_SHORT).show();
    }

    void insertarproducto(final View view) {
        view.setEnabled(false);
        final Productos producto = new Productos(edtnombre.getText().toString(),
                Integer.parseInt(edtprecio.getText().toString()));
        Call<String> call = miserviceretrofit.newproducto(producto);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("newproducto: ", "check:" + response.body());
                edtnombre.setText("");
                edtprecio.setText("");
                view.setEnabled(true);
                Toast.makeText(CreateProd.this, "Inserción Exitosa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("newproducto", t.toString());
            }
        });
    }
}

