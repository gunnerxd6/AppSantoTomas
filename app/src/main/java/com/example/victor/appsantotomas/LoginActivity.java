package com.example.victor.appsantotomas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button bt_login_login;
    EditText et_usuario, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_login_login = findViewById(R.id.bt_login_login);
        et_usuario = findViewById(R.id.et_login_user);
        et_password = findViewById(R.id.et_login_password);
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUsuario(et_usuario.getText().toString(), et_password.getText().toString());
            }
        });
    }

    private void loginUsuario(String usuario, String password) {
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        boolean vacios = validarVacios(usuario, password);
        if (vacios == true) {
            try {
                String validar_usuario = "SELECT USUARIO,PASSWORD FROM USUARIOS WHERE USUARIO='" + usuario + "' and PASSWORD='" + password + "'";
                Cursor c = db.rawQuery(validar_usuario, null);
                if (c.moveToFirst()) {
                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Error al validar usuario", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {

            }
        }
    }

    public boolean validarVacios(String usuario, String contrasena) {
        boolean todoIngresado = true;
        String errorVacio = "Por favor, complete los siguientes campos:\n ";
        AlertDialog.Builder ventana = new AlertDialog.Builder(LoginActivity.this);
        ventana.setTitle("Datos requeridos!");
        ventana.setPositiveButton("Aceptar", null);
        if (usuario.isEmpty()) {
            errorVacio = errorVacio + "\nNombre de usuario.";
            todoIngresado = false;
        }
        if (contrasena.isEmpty()) {
            errorVacio = errorVacio + "\nContraseña.";
            todoIngresado = false;
        }

        if (todoIngresado == false) {

            ventana.setMessage(errorVacio);
            ventana.show();
        }
        return todoIngresado;
    }
}