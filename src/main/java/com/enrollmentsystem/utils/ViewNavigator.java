package com.enrollmentsystem.utils;

import com.enrollmentsystem.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewNavigator {
    public static void navigateLogin(Node root) {
        try {
            FXMLLoader loginLoader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/login/LoginView.fxml"));
            Parent loginRoot = loginLoader.load();

            Scene currentScene = root.getScene();

            currentScene.getStylesheets().clear();
            currentScene.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("styles/login.css")).toExternalForm()
            );

            currentScene.setRoot(loginRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchContent(String fxmlName) {

    }
}
