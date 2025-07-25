package com.brown0909.semestralruleta;
import java.util.Random;


public class Ruleta {

    private String tipoRuleta;
    private int numeroActual;
    private String colorActual;
    private final Random random;


    private final int[] NUMEROS_ROJOS = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};

    public Ruleta() {
        this.tipoRuleta = "Americana";
        this.random = new Random();
    }


    public Resultado girarRuleta() {

        this.numeroActual = random.nextInt(38);
        this.colorActual = obtenerColor(this.numeroActual);
        return new Resultado(this.numeroActual, this.colorActual);
    }


    private String obtenerColor(int numero) {
        if (numero == 0 || numero == 37) {
            return "VERDE";
        }


        for (int numRojo : NUMEROS_ROJOS) {
            if (numRojo == numero) {
                return "ROJO";
            }
        }

        return "NEGRO";
    }
}