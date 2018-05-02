package com.example.victor.appsantotomas;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class BarrasActivity extends AppCompatActivity {
    BarChart chart;
    int id;
    Button bt_circular_global;
    ArrayList<Float> entries;
    ArrayList<Float> entries2;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Button bt_seleccionar_año;
    float barWidth;
    float barSpace;
    ArrayList yVals1;
    ArrayList yVals2;
    float groupSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barras);
        chart = findViewById(R.id.barChart);
        bt_seleccionar_año = findViewById(R.id.bt_año);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        obtenerGastos();
        obtenerIngresos();
        cargarGrafico();

        bt_seleccionar_año.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int año = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(BarrasActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        onDateSetListener, año, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
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
                obtenerGastosDefineAño(String.valueOf(año));
                obtenerIngresosDefineAño(String.valueOf(año));
                cargarGrafico();
            }
        };
    }


    private void obtenerGastos() {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        entries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int suma = 0;
            String mes = String.valueOf(i);
            if (i <= 9) {
                mes = String.valueOf("0" + i);
            }
            String sql_gastos_meses_año = "SELECT MONTO_EGRESO FROM EGRESOS WHERE strftime('%Y', FECHA_EGRESO) ='2018' AND strftime('%m', FECHA_EGRESO) ='" + mes + "'" + "AND FK_ID_USUARIO=" + id;
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
                Log.e("Suma mes", "Suma: " + suma);
            }
            entries.add(Float.valueOf(suma));
        }

    }

    private void obtenerGastosDefineAño(String año) {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        entries = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int suma = 0;
            String mes = String.valueOf(i);
            if (i <= 9) {
                mes = String.valueOf("0" + i);
            }
            String sql_gastos_meses_año = "SELECT MONTO_EGRESO FROM EGRESOS  WHERE strftime('%m', FECHA_EGRESO) ='" + mes + "'" + " AND strftime('%Y', FECHA_EGRESO) ='" + año + "'" + "AND FK_ID_USUARIO=" + id;
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
                Log.e("Suma mes", "Suma: " + suma);
            }
            entries.add(Float.valueOf(suma));
        }

    }

    private void cargarGrafico() {
        Log.e("Cantidad entries","Cantidad: "+entries2.size());
        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;

        // chart.setDescription(null);


        int groupCount = 6;

        ArrayList xVals = new ArrayList();

        xVals.add("Ene");
        xVals.add("Feb");
        xVals.add("Mar");
        xVals.add("Abr");
        xVals.add("May");
        xVals.add("Jun");
        xVals.add("Jul");
        xVals.add("Ago");
        xVals.add("Sep");
        xVals.add("Oct");
        xVals.add("Nov");
        xVals.add("Dic");

        yVals1 = new ArrayList();
        yVals2 = new ArrayList();

        for (int i = 0; i < entries.size(); i++) {
            yVals1.add(new BarEntry(i , entries.get(i)));
        }
        for (int i = 0; i < entries.size(); i++) {
            yVals2.add(new BarEntry(i , entries2.get(i)));
        }

        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "Gastos");
        set1.setColor(Color.RED);
        set1.setValueTextSize(12);
        set2 = new BarDataSet(yVals2, "Ingresos");
        set2.setColor(Color.BLUE);
        set2.setValueTextSize(10);
        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);

        chart.refreshDrawableState();


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(10f);


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

        chart.animateY(1000);
        chart.setDragEnabled(true);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(true);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(true);
        chart.setVisibleXRange(6f, 2f);
        chart.setNoDataText("No hay datos para el año seleccionado");
        chart.invalidate();
    }


    private void obtenerIngresos() {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        entries2 = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int suma = 0;
            String mes = String.valueOf(i);
            if (i <= 9) {
                mes = String.valueOf("0" + i);
            }
            String sql_gastos_meses_año = "SELECT MONTO_INGRESO FROM INGRESOS WHERE strftime('%Y', FECHA_INGRESO) ='2018' AND strftime('%m', FECHA_INGRESO) ='" + mes + "'" + "AND FK_ID_USUARIO=" + id;
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_INGRESO"));
                Log.e("Suma INGRESOS", "Suma INGRESOS: " + suma);
            }
            entries2.add(Float.valueOf(suma));
        }

    }

    private void obtenerIngresosDefineAño(String año) {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        entries2 = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            int suma = 0;
            String mes = String.valueOf(i);
            if (i <= 9) {
                mes = String.valueOf("0" + i);
            }
            String sql_gastos_meses_año = "SELECT MONTO_INGRESO FROM INGRESOS WHERE strftime('%m', FECHA_INGRESO) ='" + mes + "'" + " AND strftime('%Y', FECHA_INGRESO) ='" + año + "'" + "AND FK_ID_USUARIO=" + id;
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_INGRESO"));
                Log.e("Suma mes", "Suma: " + suma);
            }
            entries2.add(Float.valueOf(suma));
        }

    }

}
