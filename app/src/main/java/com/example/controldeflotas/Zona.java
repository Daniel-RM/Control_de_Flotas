package com.example.controldeflotas;

import java.io.Serializable;

public class Zona implements Serializable {

    private String codigo;
    private String descripcion;
    private String tipo;
    private String coordenadas;
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
        return codigo + " / " + descripcion + " / " + tipo;
    }
}
