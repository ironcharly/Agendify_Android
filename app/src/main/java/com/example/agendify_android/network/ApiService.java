package com.example.agendify_android.network;

import com.example.agendify_android.model.Turno;
import com.example.agendify_android.model.TurnoRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("backend/api_turnos.php")
    Call<List<Turno>> obtenerTurnos();

    @GET("backend/api_turnos.php")
    Call<Turno> obtenerTurnoPorId(@Query("id") int id);

    @POST("backend/api_turnos.php")
    Call<Turno> crearTurno(@Body TurnoRequest body);
}