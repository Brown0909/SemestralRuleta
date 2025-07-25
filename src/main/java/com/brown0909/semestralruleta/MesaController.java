package com.brown0909.semestralruleta;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MesaRuletaController {

    @FXML private ImageView rouletteImage;
    @FXML private Label chipsLabel;
    @FXML private Label messageLabel;
    @FXML private Button spinButton;
    @FXML private GridPane bettingGrid;
    @FXML private HBox chipPane;
    @FXML private VBox setupPane;
    @FXML private TextField playerNameField;
    @FXML private TextField playerAmountField;
    @FXML private TextArea apostasActivasArea;

    @FXML private VBox endOfRoundPane;
    @FXML private Button continueButton;
    @FXML private Button addFundsButton;
    @FXML private Button retireButton;

    private Mesa mesa;
    private Jugador jugador;
    private double montoApuestaActual = 10.0;
    private boolean sePuedeApostar = true;

    private final String[] ORDEN_RUEDA = {
            "0", "28", "9", "26", "30", "11", "7", "20", "32", "17", "5", "22", "34", "15", "3", "24",
            "36", "13", "1", "00", "27", "10", "25", "29", "12", "8", "19", "31", "18", "6", "21",
            "33", "16", "4", "23", "35", "14", "2"
    };

    @FXML
    void handleStartGameAction(ActionEvent event) {
        String playerName = playerNameField.getText();
        String amountText = playerAmountField.getText();

        if (playerName.trim().isEmpty()) {
            messageLabel.setText("Por favor, introduce un nombre.");
            return;
        }
        if (!playerName.matches("^[a-zA-Z\\s]+$")) {
            messageLabel.setText("El nombre solo puede contener letras y espacios.");
            return;
        }
        double initialAmount;
        try {
            initialAmount = Double.parseDouble(amountText);
            if (initialAmount <= 0) {
                messageLabel.setText("El monto debe ser mayor que cero.");
                return;
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Por favor, introduce un monto válido.");
            return;
        }

        this.jugador = new Jugador(playerName, initialAmount);
        Ruleta ruleta = new Ruleta();
        Crupier crupier = new Crupier("Crupier AI");
        this.mesa = new Mesa(crupier, ruleta);
        this.mesa.agregarJugador(jugador);

        this.sePuedeApostar = true;

        messageLabel.setText("¡Bienvenido, " + jugador.getNombreJugador() + "! Haz tus apuestas.");
        actualizarSaldoLabel();
        apostasActivasArea.clear();

        setupPane.setVisible(false);
        setupPane.setManaged(false);

        spinButton.setVisible(true);
        spinButton.setManaged(true);
        spinButton.setDisable(false);

        apostasActivasArea.setVisible(true);
        apostasActivasArea.setManaged(true);

        bettingGrid.setDisable(false);
        chipPane.setDisable(false);
    }

    @FXML
    void handleChipSelection(ActionEvent event) {
        if (!sePuedeApostar) return;
        String chipValue = ((Button) event.getSource()).getText();
        this.montoApuestaActual = Double.parseDouble(chipValue);
        messageLabel.setText("Ficha seleccionada: $" + this.montoApuestaActual);
    }

    @FXML
    void handleBetPlacement(ActionEvent event) {
        if (!sePuedeApostar) {
            messageLabel.setText("Las apuestas están cerradas.");
            return;
        }
        Button botonPulsado = (Button) event.getSource();
        String tipoApuesta;
        String textoBoton = botonPulsado.getText();
        if (botonPulsado.getUserData() != null) {
            tipoApuesta = (String) botonPulsado.getUserData();
        } else {
            tipoApuesta = textoBoton.toUpperCase();
        }
        Apuesta nuevaApuesta = new Apuesta(tipoApuesta, montoApuestaActual);
        try {
            int numero = Integer.parseInt(textoBoton);
            nuevaApuesta.setTipoApuesta("NUMERO");
            nuevaApuesta.setNumeroApostado(numero);
        } catch (NumberFormatException e) {

        }
        if (jugador.realizarApuesta(nuevaApuesta)) {
            messageLabel.setText("Apuesta de $" + montoApuestaActual + " a " + textoBoton);
            actualizarSaldoLabel();
            actualizarDisplayDeApuestas();
        } else {
            messageLabel.setText("¡Saldo insuficiente para esa apuesta!");
        }
    }

    @FXML
    void handleSpinButtonAction(ActionEvent event) {
        if (jugador.getApuestasActuales().isEmpty()) {
            messageLabel.setText("Debe realizar al menos una apuesta para girar.");
            return;
        }
        sePuedeApostar = false;
        spinButton.setDisable(true);
        bettingGrid.setDisable(true);
        messageLabel.setText("¡La rueda está girando! No más apuestas.");
        ResultadoRonda resultadoRonda = mesa.iniciarRonda();
        animarRuletaSimple(resultadoRonda);
    }

    private void animarRuletaSimple(ResultadoRonda resultadoRonda) {
        Resultado resultadoFinal = resultadoRonda.getResultadoGiro();
        String numeroGanadorStr = resultadoFinal.getNumero() == 37 ? "00" : String.valueOf(resultadoFinal.getNumero());

        double gradosPorCasilla = 360.0 / ORDEN_RUEDA.length;
        int indiceGanador = -1;
        for (int i = 0; i < ORDEN_RUEDA.length; i++) {
            if (ORDEN_RUEDA[i].equals(numeroGanadorStr)) {
                indiceGanador = i;
                break;
            }
        }

        double anguloFinal = (360 * 5) + (indiceGanador * gradosPorCasilla) - rouletteImage.getRotate();

        RotateTransition rt = new RotateTransition(Duration.seconds(4), rouletteImage);
        rt.setByAngle(anguloFinal);
        rt.setInterpolator(Interpolator.EASE_OUT);

        rt.setOnFinished(e -> {
            double totalRetornado = resultadoRonda.getGanancias();
            double totalApostado = resultadoRonda.getTotalApostado();
            double profit = totalRetornado - totalApostado;
            String mensajeBase = "El número ganador es: " + numeroGanadorStr + " " + resultadoFinal.getColor();

            if (profit > 0) {
                messageLabel.setText(mensajeBase + String.format(". ¡Ganaste $%.2f!", profit));
            } else {
                messageLabel.setText(mensajeBase + ". ¡Mejor suerte la próxima vez!");
            }
            actualizarSaldoLabel();

            spinButton.setVisible(false);
            spinButton.setManaged(false);
            apostasActivasArea.setVisible(false);
            apostasActivasArea.setManaged(false);

            endOfRoundPane.setVisible(true);
            endOfRoundPane.setManaged(true);

            if (jugador.getSaldo() <= 0) {
                messageLabel.setText(mensajeBase + ". ¡Te has quedado sin saldo!");
                continueButton.setVisible(false);
                continueButton.setManaged(false);
            } else {
                continueButton.setVisible(true);
                continueButton.setManaged(true);
            }
        });

        rt.play();
    }

    @FXML
    void handleContinueAction(ActionEvent event) {
        sePuedeApostar = true;
        bettingGrid.setDisable(false);
        apostasActivasArea.clear();
        messageLabel.setText("¡Perfecto! Realiza tus apuestas para la siguiente ronda.");

        endOfRoundPane.setVisible(false);
        endOfRoundPane.setManaged(false);
        spinButton.setVisible(true);
        spinButton.setManaged(true);
        spinButton.setDisable(false);
        apostasActivasArea.setVisible(true);
        apostasActivasArea.setManaged(true);
    }

    @FXML
    void handleAddFundsAction(ActionEvent event) {
        endOfRoundPane.setVisible(false);
        endOfRoundPane.setManaged(false);
        reiniciarJuegoParaNuevoSaldo();
    }

    @FXML
    void handleRetireAction(ActionEvent event) {
        mostrarAlertaFinal("¡Gracias por jugar! Te retiras con: " + String.format("$%.2f", jugador.getSaldo()));
    }

    private void actualizarDisplayDeApuestas() {
        StringBuilder sb = new StringBuilder("Apuestas actuales:\n");
        for (Apuesta apuesta : jugador.getApuestasActuales()) {
            String tipo = apuesta.getTipoApuesta();
            if (tipo.equals("NUMERO")) {
                tipo = String.valueOf(apuesta.getNumeroApostado());
            }
            sb.append(String.format("- $%.2f a %s\n", apuesta.getMonto(), tipo));
        }
        apostasActivasArea.setText(sb.toString());
    }

    private void reiniciarJuegoParaNuevoSaldo() {
        setupPane.setVisible(true);
        setupPane.setManaged(true);

        spinButton.setVisible(false);
        spinButton.setManaged(false);
        bettingGrid.setDisable(true);
        chipPane.setDisable(true);
        apostasActivasArea.setVisible(false);
        apostasActivasArea.setManaged(false);

        playerAmountField.clear();
        chipsLabel.setText("Saldo: $0.00");
        messageLabel.setText(jugador.getNombreJugador() + ", introduce un nuevo monto para continuar.");
    }

    private void mostrarAlertaFinal(String mensaje) {
        Alert alertaFinal = new Alert(Alert.AlertType.INFORMATION);
        alertaFinal.setTitle("Juego Terminado");
        alertaFinal.setHeaderText(null);
        alertaFinal.setContentText(mensaje);
        alertaFinal.showAndWait();
        Platform.exit();
    }

    private void actualizarSaldoLabel() {
        chipsLabel.setText(String.format("Saldo: $%.2f", jugador.getSaldo()));
    }
}