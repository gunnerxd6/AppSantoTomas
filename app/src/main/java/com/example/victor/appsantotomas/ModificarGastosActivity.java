package com.example.victor.appsantotomas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ModificarGastosActivity extends AppCompatActivity {
    ListView lv_gastos;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_gastos);
        lv_gastos = findViewById(R.id.lv_gastos);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        consultarGastos(id);
    }
    private void consultarGastos(int id){
        ArrayList<Gastos> gastos = new ArrayList<Gastos>();
        Gastos gastos1 = null;
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql_obtener_gastos = "SELECT * FROM EGRESOS WHERE FK_ID_USUARIO="+id;
        Cursor c = db.rawQuery(sql_obtener_gastos,null);
        while (c.moveToNext()){
            gastos1 = new Gastos();
            gastos1.setId_egreso(c.getInt(0));
            gastos1.setDetalle(c.getString(2));
            gastos1.setMonto(c.getInt(3));
            gastos1.setFecha(c.getString(4));
            gastos1.setId_usuario(c.getInt(1));
            gastos1.setId_tipo_gasto(c.getInt(5));
            gastos.add(gastos1);
        }
        db.close();
        ArrayList<String> informacion = new ArrayList<>();
        for (int i = 0; i<gastos.size();i++){
            String cadena = "Detalle: "+gastos.get(i).getDetalle()+"\nFecha: "+gastos.get(i).getFecha()+"\nMonto: "+gastos.get(i).getMonto();
            informacion.add(cadena);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,informacion);
        lv_gastos.setAdapter(adapter);

    }
}
