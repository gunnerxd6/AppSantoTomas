package com.example.victor.appsantotomas;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class BarrasActivity extends AppCompatActivity {
    BarChart barChart;
    int id;
    Button bt_circular_global;
    ArrayList<BarEntry> entries;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    Button bt_seleccionar_año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barras);
        barChart = findViewById(R.id.barChart);
        bt_seleccionar_año = findViewById(R.id.bt_año);
        id = getIntent().getExtras().getInt("ID_USUARIO_ACTUAL");
        obtenerGastos();
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
            String sql_gastos_meses_año = "SELECT MONTO_EGRESO FROM EGRESOS WHERE strftime('%Y', FECHA_EGRESO) ='2018' AND strftime('%m', FECHA_EGRESO) ='" + mes + "'";
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
                Log.e("Suma mes", "Suma: " + suma);
            }
            entries.add(new BarEntry(Float.valueOf(String.valueOf(suma + "f")), i - 1));
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
            String sql_gastos_meses_año = "SELECT MONTO_EGRESO FROM EGRESOS WHERE strftime('%Y', FECHA_EGRESO) ='2018' AND strftime('%m', FECHA_EGRESO) ='" + mes + "'" + " AND strftime('%Y', FECHA_EGRESO) ='" + año + "'";
            Cursor c = db.rawQuery(sql_gastos_meses_año, null);
            while (c.moveToNext()) {
                suma = suma + c.getInt(c.getColumnIndex("MONTO_EGRESO"));
                Log.e("Suma mes", "Suma: " + suma);
            }
            entries.add(new BarEntry(Float.valueOf(String.valueOf(suma + "f")), i - 1));
        }

    }

    private void cargarGrafico() {

        BarDataSet dataset = new BarDataSet(entries, "Meses");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setValueTextSize(12f);
        barChart.animateY(1000);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Enero");
        labels.add("Febrero");
        labels.add("Marzo");
        labels.add("Abril");
        labels.add("Mayo");
        labels.add("Junio");
        labels.add("julio");
        labels.add("Agosto");
        labels.add("Septiembre");
        labels.add("Octubre");
        labels.add("Noviembre");
        labels.add("Diciembre");

        BarData data = new BarData(labels, dataset);

        barChart.setDragEnabled(true); // on by default
        barChart.setVisibleXRange(3f, 3f); // sets the viewport to show 3 bars
        barChart.setScaleMinima(2f, 1f);
        barChart.setDescriptionTextSize(20);
        barChart.setDescription("Gastos por periodos");
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setLabelsToSkip(0);
        barChart.setData(data);
        barChart.invalidate();

    }


}
