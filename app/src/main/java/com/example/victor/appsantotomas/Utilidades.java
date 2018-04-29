package com.example.victor.appsantotomas;

import android.widget.EditText;

/**
 * Created by Victor on 4/10/2018.
 */

public class Utilidades {


    public static final String TABLA_USUARIOS = "CREATE TABLE USUARIOS (ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT," +
            " NOMBRE_USUARIO TEXT NOT NULL, EMAIL_USUARIO TEXT NOT NULL, CLAVE_USUARIO TEXT NOT NULL)";
    public static final String TABLA_INGRESOS = "CREATE TABLE INGRESOS (" +
            "  ID_INGRESO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  DETALLE_INGRESO VARCHAR(45) NOT NULL,\n" +
            "  MONTO_INGRESO INTEGET NOT NULL,\n" +
            "  FECHA_INGRESO DATE NOT NULL,\n" +
            "  FK_ID_USUARIO INTEGER NOT NULL,\n" +
            "  CONSTRAINT FK_ID_USUARIO\n" +
            "    FOREIGN KEY (FK_ID_USUARIO)\n" +
            "    REFERENCES USUARIO (ID_USUARIO))";
    public static final String TABLA_TIPO_EGRESO = "CREATE TABLE TIPO_EGRESO (\n" +
            "  ID_TIPO_EGRESO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  FK_ID_USUARIO INTEGER NOT NULL,\n" +
            "  DETALLE_TIPO_EGRESO TEXT NOT NULL,\n" +
            "  CONSTRAINT FK_ID_USUARIO\n" +
            "  FOREIGN KEY (FK_ID_USUARIO)\n" +
            "  REFERENCES USUARIO (ID_USUARIO))";
    public static final String TABLA_EGRESOS = "CREATE TABLE EGRESOS (\n" +
            "  ID_EGRESO INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "  FK_ID_USUARIO INTEGER NOT NULL,\n" +
            "  DETALLE_EGRESO TEXT NOT NULL,\n" +
            "  MONTO_EGRESO INTEGER NOT NULL,\n" +
            "  FECHA_EGRESO DATE NOT NULL,\n" +
            "  FK_TIPO_EGRESO INTEGER NOT NULL,\n" +
            "  CONSTRAINT FK_ID_USUARIO\n" +
            "  FOREIGN KEY (FK_ID_USUARIO)\n" +
            "  REFERENCES USUARIO (ID_USUARIO)" +
            "  CONSTRAINT FK_TIPO_EGRESO\n" +
            "  FOREIGN KEY (FK_TIPO_EGRESO)\n" +
            "  REFERENCES TIPO_EGRESO (ID_TIPO_EGRESO))";

}
