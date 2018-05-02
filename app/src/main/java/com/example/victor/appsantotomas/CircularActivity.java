package com.example.victor.appsantotomas;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CircularActivity extends AppCompatActivity {
    int id;
    ArrayList<String> tipos;
    ArrayList<Integer> montos;
    ArrayList<Integer> porcentajes;
    ArrayList<Integer> IDS;
    PieChart pieChart;
    Button bt_circular_global;
    ArrayList<PieEntry> yvalues;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Button bt_seleccionar_mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        bt_circular_global = findViewById(R.id.bt_circular_global);
        bt_seleccionar_mes = findViewById(R.id.bt_seleccionar_mes);
        obtenerGastos();
        cargarGraficoCircular("Global");

        bt_seleccionar_mes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int año = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CircularActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener, año, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
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
                obtenerGastosPorMes(mes_fix, String.valueOf(año));
            }
        };
        bt_circular_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerGastos();
                cargarGraficoCircular("global");
                pieChart.invalidate();

            }
        });

    }

    private void obtenerGastos() {
        int suma = 0;
        tipos = new ArrayList<>();
        montos = new ArrayList<>();
        porcentajes = new ArrayList<>();
        IDS = new ArrayList<>();
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        //Obtener total gastos
        String sql_obtener_detalle_monto_gastos = "SELECT MONTO_EGRESO,DETALLE_EGRESO FROM EGRESOS WHERE FK_ID_USUARIO = " + id;
        Cursor c = db.rawQuery(sql_obtener_detalle_monto_gastos, null);
        while (c.moveToNext()) {
            suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
        }
        String sql_obtener_tipos = "SELECT DISTINCT DETALLE_TIPO_EGRESO,ID_TIPO_EGRESO FROM TIPO_EGRESO A INNER JOIN EGRESOS B ON A.ID_TIPO_EGRESO = FK_TIPO_EGRESO WHERE B.FK_ID_USUARIO =" + id;
        Cursor d = db.rawQuery(sql_obtener_tipos, null);
        while (d.moveToNext()) {
            tipos.add(d.getString(d.getColumnIndex("DETALLE_TIPO_EGRESO")));
            IDS.add(d.getInt(d.getColumnIndex("ID_TIPO_EGRESO")));
        }
        Log.i("SUMA TOTAL", "Suma: " + suma);
        for (int i = 0; i < tipos.size(); i++) {
            int suma_tipo = 0;
            Log.i("Tipo", "tipo: " + tipos.get(i));
            String sql_obtener_suma_individual = "SELECT MONTO_EGRESO FROM EGRESOS WHERE FK_TIPO_EGRESO =" + IDS.get(i);
            Cursor e = db.rawQuery(sql_obtener_suma_individual, null);
            while (e.moveToNext()) {
                suma_tipo = suma_tipo + e.getInt(e.getColumnIndex("MONTO_EGRESO"));
            }
            montos.add(suma_tipo);
        }

        for (int i = 0; i < montos.size(); i++) {
            Log.i("Suma por tipo", tipos.get(i) + ": " + montos.get(i));
        }

        //calcular porcentajes de cada tipo
        for (int i = 0; i < montos.size(); i++) {
            int porcentaje = (montos.get(i) * 100) / suma;
            porcentajes.add(porcentaje);
        }
        db.close();
    }

    private void obtenerGastosPorMes(String mes, String año) {
        int suma = 0;

        IDS = new ArrayList<>();
        tipos = new ArrayList<>();
        montos = new ArrayList<>();
        porcentajes = new ArrayList<>();
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        //Obtener total gastos
        String sql_obtener_detalle_monto_gastos = "SELECT MONTO_EGRESO,DETALLE_EGRESO FROM EGRESOS WHERE FK_ID_USUARIO = " + id + " AND strftime('%m', FECHA_EGRESO) = '" + mes + "'" + " AND strftime('%Y', FECHA_EGRESO) = '" + año + "'";
        Cursor c = db.rawQuery(sql_obtener_detalle_monto_gastos, null);
        while (c.moveToNext()) {
            suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
        }
        String sql_obtener_tipos = "SELECT DISTINCT DETALLE_TIPO_EGRESO,ID_TIPO_EGRESO FROM TIPO_EGRESO A INNER JOIN EGRESOS B ON A.ID_TIPO_EGRESO = FK_TIPO_EGRESO WHERE B.FK_ID_USUARIO =" + id + " AND strftime('%m', FECHA_EGRESO) = '" + mes + "'" + " AND strftime('%Y', FECHA_EGRESO) = '" + año + "'";
        Cursor d = db.rawQuery(sql_obtener_tipos, null);
        while (d.moveToNext()) {
            tipos.add(d.getString(d.getColumnIndex("DETALLE_TIPO_EGRESO")));
            IDS.add(d.getInt(d.getColumnIndex("ID_TIPO_EGRESO")));
        }
        Log.i("SUMA TOTAL", "Suma: " + suma);
        for (int i = 0; i < tipos.size(); i++) {
            int suma_tipo = 0;
            Log.i("Tipo", "tipo: " + tipos.get(i));
            String sql_obtener_suma_individual = "SELECT MONTO_EGRESO FROM EGRESOS WHERE FK_TIPO_EGRESO =" + IDS.get(i) + " AND strftime('%m', FECHA_EGRESO) = '" + mes + "'" + " AND strftime('%Y', FECHA_EGRESO) = '" + año + "'";
            Cursor e = db.rawQuery(sql_obtener_suma_individual, null);
            while (e.moveToNext()) {
                suma_tipo = suma_tipo + e.getInt(e.getColumnIndex("MONTO_EGRESO"));
            }
            montos.add(suma_tipo);
        }

        for (int i = 0; i < tipos.size(); i++) {
            Log.i("Suma por tipo", tipos.get(i) + ": " + montos.get(i));
        }

        //calcular porcentajes de cada tipo
        if (suma == 0) {
            pieChart.clear();
            Toast.makeText(getApplicationContext(), "No hay registros para la fecha seleccionada", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < montos.size(); i++) {
                int porcentaje = (montos.get(i) * 100) / suma;
                porcentajes.add(porcentaje);
            }
            cargarGraficoCircular(mes);

        }

    }

    private void cargarGraficoCircular(String centro) {
        yvalues = new ArrayList<>();
        pieChart = findViewById(R.id.pie_chart);
        for (int i = 0; i < porcentajes.size(); i++) {
            yvalues.add(new PieEntry(porcentajes.get(i), tipos.get(i)));
        }
        PieDataSet dataSet = new PieDataSet(yvalues, "Tipos de gastos en procentajes");
        dataSet.setSliceSpace(0);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextSize(13f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.2f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("Porcentaje tipo de gastos");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(20);
        pieChart.setCenterText(centro);
        pieChart.animateY(1000);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(10f);



    }



}
