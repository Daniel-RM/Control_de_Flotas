package com.example.controldeflotas;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private String zona;

    public Datos() {
    }

    public Datos(String hora, double latitud, double longitud, String estado, String velocidad, String comportamiento, String zona){
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.velocidad = velocidad;
        this.comportamiento = comportamiento;
        this.zona = zona;
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

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    @Override
    public String toString() {
           return hora + "   /   " + obtenerDireccion(latitud, longitud) + "   /   " + comportamiento + "\n";
    }

    //M??todo para obtener la direcci??n postal, en base a las coordenadas recibidas. Si no se crea la direcci??n, muestro la poblaci??n
    public static String obtenerDireccion(double latitud, double longitud){

        String direccion = "";

        if(latitud != 0.0 && longitud != 0.0){
            try{
                Geocoder geocoder = new Geocoder(MenuActivity.context, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
                if(!list.isEmpty()){
                    Address DirCalle = list.get(0);
                    String locality = "";
                    String thoroughfare = "";
                    String subThoroughfare = "";

                    //Controlo que los datos recibidos, no sean null. En caso de serlo, dejo el campo en blanco
                    if(DirCalle.getLocality() == null){
                        locality = "";
                    }else{
                        locality = DirCalle.getLocality() + ", ";
                    }

                    if(DirCalle.getThoroughfare() == null){
                        thoroughfare = "";
                    }else{
                        thoroughfare = DirCalle.getThoroughfare() + ", ";
                    }

                    if(DirCalle.getSubThoroughfare() == null){
                        subThoroughfare = "";
                    }else{
                        subThoroughfare = DirCalle.getSubThoroughfare() + ", ";
                    }

                    direccion = locality + thoroughfare + subThoroughfare;

                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return direccion;
    }

}
