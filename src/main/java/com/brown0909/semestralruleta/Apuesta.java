package com.brown0909.semestralruleta;


public class Apuesta {

    private String tipoApuesta;
    private double monto;
    private int numeroApostado;

    public Apuesta(String tipoApuesta, double monto) {
        this.tipoApuesta = tipoApuesta;
        this.monto = monto;
        this.numeroApostado = -1;
    }


    public String getTipoApuesta() {
        return tipoApuesta;
    }

    public double getMonto() {
        return monto;
    }

    public int getNumeroApostado() {
        return numeroApostado;
    }


    public void setNumeroApostado(int numeroApostado) {
        this.numeroApostado = numeroApostado;
    }

    public void setTipoApuesta(String numero) {
    }
}
