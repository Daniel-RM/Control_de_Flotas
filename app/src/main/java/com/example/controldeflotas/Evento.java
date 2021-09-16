package com.example.controldeflotas;

import java.util.regex.Pattern;

public class Evento {
    private String fecha;
    private int tipo;
    private String opcion;
    private String idmodulo;
    private int cantidad;
    private String albaran;

    String[] partes = new String[5];

    public Evento() {
    }

    public Evento(String fecha, int tipo, String opcion, String idmodulo, int cantidad, String albaran) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.opcion = opcion;
        this.idmodulo = idmodulo;
        this.cantidad = cantidad;
        this.albaran = albaran;
    }

    public String getFecha() {
        String hora = "";
        hora = fecha.substring(8,14);
        hora = hora.substring(0,2) + ":" + hora.substring(2,4)  +":" + hora.substring(4,6);
        return hora;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return trataTipo(tipo);
    }

    public int getTipoNum() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getIdmodulo() {
        return idmodulo;
    }

    public void setIdmodulo(String idmodulo) {
        this.idmodulo = idmodulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getAlbaran() {
        return albaran;
    }

    public void setAlbaran(String albaran) {
        this.albaran = albaran;
    }

    public String[] getOpciones(){
        String separa = Pattern.quote("|");
        partes = opcion.split(separa);
        return partes;
    }

//    @Override
//    public String toString() {
//        return "Evento{" +
//                "fecha='" + fecha + '\'' +
//                ", tipo='" + tipo + '\'' +
//                ", opcion='" + opcion + '\'' +
//                ", idmodulo='" + idmodulo + '\'' +
//                ", cantidad='" + cantidad + '\'' +
//                ", albaran='" + albaran + '\'' +
//                '}';
//    }

    @Override
    public String toString() {
        return dameMatricula(idmodulo).trim() + ": " + trataTipo(tipo);
    }

    public String trataTipo(int tipo){
        String tipe = "";
        switch (tipo){
            case 0:
                tipe = "Paro";
                break;
            case 1:
                tipe = "En marcha";
                break;
            case 2:
                tipe = "Entra en zona de carga en " + opcion;
                break;
            case 3:
                tipe = "Sale de zona de carga en " + opcion;
                break;
            case 4:
                tipe = "Exceso de velocidad";
                break;
            case 5:
                tipe = "Bajo combustible";
                break;
            case 6:
                tipe = "Inicio de descarga";
                break;
            case 7:
                tipe = "Fin de descarga";
                break;
            case 8:
                tipe = "Viaje con caducidad de hormigón. " + getOpciones()[1] + " - " + getOpciones()[2] + " - " + "N.Carga: " + getOpciones()[4] + " m3" ;
                break;
            case 9:
                tipe = "Reasignación de flota";
                break;
            case 10:
                tipe = "Superada adición de agua";
                break;
            case 11:
                tipe = "En marcha fuera de tiempo";
                break;
            case 12:
                tipe = "Entra en zona de descarga";
                break;
            case 13:
                tipe = "Sale de zona de descarga";
                break;
            case 14:
                tipe = "Inicio de carga " + opcion + " - Alb: " + albaran;
                break;
            case 15:
                tipe = cantidad + "lts. Agua añadida hormigón";
                break;
            case 16:
                tipe = "ITV";
                break;
            case 17:
                tipe = "Revisión mecánica";
                break;
            case 18:
                tipe = "Sale zona de trabajo. - Alb: " + albaran;
                break;
            case 19:
                tipe = "Entrada en radio no asignado";
                break;
            case 20:
                tipe = "RPM a 0";
                break;
            case 21:
                tipe = "Presión a 0";
                break;
            case 22:
                tipe = "Sin agua añadida en viajes del día";
                break;
            case 23:
                tipe = "Sin tramas en horario de trabajo";
                break;
            case 24:
                tipe = "Viaje sin tramas";
                break;
            case 25:
                tipe = "Superado tiempo de viaje. Alb - " + albaran;
                break;
            case 26:
                tipe = "Descarga de hormigón en planta";
                break;
            case 27:
                tipe = "Pérdida de señal";
                break;
            case 28:
                tipe = "Sin tramas del día anterior";
                break;
            case 29:
                tipe = "Envío continuo tramas";
                break;
            case 35:
                tipe = "Fuera radio de seguridad";
                break;
            case 100:
                tipe = "RPM a 0 en albarán: " + albaran;
                break;
            case 101:
                tipe = "Presión a 0 en albarán: " + albaran;
                break;
            case 102:
                tipe = "Sin agua en 3 viajes";
                break;
        }
        return tipe;
    }

    public static String dameMatricula(String id){
        String matricula = "";
        for(int x=0;x<MenuActivity.listaVehiculos.size();x++){
            if(id.equals(MenuActivity.listaVehiculos.get(x).getIdentificador())){
                matricula = MenuActivity.listaVehiculos.get(x).getMatricula();
            }
        }
        return matricula;
    }
}
