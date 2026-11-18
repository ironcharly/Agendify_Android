package com.example.agendify_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendify_android.adapter.TurnosAdapter;
import com.example.agendify_android.model.Turno;
import com.example.agendify_android.network.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TURNO_ID = "EXTRA_TURNO_ID";
    private static final String STATE_TURNOS = "STATE_TURNOS";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvSinTurnos;
    private TurnosAdapter adapter;
    private final ArrayList<Turno> listaTurnos = new ArrayList<>();

    private final ActivityResultLauncher<Intent> crearTurnoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarTurnosDesdeApi();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
        setSupportActionBar(findViewById(R.id.toolbarPrincipal));
        setTitle(R.string.titulo_mis_turnos);


        recyclerView = findViewById(R.id.recyclerViewTurnos);
        progressBar = findViewById(R.id.progressBarPrincipal);
        tvSinTurnos = findViewById(R.id.tvSinTurnos);
        FloatingActionButton fabCrear = findViewById(R.id.fabCrearTurno);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TurnosAdapter(listaTurnos, turno -> {
            Intent intent = new Intent(MainActivity.this, DetalleTurnoActivity.class);
            intent.putExtra(EXTRA_TURNO_ID, turno.getIdTurno());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        fabCrear.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearTurnoActivity.class);
            crearTurnoLauncher.launch(intent);
        });

        if (savedInstanceState != null) {
            ArrayList<Turno> guardados =
                    (ArrayList<Turno>) savedInstanceState.getSerializable(STATE_TURNOS);
            if (guardados != null && !guardados.isEmpty()) {
                listaTurnos.clear();
                listaTurnos.addAll(guardados);
                adapter.notifyDataSetChanged();
                actualizarVacio();
            } else {
                cargarTurnosDesdeApi();
            }
        } else {
            cargarTurnosDesdeApi();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_TURNOS, listaTurnos);
    }

    private void actualizarVacio() {
        tvSinTurnos.setVisibility(listaTurnos.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void cargarTurnosDesdeApi() {
        progressBar.setVisibility(View.VISIBLE);
        tvSinTurnos.setVisibility(View.GONE);

        ApiClient.getClient().obtenerTurnos().enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(@NonNull Call<List<Turno>> call,
                                   @NonNull Response<List<Turno>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    listaTurnos.clear();
                    listaTurnos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    actualizarVacio();
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.error_api_generic, response.code()),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Turno>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, R.string.error_conexion, Toast.LENGTH_LONG).show();
            }
        });
    }
}