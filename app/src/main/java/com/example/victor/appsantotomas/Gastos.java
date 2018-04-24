package com.example.victor.appsantotomas;

public class Gastos {
    int id_egreso;
    int id_tipo_gasto;
    int monto;

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    int id_usuario;
    String detalle;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    String fecha;

    public Gastos() {
    }

    public Gastos(int id_usuario, int id_tipo_gasto, int monto, String detalle) {
        this.id_egreso = id_usuario;
        this.id_tipo_gasto = id_tipo_gasto;
        this.monto = monto;
        this.detalle = detalle;

    }

    public int getId_egreso() {
        return id_egreso;
    }

    public void setId_egreso(int id_egreso) {
        this.id_egreso = id_egreso;
    }

    public int getId_tipo_gasto() {
        return id_tipo_gasto;
    }

    public void setId_tipo_gasto(int id_tipo_gasto) {
        this.id_tipo_gasto = id_tipo_gasto;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
