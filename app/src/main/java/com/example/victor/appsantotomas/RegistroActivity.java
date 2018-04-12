package com.example.victor.appsantotomas;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {
    EditText et_registro_usuario,et_registro_email,et_registro_password,et_registro_cpassword;
    Button bt_registrar;
    ImageView iv_registro_email,iv_registro_ccontrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        iv_registro_ccontrasena = findViewById(R.id.iv_registro_ccontrasena);
        iv_registro_email = findViewById(R.id.iv_registro_email);
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

        iv_registro_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToast("nombre@correo.com",iv_registro_email);
            }
        });

        iv_registro_ccontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToast("Repetir contraseña debe coincidir con el campo contraseña",iv_registro_ccontrasena);
            }
        });


    }
    private void registrarUsuario(String usuario,String email, String password){
        /*BaseHelper helper = new BaseHelper(this,"db_gastos",null,1);
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
            */
        validarRegistroUsuario(et_registro_usuario,et_registro_email,et_registro_password,et_registro_cpassword);

    }

    private void showCustomToast(String message,ImageView v){
        /*
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        */
        int x = v.getLeft();
        int y = v.getTop();
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.LEFT, x , y);
        toast.show();
    }
    private boolean validarRegistroUsuario(EditText usuario,EditText email, EditText contrasena, EditText ccontrasena){
        boolean valido = true;
        String errores = "Error: ";
        //Validar nombre
        if(usuario.getText().toString().equals("")){
            errores = errores + " El campo usuario no debe estar vacio. ";
            valido = false;
        }

        String asd = "asd";
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()==false) {
            errores = errores + "Email no valido. ";
            valido = false;
        }
        Log.i("Estado","Valido: "+valido);

        return valido;

    }
}
