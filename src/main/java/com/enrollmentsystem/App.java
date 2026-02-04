package com.enrollmentsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fr.brouillard.oss.cssfx.CSSFX;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CSSFX.start();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/login/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getStylesheets().add(
                Objects.requireNonNull(App.class.getResource("styles/login.css")).toExternalForm()
        );

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}