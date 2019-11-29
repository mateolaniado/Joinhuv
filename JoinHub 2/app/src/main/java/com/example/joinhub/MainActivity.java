package com.example.joinhub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void PresionarBoton (View VistaRecibida){
        Log.d("Menu", "Me voy a listado");
        Intent buscar;
        buscar = new Intent(MainActivity.this, datos.class);
        startActivity(buscar);
    }



}
