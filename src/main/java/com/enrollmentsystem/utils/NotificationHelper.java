package com.enrollmentsystem.utils;

import com.enrollmentsystem.App;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;

import java.util.Objects;

public class NotificationHelper {
    public static void showToast(Window owner, String message, String type) {
        ImageView icon = new ImageView(
                new Image(
                        Objects.requireNonNull(App.class.getResourceAsStream("/com/enrollmentsystem/assets/icons/success-icon.png"))
                )
        );
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);
        icon.getStyleClass().add("notification-icon");

        Notifications.create()
                .owner(owner)
                .graphic(icon)
                .styleClass(type)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .show();
    }
}
