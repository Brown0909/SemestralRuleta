package com.brown0909.final_programacion;

public class Resultado {
    private final int numero;
    private final String color;

    public Resultado(int numero, String color) {
        this.numero = numero;
        this.color = color;
    }


    public int getNumero() {
        return numero;
    }

    public String getColor() {
        return color;
    }
}