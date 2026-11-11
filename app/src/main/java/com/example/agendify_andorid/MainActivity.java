package com.agendify.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.agendify.app.adapter.TurnosAdapter;
import com.agendify.app.model.Turno;
import com.agendify.app.network.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TurnosAdapter adapter;
    private ArrayList<Turno> listaTurnos = new ArrayList<>();

    // Launcher para recibir el resultado de la Pantalla 2 (R1)
    private final ActivityResultLauncher<Intent> crearTurnoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarTurnosDesdeApi(); // Refrescar lista tras crear
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewTurnos);
        progressBar = findViewById(R.id.progressBar);
        FloatingActionButton fabAgregar = findViewById(R.id.fabAgregarTurno);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TurnosAdapter(listaTurnos, turno -> {
            // Abrir Pantalla 3 pasando ID por Intent Extra (R1)
            Intent intent = new Intent(MainActivity.this, DetalleTurnoActivity.class);
            intent.putExtra("EXTRA_TURNO_ID", turno.getIdTurno());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearTurnoActivity.class);
            crearTurnoLauncher.launch(intent);
        });

        // Restaurar estado al girar pantalla (R6)
        if (savedInstanceState != null) {
            ArrayList<Turno> guardados = (ArrayList<Turno>) savedInstanceState.getSerializable("STATE_TURNOS");
            if (guardados != null && !guardados.isEmpty()) {
                listaTurnos.clear();
                listaTurnos.addAll(guardados);
                adapter.notifyDataSetChanged();
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
        outState.putSerializable("STATE_TURNOS", listaTurnos);
    }

    private void cargarTurnosDesdeApi() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getClient().obtenerTurnos().enqueue(new Callback<List<Turno>>() {
            @Override
            public void onResponse(@NonNull Call<List<Turno>> call, @NonNull Response<List<Turno>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    listaTurnos.clear();
                    listaTurnos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_api_generic, response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Turno>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Manejo de Modo Avión / Sin Conexión sin Cierres (R3)
                Toast.makeText(MainActivity.this, R.string.error_conexion, Toast.LENGTH_LONG).show();
            }
        });
    }
}