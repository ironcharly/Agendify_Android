package com.example.agendify_android;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendify_android.model.ErrorApi;
import com.example.agendify_android.model.Turno;
import com.example.agendify_android.model.TurnoRequest;
import com.example.agendify_android.network.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearTurnoActivity extends AppCompatActivity {

    private static final Pattern FECHA = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern HORA = Pattern.compile("^\\d{2}:\\d{2}$");

    private EditText etNombre, etTelefono, etEmail, etFecha, etHora;
    private ProgressBar progressBar;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_turno);

        MaterialToolbar toolbar = findViewById(R.id.toolbarCrear);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.titulo_crear_turno);
        }
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        progressBar = findViewById(R.id.progressBarCrear);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> intentarCrear());
    }

    private void intentarCrear() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();
        String hora = etHora.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()
                || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, R.string.error_datos_invalidos, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.error_datos_invalidos, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!FECHA.matcher(fecha).matches() || !HORA.matcher(hora).matches()) {
            Toast.makeText(this, R.string.error_datos_invalidos, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnGuardar.setEnabled(false);

        TurnoRequest body = new TurnoRequest(nombre, telefono, email, fecha, hora);
        ApiClient.getClient().crearTurno(body).enqueue(new Callback<Turno>() {
            @Override
            public void onResponse(@NonNull Call<Turno> call, @NonNull Response<Turno> response) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CrearTurnoActivity.this, R.string.turno_creado, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                    return;
                }

                String mensajeApi = leerError(response);
                if (response.code() == 400 || response.code() == 422) {
                    Toast.makeText(
                            CrearTurnoActivity.this,
                            getString(R.string.error_validacion, mensajeApi),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(
                            CrearTurnoActivity.this,
                            getString(R.string.error_api_generic, response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Turno> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnGuardar.setEnabled(true);
                Toast.makeText(CrearTurnoActivity.this, R.string.error_conexion, Toast.LENGTH_LONG).show();
            }
        });
    }

    private String leerError(Response<?> response) {
        try {
            if (response.errorBody() == null) {
                return getString(R.string.error_datos_invalidos);
            }
            ErrorApi error = new Gson().fromJson(response.errorBody().string(), ErrorApi.class);
            if (error != null && error.error != null && !error.error.isEmpty()) {
                return error.error;
            }
        } catch (IOException ignored) {
        }
        return getString(R.string.error_datos_invalidos);
    }
}