package com.example.joinhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class listado extends AppCompatActivity {
    ArrayList<String> lista;
    ListView listadecosas;
    ArrayAdapter miadaptador;
    ArrayList<String> idLista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lista = new ArrayList<String>();
        idLista = new ArrayList<String>();
        Log.d("LecturaJSON", "Entro a listado");
        setContentView(R.layout.activity_listado);
        lista = new ArrayList<>();
        listadecosas = findViewById(R.id.miListaDeCosas);
        listadecosas.setOnItemClickListener(escuchadorparaListView);
        tareaAsincronica miTarea = new tareaAsincronica();
        miTarea.execute();
    }

    AdapterView.OnItemClickListener escuchadorparaListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Acceso API", "Posicion seleccionada:" + position);
            Log.d("Acceso API", "Elemento seleccinado:" + lista.get(position));
            Log.d("Acceso API", "id Seleccionado:" + idLista.get(position));
            Intent buscar;
            buscar = new Intent(listado.this, listado.class);
            startActivity(buscar);



        }
    };


    public void procesarJSONleido(InputStreamReader streamLeido) {
        Log.d("LecturaJSON", "Entro");
        try {

            //JsonParser parser=new JsonParser();
            //JsonObject jsonObject=parser.parse(streamLeido).getAsJsonObject();

            JsonParser parseadosDelJson;
            parseadosDelJson = new JsonParser();

            JsonArray objSonCrudo;
            objSonCrudo = parseadosDelJson.parse(streamLeido).getAsJsonArray();


            JsonArray arreventos;
            arreventos = objSonCrudo.getAsJsonArray();
          //  JsonArray listadecosasJSON = jsonObject.getAsJsonArray();

            for (int punteroevento = 0; punteroevento<arreventos.size(); punteroevento++) {
                //JsonObject evento = listadecosasJSON.getAsJsonObject(punteroevento);
                JsonObject evento;
                evento = arreventos.get(punteroevento).getAsJsonObject();

                String id;
                id = evento.get("id").getAsString();
                int idusuario;
                idusuario = evento.get("idusuario").getAsInt();
                int idcategoria;
                idcategoria = evento.get("idcategoria").getAsInt();
                int idsubcategoria;
                idsubcategoria = evento.get("idsubcategoria").getAsInt();
                String Nombre;
                Nombre = evento.get("Nombre").getAsString();
                String Descripcion;
                Descripcion = evento.get("Descripcion").getAsString();
                String Cupo;
                Cupo = evento.get("Cupo").getAsString();
                String Restricciones;
                Restricciones = evento.get("Restricciones").getAsString();
                Log.d("Listado", "idevento: "+ id + " idusuario: " + idusuario + " categoria: " + idcategoria + " Subcategoria: "+ idsubcategoria + " Nombre: "+ Nombre +" Descripcion: " + Descripcion + " Cupo: "+ Cupo+ " Restricciones: "+ Restricciones);
                lista.add(Nombre + " | " + Descripcion + " | " + " Cupo: " + Cupo);
                idLista.add(id);

            }
            miadaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            listadecosas.setAdapter(miadaptador);

        } catch (Exception error) {
            Log.d("LecturaJSON", "ERROR" + error.getMessage());
        }
    }

    class tareaAsincronica extends AsyncTask<Void, Void, Void> {

        String miPC = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL miRuta = new URL("http://10.0.2.2:60180" + "/api/ObtenerEventos");
                HttpURLConnection miConexion = (HttpURLConnection) miRuta.openConnection();
                Log.d("AccesoApi", "Conecto");
                if (miConexion.getResponseCode() == 200) {
                    Log.d("AccesoApi", "Conexion OK");
                    InputStream cuerpoRespuesta = miConexion.getInputStream();
                    InputStreamReader lectorRespuesta = new InputStreamReader(cuerpoRespuesta, "UTF-8");
                    JsonReader JSONleido = new JsonReader(lectorRespuesta);
                    procesarJSONleido(lectorRespuesta);

                } else {

                    Log.d("AccesoApi", "error");
                }
                miConexion.disconnect();
            } catch (Exception Error) {
                Log.d("AccesoApi", "ERROR de conexion" + Error.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listadecosas.setAdapter(miadaptador);
        }
    }
}


