package com.example.agendify_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CrearCuentaActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private ProgressBar progressBar;
    private Button btnCrearCuenta;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        MaterialToolbar toolbar = findViewById(R.id.toolbarRegistro);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.titulo_crear_cuenta);
        }
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmailRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        progressBar = findViewById(R.id.progressBarRegistro);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        btnCrearCuenta.setOnClickListener(v -> registrar());
    }

    private void registrar() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, R.string.login_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass.length() < 6) {
            Toast.makeText(this, R.string.error_datos_invalidos, Toast.LENGTH_SHORT).show();
            return;
        }
        setLoading(true);
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        irAMain();
                    } else {
                        Toast.makeText(this, mensajeError(task.getException()), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private String mensajeError(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            return getString(R.string.error_email_en_uso);
        }
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return getString(R.string.error_password_debil);
        }
        if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return getString(R.string.error_email_invalido);
        }
        if (exception != null && exception.getMessage() != null) {
            return exception.getMessage();
        }
        return getString(R.string.error_registro);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnCrearCuenta.setEnabled(!loading);
    }

    private void irAMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
