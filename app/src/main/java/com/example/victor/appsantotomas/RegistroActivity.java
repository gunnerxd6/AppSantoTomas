package com.example.victor.appsantotomas;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    EditText et_registro_usuario, et_registro_email, et_registro_password, et_registro_cpassword;
    Button bt_registrar;
    ImageView iv_registro_email, iv_registro_ccontrasena;
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
                registrarUsuario(et_registro_usuario, et_registro_email, et_registro_password, et_registro_cpassword);
            }
        });

        iv_registro_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToast("nombre@correo.com", iv_registro_email);
            }
        });
        iv_registro_ccontrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToast("Repetir contraseña debe coincidir con el campo contraseña", iv_registro_ccontrasena);
            }
        });
    }
    private void registrarUsuario(EditText usuario, EditText email, EditText contrasena, EditText ccontrasena) {
        boolean registrar = true;
        BaseHelper helper = new BaseHelper(this, "db_gastos", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        boolean vacios = validarVacios(usuario, email, contrasena, ccontrasena);
        boolean contra = true;
        boolean correoV = true;
        boolean largoContraseña = true;
        boolean largoUsuario = true;
        boolean alfanumerico = true;
        if (!vacios){
                registrar = false;
            }else{
            contra = compararPassword(contrasena, ccontrasena);
            correoV = true;
            largoContraseña = validarLargoContrasena(contrasena);
            alfanumerico = validarAlfanumerico(usuario);
            largoUsuario = validarLargoUsuario(usuario);
        }
        if (!alfanumerico)
            registrar = false;

        if (!largoUsuario)
            registrar = false;

        if (!largoContraseña)
            registrar = false;

        if (!contra)
            registrar = false;

        if (vacios && contra) {
            correoV = validarCorreo(email.getText().toString());
        }

        if (!correoV) {
            registrar = false;
        }
        if (registrar) {
            String validar_usuario = "SELECT NOMBRE_USUARIO,CLAVE_USUARIO FROM USUARIOS WHERE NOMBRE_USUARIO='" + usuario.getText().toString() + "'";
            Cursor c = db.rawQuery(validar_usuario, null);
            if (c.moveToFirst()) {
                Toast.makeText(getApplicationContext(), "Usuario ya registrado", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("NOMBRE_USUARIO", usuario.getText().toString());
                    contentValues.put("EMAIL_USUARIO", email.getText().toString());
                    contentValues.put("CLAVE_USUARIO", contrasena.getText().toString());
                    db.insert("USUARIOS", null, contentValues);
                    db.close();
                    Toast.makeText(getApplicationContext(), "Usuario registrado", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "Error al conectar a la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
            c.close();
        }
    }

    private void showCustomToast(String message, ImageView v) {
        /*
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        */
        int x = v.getLeft();
        int y = v.getTop();
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, x, y);
        toast.show();
    }

    public boolean validarVacios(EditText usuario, EditText email, EditText contrasena, EditText ccontrasena) {
        boolean todoIngresado = true;
        String errorVacio = "Por favor, complete los siguientes campos:\n ";
        AlertDialog.Builder ventana = new AlertDialog.Builder(RegistroActivity.this);
        ventana.setTitle("Datos requeridos!");
        ventana.setPositiveButton("Aceptar", null);
        if (usuario.getText().toString().isEmpty()) {
            errorVacio = errorVacio + "\nNombre de usuario.";
            todoIngresado = false;
        }
        if (email.getText().toString().isEmpty()) {
            errorVacio = errorVacio + "\nEmail.";
            todoIngresado = false;
        }
        if (contrasena.getText().toString().isEmpty()) {
            errorVacio = errorVacio + "\nContraseña.";
            todoIngresado = false;
        }
        if (ccontrasena.getText().toString().isEmpty()) {
            errorVacio = errorVacio + "\nConfirmar contraseña.";
            todoIngresado = false;
        }
        if (!todoIngresado) {

            ventana.setMessage(errorVacio);
            ventana.show();
        }

        return todoIngresado;
    }

    public boolean validarCorreo(String correo) {
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
        builder.setTitle("Error: ");
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(correo);
        if (!matcher.matches()) {
            builder.setMessage("El correo ingresado no es valido.");
            builder.setPositiveButton("Aceptar", null);
            builder.show();
        }
        return matcher.matches();
    }

    public boolean compararPassword(EditText pass, EditText pass2) {
        boolean coincide;
        if (pass.getText().toString().equals(pass2.getText().toString())) {
            coincide = true;
        } else {
            Toast.makeText(RegistroActivity.this, "Contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
            coincide = false;
        }
        return coincide;
    }

    public boolean validarLargoContrasena(EditText contrasena) {
        boolean valido = true;
        String mensaje = "";
        if (contrasena.getText().toString().length() < 6) {
            mensaje = "La contraseña debe contener minimo 6 caracteres";
            valido = false;
        }
        if (contrasena.getText().toString().length() > 20) {
            mensaje = "La contraseña debe contener maximo 20 caracteres";
            valido = false;
        }
        if (!valido) {

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
        }
        return valido;
    }

    public boolean validarAlfanumerico(EditText usuario) {
        boolean valido = true;
        if (!usuario.getText().toString().matches("[A-Za-z0-9]+")) {
            valido = false;
            Toast.makeText(getApplicationContext(), "El nombre de usuario debe ser alfanumerico", Toast.LENGTH_LONG).show();
        }

        return valido;
    }

    public boolean validarLargoUsuario(EditText usuario) {
        boolean valido = true;
        String mensaje = "";
        if (usuario.getText().toString().length() < 6) {
            mensaje = "El usuario debe contener minimo 6 caracteres";
            valido = false;
        }
        if (usuario.getText().toString().length() > 20) {
            mensaje = "El usuario debe contener maximo 20 caracteres";
            valido = false;
        }
        if (!valido) {

            Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
        }
        return valido;
    }
}