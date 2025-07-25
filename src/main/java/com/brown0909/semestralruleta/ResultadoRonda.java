package com.brown0909.final_programacion;

public class ResultadoRonda {
    private final Resultado resultadoGiro;
    private final double ganancias;
    private final double totalApostado;

    public ResultadoRonda(Resultado resultadoGiro, double ganancias, double totalApostado) {
        this.resultadoGiro = resultadoGiro;
        this.ganancias = ganancias;
        this.totalApostado = totalApostado;
    }

    public Resultado getResultadoGiro() {
        return resultadoGiro;
    }

    public double getGanancias() {
        return ganancias;
    }

    public double getTotalApostado() {
        return totalApostado;
    }
}