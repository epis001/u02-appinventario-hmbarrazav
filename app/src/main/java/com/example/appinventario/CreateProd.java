package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    ProgressDialog progressDialog;
    String mediaPath="";
    ImageView imgView;
    TextView str1;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prod);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Subiendo...");
        imgView = (ImageView) findViewById(R.id.preview);
        str1 = (TextView) findViewById(R.id.miruta);

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
        if (edtnombre.getText().length() != 0 && edtprecio.getText().length() != 0 &&mediaPath.length()!=0) {
            subirimagen(view);
        }
        else Toast.makeText(CreateProd.this, "Debe Ingresar un nombre, un precio y seleccionar una imagen", Toast.LENGTH_SHORT).show();
    }
    void subirimagen(final View view){
        progressDialog.show(); view.setEnabled(false);
        file = new File(mediaPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> callupload = getResponse.uploadFile(fileToUpload, filename);
        callupload.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> callupload, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    Log.i("respuesta",serverResponse.getMessage());
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        //insertarproducto(view);
                    } else { Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show(); }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss(); view.setEnabled(true);
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) { }
        });
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
    public void imagen(View view) {
        Intent seleccionFotografiaIntent = new Intent();
        seleccionFotografiaIntent.setType("image/*");
        seleccionFotografiaIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(seleccionFotografiaIntent,
                "Seleccionar fotografía"),0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                mediaPath = getRealPathFromURI(selectedImage);
                str1.setText(mediaPath);
                imgView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
            } else {
                Toast.makeText(this, "tu no haz pulsado una imagen", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Hubo algun error", Toast.LENGTH_LONG).show();
        }
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}

