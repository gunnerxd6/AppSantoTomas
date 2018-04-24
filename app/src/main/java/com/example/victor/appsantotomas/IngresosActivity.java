package com.example.victor.appsantotomas;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IngresosActivity extends AppCompatActivity {
    String fecha;
    EditText et_ingresos,et_detalle;
    Button bt_registrar_ingreso;
    int id;
    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        fecha = sdf.format(Calendar.getInstance().getTime());
        et_ingresos = findViewById(R.id.et_ingresos);
        et_detalle = findViewById(R.id.et_detalles);
        bt_registrar_ingreso = findViewById(R.id.bt_ingresos_registrar);
        bt_registrar_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarIngreso(et_ingresos,et_detalle,fecha,id);
            }
        });
    }

    private void registrarIngreso(EditText et_ingresos, EditText et_detalle, String fecha,int id_usuario){
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("DETALLE_INGRESO", et_detalle.getText().toString());
            contentValues.put("MONTO_INGRESO", et_ingresos.getText().toString());
            contentValues.put("FECHA_INGRESO", String.valueOf(fecha));
            contentValues.put("FK_ID_USUARIO", id_usuario);
            db.insert("INGRESOS", null, contentValues);
        }catch (Exception e){
            Log.e("ERROR INGRESO",e.getMessage().toString());
        }

        db.close();
    }

}
