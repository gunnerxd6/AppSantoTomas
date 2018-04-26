package com.example.victor.appsantotomas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModificarGastosActivity extends AppCompatActivity {
    ListView lv_gastos;
    ArrayList<Gastos> gastos;
    ArrayList<String> informacion;
    TextView tv_datepicker;
    SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialog.OnDateSetListener onDateSetListener;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_gastos);
        lv_gastos = findViewById(R.id.lv_gastos);
        tv_datepicker = findViewById(R.id.tv_datepicker);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        consultarGastos(id,lv_gastos);
        lv_gastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaseHelper helper = new BaseHelper(getApplicationContext(), "db_gastos", null, 1);
                final SQLiteDatabase db = helper.getWritableDatabase();
                final Dialog dialog = new Dialog(ModificarGastosActivity.this);
                dialog.setContentView(R.layout.dialog_modificar_gastos);
                dialog.setTitle("");
                final int id_egreso = gastos.get(i).getId_egreso();
                Button bt_dialog_modificar = dialog.findViewById(R.id.bt_dialog_modificar);
                Button bt_dialog_eliminar = dialog.findViewById(R.id.bt_dialog_eliminar);
                final EditText et_dialog_detalle = dialog.findViewById(R.id.et_dialog_detalle);
                final EditText et_dialog_monto = dialog.findViewById(R.id.et_dialog_monto);
                final Spinner s_dialog_tipo = dialog.findViewById(R.id.s_dialog_tipo);
                consultarListaDeTiposDeGasto(id,s_dialog_tipo);
                et_dialog_detalle.setText(gastos.get(i).getDetalle());
                et_dialog_monto.setText(String.valueOf(gastos.get(i).getMonto()));
                bt_dialog_modificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean ingresar = true;
                        if (s_dialog_tipo.getSelectedItem().toString().equals(getResources().getString(R.string.seleccione))){
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.seleccionar_tipo),Toast.LENGTH_SHORT).show();
                            ingresar = false;
                        }
                        if (et_dialog_detalle.getText().toString().equals("") || et_dialog_monto.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.completar_campos),Toast.LENGTH_SHORT).show();
                            ingresar = false;
                        }
                        if (ingresar){

                            try {
                                String sql_modificar_gasto = "UPDATE EGRESOS SET DETALLE_EGRESO = '" + et_dialog_detalle.getText().toString() + "', MONTO_EGRESO =" +
                                        "" + et_dialog_monto.getText().toString() + ", FK_TIPO_EGRESO ="+obtenerIdGasto(s_dialog_tipo)+" WHERE ID_EGRESO =" + id_egreso;
                                db.execSQL(sql_modificar_gasto);
                                consultarGastos(id, lv_gastos);
                                Toast.makeText(getApplicationContext(), "Gasto modificado", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }catch (Exception e){
                                Toast.makeText(getApplicationContext(),"Error al modificar gasto",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                bt_dialog_eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(ModificarGastosActivity.this);
                        builder.setTitle("¿Eliminar?");
                        builder.setMessage("Eliminar registro del gasto");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                builder.show();
                                eliminarGasto(id_egreso);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.show();
                    }


                });

                dialog.show();
            }
        });

        tv_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int año = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(ModificarGastosActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,
                onDateSetListener,año,mes,dia);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
});

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
@Override
public void onDateSet(DatePicker datePicker, int año, int mes, int dia) {
    mes = mes + 1;
    String mes_fix = "";
    if (mes < 10) {
        mes_fix = "0" + mes;
        mes = Integer.valueOf(mes_fix);
    }
    String fecha = año + "-" + mes_fix + "-" + dia;
    Log.d("Fecha", fecha);
        consultarGastosPorFecha(id,lv_gastos,fecha);
        }
        };

    }

    private void consultarGastos(int id,ListView lv_gastos){
        gastos = new ArrayList<>();
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
        informacion = new ArrayList<>();
        for (int i = 0; i<gastos.size();i++){
            String detalle = obtenerDetalleGasto(gastos.get(i).getId_tipo_gasto());
            String cadena = "Detalle: "+gastos.get(i).getDetalle()+"\nFecha: "+gastos.get(i).getFecha()+"\nMonto: "+gastos.get(i).getMonto()+
                    "\nTipo: "+detalle;
            informacion.add(cadena);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,informacion);
        lv_gastos.setAdapter(adapter);

    }


    private void consultarListaDeTiposDeGasto(int id, Spinner s_tipogastos){
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> tipos_gasto = new ArrayList<String>();
        tipos_gasto.add(getResources().getString(R.string.seleccione));
        for(int i = 0; i<tipos_gasto.size();i++){
            Log.e("Item","Item: "+tipos_gasto.get(i));
        }

        String sql = "SELECT DETALLE_TIPO_EGRESO FROM TIPO_EGRESO WHERE FK_ID_USUARIO ="+id;
        Cursor c = db.rawQuery(sql,null);
        while(c.moveToNext()){
            tipos_gasto.add(c.getString(c.getColumnIndex("DETALLE_TIPO_EGRESO")));
            Log.e("Valor tipo",""+c.getString(c.getColumnIndex("DETALLE_TIPO_EGRESO")));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,tipos_gasto);
        s_tipogastos.setAdapter(adapter);
    }
    private int obtenerIdGasto(Spinner s){
        int id_gasto = 0;
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql_obtener_id_gasto = "SELECT ID_TIPO_EGRESO FROM TIPO_EGRESO WHERE DETALLE_TIPO_EGRESO = '"
                +s.getSelectedItem().toString()+"'";
        Cursor c = db.rawQuery(sql_obtener_id_gasto,null);
        if (c.moveToFirst()){
            id_gasto = c.getInt(c.getColumnIndex("ID_TIPO_EGRESO"));
        }
        return id_gasto;
    }
    private String obtenerDetalleGasto(int id_gasto){
        String detalle = "";
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql_obtener_id_gasto = "SELECT DETALLE_TIPO_EGRESO FROM TIPO_EGRESO WHERE ID_TIPO_EGRESO = "+id_gasto;
        Cursor c = db.rawQuery(sql_obtener_id_gasto,null);
        if (c.moveToFirst()){
            detalle = c.getString(c.getColumnIndex("DETALLE_TIPO_EGRESO"));
        }
        return detalle;
    }

    private void eliminarGasto(int id_gasto){
        try {
            BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            String sql_eliminar = "DELETE FROM EGRESOS WHERE ID_EGRESO=" + id_gasto;
            db.execSQL(sql_eliminar);
            consultarGastos(id, lv_gastos);
        }catch (Exception e){

        }
    }

    private void consultarGastosPorFecha(int id,ListView lv_gastos,String fecha){
        ArrayList<Gastos>gastos2 = new ArrayList<>();
        Gastos gastos1 = null;
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql_obtener_gastos = "SELECT * FROM EGRESOS WHERE FK_ID_USUARIO="+id+" AND FECHA_EGRESO ='"+fecha+"'";
        Cursor c = db.rawQuery(sql_obtener_gastos,null);
        while (c.moveToNext()){
            gastos1 = new Gastos();
            gastos1.setId_egreso(c.getInt(0));
            gastos1.setDetalle(c.getString(2));
            gastos1.setMonto(c.getInt(3));
            gastos1.setFecha(c.getString(4));
            gastos1.setId_usuario(c.getInt(1));
            gastos1.setId_tipo_gasto(c.getInt(5));
            gastos2.add(gastos1);
        }
        db.close();
        ArrayList<String>informacion1 = new ArrayList<>();
        for (int i = 0; i<gastos2.size();i++){
            String detalle = obtenerDetalleGasto(gastos2.get(i).getId_tipo_gasto());
            String cadena = "Detalle: "+gastos2.get(i).getDetalle()+"\nFecha: "+gastos2.get(i).getFecha()+"\nMonto: "+gastos2.get(i).getMonto()+
                    "\nTipo: "+detalle;
            informacion1.add(cadena);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,informacion1);
        lv_gastos.setAdapter(adapter);

    }



}
