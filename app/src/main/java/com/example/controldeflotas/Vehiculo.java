package com.example.controldeflotas;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    private String tipoVehiculo;
    private String descripcion;
    private String matricula;
    private String identificador;
    private double latitud, longitud;
    private String estado;
    private String fecha;
    private String velocidad;
    private String distancia;
    private String altitud;
    private String rpm;
    private String temperatura;
    private String presion;


    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identicador) {
        this.identificador = identicador;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getAltitud() {
        return altitud;
    }

    public void setAltitud(String altitud) {
        this.altitud = altitud;
    }

    public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getPresion() {
        return presion;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Vehiculo{" +
                "tipoVehiculo='" + tipoVehiculo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", matricula='" + matricula + '\'' +
                ", identificador='" + identificador + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", estado='" + estado + '\'' +
                ", fecha='" + fecha + '\'' +
                ", velocidad='" + velocidad + '\'' +
                ", distancia='" + distancia + '\'' +
                ", altitud='" + altitud + '\'' +
                ", rpm='" + rpm + '\'' +
                ", temperatura='" + temperatura + '\'' +
                ", presion='" + presion + '\'' +
                '}';
    }
}
