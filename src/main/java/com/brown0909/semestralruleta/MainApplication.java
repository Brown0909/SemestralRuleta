package com.brown0909.semestralruleta;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("Presentacion.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 700);

        stage.setTitle("Presentación del Proyecto");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}