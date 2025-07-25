package com.brown0909.semestralruleta;


import java.util.ArrayList;
import java.util.List;


public class Jugador {

    private String nombreJugador;
    private double saldo;
    private List<com.brown0909.semestralruleta.Apuesta> apuestasActuales;

    public Jugador(String nombreJugador, double saldoInicial) {
        this.nombreJugador = nombreJugador;
        this.saldo = saldoInicial;
        this.apuestasActuales = new ArrayList<>();
    }


    public boolean realizarApuesta(com.brown0909.semestralruleta.Apuesta apuesta) {
        if (apuesta.getMonto() > this.saldo) {
            System.out.println("Saldo insuficiente para realizar la apuesta.");
            return false;
        }
        this.saldo -= apuesta.getMonto();
        this.apuestasActuales.add(apuesta);
        System.out.println(nombreJugador + " ha apostado $" + apuesta.getMonto() + " a " + apuesta.getTipoApuesta());
        return true;
    }

    public void recibirGanancia(double cantidad) {
        this.saldo += cantidad;
    }

    public void limpiarApuestas() {
        this.apuestasActuales.clear();
    }


    public String getNombreJugador() {
        return nombreJugador;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<com.brown0909.semestralruleta.Apuesta> getApuestasActuales() {
        return apuestasActuales;
    }
}
