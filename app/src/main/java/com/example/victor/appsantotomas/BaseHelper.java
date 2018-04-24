package com.example.victor.appsantotomas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper {
    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utilidades.TABLA_USUARIOS);
        sqLiteDatabase.execSQL(Utilidades.TABLA_INGRESOS);
        sqLiteDatabase.execSQL(Utilidades.TABLA_TIPO_EGRESO);
        sqLiteDatabase.execSQL(Utilidades.TABLA_EGRESOS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE USUARIOS");
        sqLiteDatabase.execSQL("DROP TABLE INGRESOS");
        sqLiteDatabase.execSQL("DROP TABLE TIPO_EGRESO");
        sqLiteDatabase.execSQL("DROP TABLE EGRESOS");
        onCreate(sqLiteDatabase);
    }
}
