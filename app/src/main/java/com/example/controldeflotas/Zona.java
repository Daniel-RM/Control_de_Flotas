package com.example.controldeflotas;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Zona implements Serializable {

    private String codigo;
    private String descripcion;
    private String tipo;
    private String coordenadas;
//    private String coordenada1;
//    private String coordenada2;
//    private String coordenada3;
//    private String coordenada4;
//    private String coordenada5;
    private String direccion;

    public Zona(){
    }

    public Zona(String codigo, String descripcion, String tipo, String coordenadas, String direccion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.coordenadas = coordenadas;
        this.direccion = direccion;
    }

//        public Zona(String codigo, String descripcion, String tipo, String coordenada1, String coordenada2, String coordenada3, String coordenada4, String coordenada5, String direccion) {
//        this.codigo = codigo;
//        this.descripcion = descripcion;
//        this.tipo = tipo;
//        this.coordenada1 = coordenada1;
//        this.coordenada2 = coordenada2;
//        this.coordenada3 = coordenada3;
//        this.coordenada4 = coordenada4;
//        this.coordenada5 = coordenada5;
//        this.direccion = direccion;
//    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String[] getCoordenadas() {
        String[] coordArray = coordenadas.split(",");
        return coordArray;
    }

    public String getCoordenadasString(){
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
//        return "Zona{" +
//                "codigo='" + codigo + '\'' +
//                ", descripcion='" + descripcion + '\'' +
//                ", tipo='" + tipo + '\'' +
//                //", coordenadas='" + coordenadas + '\'' +
//                //", direccion='" + direccion + '\'' +
//                '}';
        return codigo + " / " + descripcion + " / " + tipo;
    }
}
