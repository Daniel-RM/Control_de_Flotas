package com.example.controldeflotas;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    private String matricula;
    private double latitud, longitud;
    private String estado;
    private String fecha;
    private String velocidad;
    private String distancia;
    private String altitud;
    private String rpm;
    private String temperatura;
    private String presion;

    public Vehiculo() {
    }

    public Vehiculo(String matricula, double latitud, double longitud, String estado, String fecha, String velocidad, String distancia, String altitud, String rpm, String temperatura, String presion) {
        this.matricula = matricula;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.fecha = fecha;
        this.velocidad = velocidad;
        this.distancia = distancia;
        this.altitud = altitud;
        this.rpm = rpm;
        this.temperatura = temperatura;
        this.presion = presion;
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

    @Override
    public String toString() {
        return "Vehiculo{" +
                "matricula='" + matricula + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", estado='" + estado + '\'' +
                ", fecha='" + fecha + '\'' +
                ", velocidad=" + velocidad +
                ", distancia=" + distancia +
                ", altitud=" + altitud +
                ", rpm=" + rpm +
                ", temperatura=" + temperatura +
                ", presion=" + presion +
                '}';
    }
}
