package com.example.victor.appsantotomas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EstadisticasActivity extends AppCompatActivity {
    int id;
    Button bt_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        bt_circular = findViewById(R.id.bt_circular);
        bt_circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EstadisticasActivity.this,CircularActivity.class);
                i.putExtra("ID_USUARIO_ACTUAL",id);
                startActivity(i);
            }
        });
    }


}
