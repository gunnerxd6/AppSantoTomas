package com.example.victor.appsantotomas;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.support.design.widget.FloatingActionButton;


public class MenuActivity extends AppCompatActivity {
    //declaracion de variables del tipo FloatingActionButton y ImageView
    FloatingActionButton fab_agregargastos,fab_ingresos,fab_modificargastos,fab_estadisticas;
    ImageView iv_salir,iv_configuracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Floatingactionbutton
        fab_ingresos=findViewById(R.id.fab_ingresos);
        fab_agregargastos=findViewById(R.id.fab_agregargastos);
        fab_modificargastos=findViewById(R.id.fab_modificargastos);
        fab_estadisticas=findViewById(R.id.fab_estadisticas);

        //ImageView
        iv_configuracion=findViewById(R.id.iv_configuracion);
        iv_salir=findViewById(R.id.iv_salir);

    }
}