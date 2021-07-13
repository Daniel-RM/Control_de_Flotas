package com.example.controldeflotas;

public class Datos {

    private String matricula;
    private String fecha;
    private String hora;
    private double latitud;
    private double longitud;
    private String estado;
    private String comportamiento;
    private String velocidad;
    private String distancia;

    public Datos() {
    }

    public Datos(String matricula, String fecha, String hora, double latitud, double longitud, String estado, String comportamiento, String velocidad, String distancia) {
        this.matricula = matricula;
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.comportamiento = comportamiento;
        this.velocidad = velocidad;
        this.distancia = distancia;
    }

    public String getMatricula(){
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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

    public String getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(String comportamiento) {
        this.comportamiento = comportamiento;
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

    @Override
    public String toString() {
        return "Datos{" +
                "matricula='" + matricula + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", comportamiento='" + comportamiento + '\'' +
                ", velocidad='" + velocidad + '\'' +
                ", distancia='" + distancia + '\'' +
                '}';
    }
}
