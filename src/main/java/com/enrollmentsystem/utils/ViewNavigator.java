package com.enrollmentsystem.utils;

import com.enrollmentsystem.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class ViewNavigator {
    public static void logout(Node root) {
        try {
            FXMLLoader loginLoader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/login/Login.fxml"));
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

    public static void switchContent(String module, String fxmlName, BorderPane root) {
        try {
            FXMLLoader contentLoader = new FXMLLoader(
                    App.class.getResource("/com/enrollmentsystem/views/dashboard/" + module + "/" + fxmlName +".fxml")
            );
            Parent contentRoot = contentLoader.load();
            contentRoot.getStylesheets().add(
                Objects.requireNonNull(App.class.getResource(
                        "/com/enrollmentsystem/styles/" + fxmlName.toLowerCase() + ".css")
                ).toExternalForm()
            );

            root.setCenter(contentRoot);
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createAddStudentDialog(Stage ownerStage) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/enrollment/EnrollmentForm.fxml"));
            Parent dialogLoader = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setResizable(false);
            dialog.setMaximized(true);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initOwner(ownerStage);

            Scene dialogScene = new Scene(dialogLoader);
            dialogScene.setFill(Color.TRANSPARENT);
            dialogScene.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource(
                            "/com/enrollmentsystem/styles/enrollmentform.css"
                    )).toExternalForm()
            );

            dialog.setScene(dialogScene);
            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exitAddPopup(Button exitBtn) {
        Stage popupStage = (Stage) exitBtn.getScene().getWindow();
        popupStage.close();
    }
}
