package com.example.appinventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends FragmentActivity {
    private FloatingActionButton fabaddproduct;
    private static final int RC_CREATE_PRODUCT = 1;
    private static final int RC_UPDATE_PRODUCT = 2;
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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CREATE_PRODUCT) {
            //cargarproductos();
        } else if (requestCode == RC_UPDATE_PRODUCT) {
            // cargarproductos();
        }
    }
}
