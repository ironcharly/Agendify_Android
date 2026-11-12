package com.example.agendify_android.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Turno implements Serializable {

    @SerializedName("id_turno")
    private String idTurno;

    @SerializedName("id_negocio")
    private String idNegocio;

    @SerializedName("id_servicio")
    private String idServicio;

    @SerializedName("nombre_cliente_final")
    private String nombreClienteFinal;

    @SerializedName("telefono_cliente_final")
    private String telefonoClienteFinal;

    @SerializedName("email_cliente_final")
    private String emailClienteFinal;

    @SerializedName("fecha_hora_inicio")
    private String fechaHoraInicio;

    @SerializedName("fecha_hora_fin")
    private String fechaHoraFin;

    @SerializedName("estado")
    private String estado;

    @SerializedName("codigo_cancelacion")
    private String codigoCancelacion;

    @SerializedName("nombre_servicio")
    private String nombreServicio;

    public String getIdTurno() { return idTurno; }
    public String getIdNegocio() { return idNegocio; }
    public String getIdServicio() { return idServicio; }
    public String getNombreClienteFinal() { return nombreClienteFinal; }
    public String getTelefonoClienteFinal() { return telefonoClienteFinal; }
    public String getEmailClienteFinal() { return emailClienteFinal; }
    public String getFechaHoraInicio() { return fechaHoraInicio; }
    public String getFechaHoraFin() { return fechaHoraFin; }
    public String getEstado() { return estado; }
    public String getCodigoCancelacion() { return codigoCancelacion; }
    public String getNombreServicio() { return nombreServicio; }
}