package com.brown0909.final_programacion;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class PresentacionController {

    public void handleContinueAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MesaRuleta.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

            Stage gameStage = new Stage();
            gameStage.setTitle("Ruleta Americana");
            gameStage.setScene(scene);
            gameStage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error al Cargar");
            errorAlert.setHeaderText("No se pudo cargar la pantalla del juego.");
            errorAlert.setContentText("Asegúrate de que los archivos FXML y Controller estén correctos y sincronizados. Causa: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }
}