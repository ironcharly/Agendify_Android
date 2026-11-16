package com.example.agendify_android.model;

import com.google.gson.annotations.SerializedName;

public class TurnoRequest {

    @SerializedName("nombre_cliente_final")
    public String nombreClienteFinal;

    @SerializedName("telefono_cliente_final")
    public String telefonoClienteFinal;

    @SerializedName("email_cliente_final")
    public String emailClienteFinal;

    @SerializedName("fecha")
    public String fecha;

    @SerializedName("hora")
    public String hora;

    public TurnoRequest(String nombre, String telefono, String email, String fecha, String hora) {
        this.nombreClienteFinal = nombre;
        this.telefonoClienteFinal = telefono;
        this.emailClienteFinal = email;
        this.fecha = fecha;
        this.hora = hora;
    }
}