package com.example.victor.appsantotomas;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    EditText et_registro_usuario,et_registro_email,et_registro_password,et_registro_cpassword;
    Button bt_registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        et_registro_usuario = findViewById(R.id.et_registro_usuario);
        et_registro_email = findViewById(R.id.et_registro_email);
        et_registro_password = findViewById(R.id.et_registro_password);
        et_registro_cpassword = findViewById(R.id.et_registro_cpassword);
        bt_registrar = findViewById(R.id.bt_registro_registrar);
        bt_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario(et_registro_usuario.getText().toString(),et_registro_email.getText().toString(),et_registro_password.getText().toString());
            }
        });

    }
    private void registrarUsuario(String usuario,String email, String password){
        BaseHelper helper = new BaseHelper(this,"db_gastos",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            ContentValues c = new ContentValues();
            c.put("USUARIO",usuario);
            c.put("EMAIL",email);
            c.put("PASSWORD", password);
            db.insert("USUARIOS",null,c);
            db.close();
            Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    "Error al conectar a la base de datos",Toast.LENGTH_SHORT).show();
        }

    }

}
