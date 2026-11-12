package com.example.agendify_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agendify_android.model.Turno;
import com.example.agendify_android.network.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleTurnoActivity extends AppCompatActivity {

    private static final String STATE_TURNO = "STATE_TURNO";

    private ProgressBar progressBar;
    private TextView tvCliente, tvTelefono, tvEmail, tvServicio, tvInicio, tvFin, tvEstado, tvCodigo;
    private Turno turnoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_turno);

        MaterialToolbar toolbar = findViewById(R.id.toolbarDetalle);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.titulo_detalle_turno);
        }
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed());

        progressBar = findViewById(R.id.progressBarDetalle);
        tvCliente = findViewById(R.id.tvDetalleCliente);
        tvTelefono = findViewById(R.id.tvDetalleTelefono);
        tvEmail = findViewById(R.id.tvDetalleEmail);
        tvServicio = findViewById(R.id.tvDetalleServicio);
        tvInicio = findViewById(R.id.tvDetalleInicio);
        tvFin = findViewById(R.id.tvDetalleFin);
        tvEstado = findViewById(R.id.tvDetalleEstado);
        tvCodigo = findViewById(R.id.tvDetalleCodigo);

        if (savedInstanceState != null) {
            turnoActual = (Turno) savedInstanceState.getSerializable(STATE_TURNO);
            if (turnoActual != null) {
                pintarTurno(turnoActual);
                return;
            }
        }

        String idStr = getIntent().getStringExtra(MainActivity.EXTRA_TURNO_ID);
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_id_invalido, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDetalle(id);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (turnoActual != null) {
            outState.putSerializable(STATE_TURNO, turnoActual);
        }
    }

    private void cargarDetalle(int id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getClient().obtenerTurnoPorId(id).enqueue(new Callback<Turno>() {
            @Override
            public void onResponse(@NonNull Call<Turno> call, @NonNull Response<Turno> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    turnoActual = response.body();
                    pintarTurno(turnoActual);
                } else if (response.code() == 404) {
                    Toast.makeText(DetalleTurnoActivity.this, R.string.error_no_encontrado, Toast.LENGTH_LONG).show();
                    finish();
                } else if (response.code() == 400) {
                    Toast.makeText(DetalleTurnoActivity.this, R.string.error_id_invalido, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(
                            DetalleTurnoActivity.this,
                            getString(R.string.error_api_generic, response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Turno> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DetalleTurnoActivity.this, R.string.error_conexion, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void pintarTurno(Turno t) {
        String servicio = t.getNombreServicio() != null ? t.getNombreServicio() : "-";
        String email = t.getEmailClienteFinal() != null ? t.getEmailClienteFinal() : "-";
        String codigo = t.getCodigoCancelacion() != null ? t.getCodigoCancelacion() : "-";

        tvCliente.setText(getString(R.string.label_cliente, t.getNombreClienteFinal()));
        tvTelefono.setText(getString(R.string.label_telefono, t.getTelefonoClienteFinal()));
        tvEmail.setText(getString(R.string.label_email, email));
        tvServicio.setText(getString(R.string.label_servicio, servicio));
        tvInicio.setText(getString(R.string.label_inicio, t.getFechaHoraInicio()));
        tvFin.setText(getString(R.string.label_fin, t.getFechaHoraFin()));
        tvEstado.setText(getString(R.string.label_estado, t.getEstado()));
        tvCodigo.setText(getString(R.string.label_codigo, codigo));
    }
}