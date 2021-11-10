package com.example.controldeflotas;

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
    private String horasMotor;
    private String combustibleNivel;
    private String distanciaServicio;
    private String zona;

    private String planta;

    ////////////////////Datos CAN
    private String odometro;
    private String combustibleTotalUsado;
    private String tmpMotor;
    private String ralenti;
    private String combustibleRalenti;
    private String freno;
    private String sobrefreno;
    private String sobrevelocidad;
    private String sobreaceleracion;


    /////////////////////Datos de viaje
    private String carga_descarga_viaje;
    private String agua_total_viaje;
    private String hora_viaje;
    private String m3_viaje;
    private String cliente_promsa_viaje;
    private String idviaje;
    private String caducado_viaje;
    private String albaran_viaje;
    private String obra_promsa_viaje;
    private String cliente_viaje;
    private String obra_viaje;
    private String zona_viaje;
    private String agua_viaje;
    ///////////////////////////////////


    public Vehiculo() {
    }

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

    public String getZona() {return zona;}

    public void setZona(String zona) {this.zona = zona;}

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

    public String getOdometro() { return odometro; }

    public void setOdometro(String odometro) {this.odometro = odometro;}

    public String getCombustibleTotalUsado() {return combustibleTotalUsado;}

    public void setCombustibleTotalUsado(String combustibleTotalUsado) {
        this.combustibleTotalUsado = combustibleTotalUsado;
    }

    public String getHorasMotor() {
        return horasMotor;
    }

    public void setHorasMotor(String horasMotor) {
        this.horasMotor = horasMotor;
    }

    public String getCombustibleNivel() {
        return combustibleNivel;
    }

    public void setCombustibleNivel(String combustibleNivel) {
        this.combustibleNivel = combustibleNivel;
    }

    public String getTmpMotor() {
        return tmpMotor;
    }

    public void setTmpMotor(String tmpMotor) {
        this.tmpMotor = tmpMotor;
    }

    public String getDistanciaServicio() {
        return distanciaServicio;
    }

    public void setDistanciaServicio(String distanciaServicio) {
        this.distanciaServicio = distanciaServicio;
    }

    public String getCarga_descarga_viaje() {
        return carga_descarga_viaje;
    }

    public void setCarga_descarga_viaje(String carga_descarga_viaje) {
        this.carga_descarga_viaje = carga_descarga_viaje;
    }

    public String getAgua_total_viaje() {
        return agua_total_viaje;
    }

    public void setAgua_total_viaje(String agua_total_viaje) {
        this.agua_total_viaje = agua_total_viaje;
    }

    public String getHora_viaje() {
        return hora_viaje;
    }

    public void setHora_viaje(String hora_viaje) {
        this.hora_viaje = hora_viaje;
    }

    public String getM3_viaje() {
        return m3_viaje;
    }

    public void setM3_viaje(String m3_viaje) {
        this.m3_viaje = m3_viaje;
    }

    public String getCliente_promsa_viaje() {
        return cliente_promsa_viaje;
    }

    public void setCliente_promsa_viaje(String cliente_promsa_viaje) {
        this.cliente_promsa_viaje = cliente_promsa_viaje;
    }

    public String getIdviaje() {
        return idviaje;
    }

    public void setIdviaje(String idviaje) {
        this.idviaje = idviaje;
    }

    public String getCaducado_viaje() {
        return caducado_viaje;
    }

    public void setCaducado_viaje(String caducado_viaje) {
        this.caducado_viaje = caducado_viaje;
    }

    public String getAlbaran_viaje() {
        return albaran_viaje;
    }

    public void setAlbaran_viaje(String albaran_viaje) {
        this.albaran_viaje = albaran_viaje;
    }

    public String getObra_promsa_viaje() {
        return obra_promsa_viaje;
    }

    public void setObra_promsa_viaje(String obra_promsa_viaje) {
        this.obra_promsa_viaje = obra_promsa_viaje;
    }

    public String getCliente_viaje() {
        return cliente_viaje;
    }

    public void setCliente_viaje(String cliente_viaje) {
        this.cliente_viaje = cliente_viaje;
    }

    public String getObra_viaje() {
        return obra_viaje;
    }

    public void setObra_viaje(String obra_viaje) {
        this.obra_viaje = obra_viaje;
    }

    public String getZona_viaje() {
        return zona_viaje;
    }

    public void setZona_viaje(String zona_viaje) {
        this.zona_viaje = zona_viaje;
    }

    public String getAgua_viaje() {
        return agua_viaje;
    }

    public void setAgua_viaje(String agua_viaje) {
        this.agua_viaje = agua_viaje;
    }

    public String getRalenti() {return ralenti;}

    public void setRalenti(String ralenti) {this.ralenti = ralenti;}

    public String getCombustibleRalenti() {return combustibleRalenti;}

    public void setCombustibleRalenti(String combustibleRalenti) {
        this.combustibleRalenti = combustibleRalenti;
    }

    public String getFreno() {return freno;}

    public void setFreno(String freno) {this.freno = freno;}

    public String getSobrefreno() {return sobrefreno;}

    public void setSobrefreno(String sobrefreno) {
        this.sobrefreno = sobrefreno;
    }

    public String getSobrevelocidad() {return sobrevelocidad;}

    public void setSobrevelocidad(String sobrevelocidad) {
        this.sobrevelocidad = sobrevelocidad;
    }

    public String getSobreaceleracion() {
        return sobreaceleracion;
    }

    public void setSobreaceleracion(String sobreaceleracion) {
        this.sobreaceleracion = sobreaceleracion;
    }

    public String getPlanta() {return planta;}

    public void setPlanta(String planta) {this.planta = planta;}

    @Override
    public String toString() {
        return matricula;
    }
}
