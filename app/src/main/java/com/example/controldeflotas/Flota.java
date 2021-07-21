package com.example.controldeflotas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Flota implements Serializable {

    public String nombre;
    public List<Vehiculo> vehiculos = new ArrayList<Vehiculo>(0){};
    public Posicion posicion;

}
