package com.enrollmentsystem.utils;

import com.enrollmentsystem.App;
import com.enrollmentsystem.controllers.shared.ConfirmationModalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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
                    Objects.requireNonNull(App.class.getResource("styles/login/login.css")).toExternalForm()
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
                        "/com/enrollmentsystem/styles/" + module + "/" + fxmlName.toLowerCase() + ".css")
                ).toExternalForm()
            );

            root.setCenter(contentRoot);
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public static void exitAddPopup(Button exitBtn) {
        Stage popupStage = (Stage) exitBtn.getScene().getWindow();
        popupStage.close();
    }

    public static void showModal(Parent modalLoader, Stage ownerStage) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("stack-pane");
        overlay.getChildren().add(modalLoader);

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);
        modal.setMaximized(true);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(ownerStage);

        Scene modalScene = new Scene(overlay);
        modalScene.setFill(Color.TRANSPARENT);
        modalScene.getStylesheets().add(
                Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/modal.css")).toExternalForm()
        );

        modal.setScene(modalScene);
        modal.showAndWait();
    }

    public static void showDeleteModal(Stage owner, Runnable onConfirmDelete) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/shared/ConfirmDelete.fxml"));
            Parent content = loader.load();
            content.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/confirm-modal.css")).toExternalForm()
            );

            ConfirmationModalController controller = loader.getController();
            controller.setup(onConfirmDelete);

            showConfirmationModal(content, owner);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load delete modal: " + e.getMessage());
        }
    }

    public static void showArchiveModal(Stage owner, Runnable onConfirmArchive) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/enrollmentsystem/views/dashboard/shared/ConfirmArchive.fxml"));
            Parent content = loader.load();
            content.getStylesheets().add(
                    Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/confirm-modal.css")).toExternalForm()
            );

            ConfirmationModalController controller = loader.getController();
            controller.setup(onConfirmArchive);

            showConfirmationModal(content, owner);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load archive modal: " + e.getMessage());
        }
    }

    private static void showConfirmationModal(Parent content, Stage owner) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("stack-pane");
        overlay.getChildren().add(content);

        Stage modal = new Stage();
        modal.initModality(Modality.APPLICATION_MODAL);
        modal.setResizable(false);
        modal.setMaximized(true);
        modal.initStyle(StageStyle.TRANSPARENT);
        modal.initOwner(owner);

        Scene modalContent = new Scene(overlay);
        modalContent.setFill(Color.TRANSPARENT);
        modalContent.getStylesheets().add(
                Objects.requireNonNull(App.class.getResource("/com/enrollmentsystem/styles/shared/modal.css")).toExternalForm()
        );

        modal.setScene(modalContent);
        modal.showAndWait();
    }
}
