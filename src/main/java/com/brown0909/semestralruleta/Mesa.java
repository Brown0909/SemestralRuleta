package com.brown0909.final_programacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesa {

    private Ruleta ruleta;
    private List<Jugador> jugadores;
    private Crupier crupier;

    private final List<Integer> COLUMNA_1 = Arrays.asList(1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34);
    private final List<Integer> COLUMNA_2 = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35);
    private final List<Integer> COLUMNA_3 = Arrays.asList(3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36);

    public Mesa(Crupier crupier, Ruleta ruleta) {
        this.crupier = crupier;
        this.ruleta = ruleta;
        this.jugadores = new ArrayList<>();
    }

    public void agregarJugador(Jugador jugador) {
        this.jugadores.add(jugador);
    }

    public com.brown0909.final_programacion.ResultadoRonda iniciarRonda() {

        double totalApostado = 0;
        for (Jugador jugador : jugadores) {
            totalApostado += jugador.getApuestasActuales().stream().mapToDouble(com.brown0909.final_programacion.Apuesta::getMonto).sum();
        }

        com.brown0909.final_programacion.Resultado resultadoGiro = ruleta.girarRuleta();
        double gananciasTotales = evaluarApuestas(resultadoGiro);

        for (Jugador jugador : jugadores) {
            jugador.limpiarApuestas();
        }
        return new com.brown0909.final_programacion.ResultadoRonda(resultadoGiro, gananciasTotales, totalApostado);
    }

    private double evaluarApuestas(com.brown0909.final_programacion.Resultado resultado) {
        double gananciasRonda = 0;
        for (Jugador jugador : jugadores) {
            double gananciasJugador = 0;
            for (com.brown0909.final_programacion.Apuesta apuesta : jugador.getApuestasActuales()) {
                gananciasJugador += calcularGanancia(apuesta, resultado);
            }
            if (gananciasJugador > 0) {
                jugador.recibirGanancia(gananciasJugador);
            }
            gananciasRonda += gananciasJugador;
        }
        return gananciasRonda;
    }


    private double calcularGanancia(com.brown0909.final_programacion.Apuesta apuesta, com.brown0909.final_programacion.Resultado resultado) {
        String tipo = apuesta.getTipoApuesta();
        double monto = apuesta.getMonto();
        int numeroGanador = resultado.getNumero();
        String colorGanador = resultado.getColor();

        switch (tipo) {
            case "NUMERO": if (apuesta.getNumeroApostado() == numeroGanador) return monto * 36; break;
            case "0": if (numeroGanador == 0) return monto * 36; break;
            case "00": if (numeroGanador == 37) return monto * 36; break;
            case "ROJO": if ("ROJO".equals(colorGanador)) return monto * 2; break;
            case "NEGRO": if ("NEGRO".equals(colorGanador)) return monto * 2; break;
            case "PAR": if (numeroGanador > 0 && numeroGanador < 37 && numeroGanador % 2 == 0) return monto * 2; break;
            case "IMPAR": if (numeroGanador > 0 && numeroGanador < 37 && numeroGanador % 2 != 0) return monto * 2; break;
            case "1-18": if (numeroGanador >= 1 && numeroGanador <= 18) return monto * 2; break;
            case "19-36": if (numeroGanador >= 19 && numeroGanador <= 36) return monto * 2; break;
            case "1ST 12": if (numeroGanador >= 1 && numeroGanador <= 12) return monto * 3; break;
            case "2ND 12": if (numeroGanador >= 13 && numeroGanador <= 24) return monto * 3; break;
            case "3RD 12": if (numeroGanador >= 25 && numeroGanador <= 36) return monto * 3; break;
            case "COLUMNA_1": if (COLUMNA_1.contains(numeroGanador)) return monto * 3; break;
            case "COLUMNA_2": if (COLUMNA_2.contains(numeroGanador)) return monto * 3; break;
            case "COLUMNA_3": if (COLUMNA_3.contains(numeroGanador)) return monto * 3; break;
        }
        return 0;
    }
}