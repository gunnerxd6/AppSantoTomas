package com.example.victor.appsantotomas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MenuActivity extends AppCompatActivity {
    //declaracion de variables del tipo FloatingActionButton y ImageView
    FloatingActionButton fab_agregargastos, fab_ingresos, fab_modificargastos, fab_estadisticas;
    ImageView iv_salir, iv_configuracion;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Obtener id de usuario logeado
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        //Floatingactionbutton
        fab_ingresos = findViewById(R.id.fab_ingresos);
        fab_agregargastos = findViewById(R.id.fab_agregargastos);
        fab_modificargastos = findViewById(R.id.fab_modificargastos);
        fab_estadisticas = findViewById(R.id.fab_estadisticas);

        //ImageView
        iv_configuracion = findViewById(R.id.iv_configuracion);
        iv_salir = findViewById(R.id.iv_salir);


        fab_ingresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, IngresosActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL", id);
                startActivity(i);
            }
        });
        fab_agregargastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AgregarGastosActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL", id);
                startActivity(i);
            }
        });
        fab_modificargastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, ModificarGastosActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL", id);
                startActivity(i);
            }
        });
        fab_estadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, EstadisticasActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL", id);
                startActivity(i);
            }
        });
        iv_configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, ConfiguracionActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL", id);
                startActivity(i);
            }
        });

        iv_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle("¿Salir?");
                builder.setMessage("Salir a la pantalla inicial");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent o = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(o);
                        id = 0;
                        MenuActivity.this.finish();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        builder.setTitle("¿Salir?");
        builder.setMessage("Salir a la pantalla inicial");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent o = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(o);
                id = 0;
                MenuActivity.this.finish();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();

        // super.onBackPressed();
    }
}