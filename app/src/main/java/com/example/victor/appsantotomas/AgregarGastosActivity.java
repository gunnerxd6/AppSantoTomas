package com.example.victor.appsantotomas;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AgregarGastosActivity extends AppCompatActivity {
    int id;
    EditText et_monto_gasto, et_detalle_gasto;
    Button bt_añadir;
    ImageView iv_agregargastos_anadir_tipo;
    Spinner s_tipogastos;
    String fecha;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fecha = sdf.format(Calendar.getInstance().getTime());
        setContentView(R.layout.activity_agregargastos);
        et_monto_gasto = findViewById(R.id.et_montoagregargastos);
        et_detalle_gasto = findViewById(R.id.et_detallesagregargastos);
        bt_añadir = findViewById(R.id.btn_anadir);
        s_tipogastos = findViewById(R.id.s_tipogastos);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        consultarListaDeTiposDeGasto(id);
        Toast.makeText(getApplicationContext(), "Id: " + id, Toast.LENGTH_SHORT).show();
        iv_agregargastos_anadir_tipo = findViewById(R.id.iv_agregargastos_añadir_tipo);
        iv_agregargastos_anadir_tipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(AgregarGastosActivity.this);
                dialog.setContentView(R.layout.dialog_registrar_tipo_gasto);
                dialog.setTitle("");
                final EditText et_tipo = dialog.findViewById(R.id.et_dialog_registrar_tipo);
                Button bt_agregar = dialog.findViewById(R.id.bt_registrar_tipo_agregar);
                Button bt_cancelar = dialog.findViewById(R.id.bt_registrar_tipo_cancelar);
                bt_agregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (et_tipo.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "El tipo no puede ser vacio", Toast.LENGTH_SHORT).show();
                        } else {
                            if (agregarTipoEgreso(et_tipo, id)) {
                                Toast.makeText(getApplicationContext(), "Tipo de gasto registrado", Toast.LENGTH_SHORT).show();
                                et_tipo.setText("");
                                dialog.dismiss();
                                consultarListaDeTiposDeGasto(id);
                            } else {
                                Toast.makeText(getApplicationContext(), "Error al ingresar tipo de gasto", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                bt_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        bt_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s_tipogastos.getSelectedItem().toString().equals(getResources().getString(R.string.seleccione))) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.seleccionar_tipo), Toast.LENGTH_SHORT).show();
                } else {
                    if (et_monto_gasto.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Debe ingresar un monto", Toast.LENGTH_SHORT);

                    } else {
                        registrarGasto(et_monto_gasto, et_detalle_gasto, s_tipogastos, id, fecha);
                    }
                }

            }
        });

    }

    private boolean agregarTipoEgreso(EditText tipoEgreso, int id_usuario) {
        Boolean ingresado;
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("DETALLE_TIPO_EGRESO", tipoEgreso.getText().toString());
            contentValues.put("FK_ID_USUARIO", id_usuario);
            db.insert("TIPO_EGRESO", null, contentValues);
            ingresado = true;
        } catch (Exception e) {
            Log.e("ERROR INGRESO", e.getMessage().toString());
            ingresado = false;
        }
        db.close();
        return ingresado;
    }

    private void consultarListaDeTiposDeGasto(int id) {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> tipos_gasto = new ArrayList<String>();
        tipos_gasto.add(getResources().getString(R.string.seleccione));
        for (int i = 0; i < tipos_gasto.size(); i++) {
            Log.e("Item", "Item: " + tipos_gasto.get(i));
        }

        String sql = "SELECT DETALLE_TIPO_EGRESO FROM TIPO_EGRESO WHERE FK_ID_USUARIO =" + id;
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            tipos_gasto.add(c.getString(c.getColumnIndex("DETALLE_TIPO_EGRESO")));
            Log.e("Valor tipo", "" + c.getString(c.getColumnIndex("DETALLE_TIPO_EGRESO")));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos_gasto);
        s_tipogastos.setAdapter(adapter);
    }

    private void registrarGasto(EditText monto, EditText detalle, Spinner tipo, int id, String fecha) {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        String tipo_seleccionado = tipo.getSelectedItem().toString();
        int idTipo = 0;
        String sql_obtener_id_gasto = "SELECT ID_TIPO_EGRESO FROM TIPO_EGRESO WHERE DETALLE_TIPO_EGRESO = '" + tipo_seleccionado + "'";
        Cursor c = db.rawQuery(sql_obtener_id_gasto, null);
        if (c.moveToFirst()) {
            idTipo = c.getInt(c.getColumnIndex("ID_TIPO_EGRESO"));
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("MONTO_EGRESO", monto.getText().toString());
                contentValues.put("FECHA_EGRESO", String.valueOf(fecha));
                contentValues.put("DETALLE_EGRESO", detalle.getText().toString());
                contentValues.put("FK_ID_USUARIO", id);
                contentValues.put("FK_TIPO_EGRESO", idTipo);
                db.insert("EGRESOS", null, contentValues);

                Toast.makeText(getApplicationContext(), R.string.gasto_registrado, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.error_al_registrar_tipo, Toast.LENGTH_SHORT);
        }
        db.close();
    }

}
