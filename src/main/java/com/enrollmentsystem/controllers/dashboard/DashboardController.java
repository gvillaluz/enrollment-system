package com.enrollmentsystem.controllers.dashboard;

import com.enrollmentsystem.controllers.BaseController;
import com.enrollmentsystem.models.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.events.MouseEvent;

public class DashboardController extends BaseController {
    @FXML
    private BorderPane root;
    @FXML
    private HBox addStudentBtn;
    @FXML
    private VBox subMenuBtns;
    @FXML
    private ImageView arrowIcon;
    @FXML
    private Label welcomeLabel;
    @FXML
    private ScrollPane mainContent;

    private Button activeButton;

    @FXML
    public void initialize() {
        BaseController.setSession(UserSession.getInstance());
        BaseController.setRoot(root);

        welcomeLabel.setText("Welcome, " + BaseController.session.getUser().getFirstName() + "!");

        subMenuBtns.setVisible(false);
        subMenuBtns.setManaged(false);
        mainContent.setFocusTraversable(false);
        addStudentBtn.setOnMouseClicked(event -> {
            boolean isVisible = subMenuBtns.isVisible();
            subMenuBtns.setVisible(!isVisible);
            subMenuBtns.setManaged(!isVisible);
            arrowIcon.setRotate(!isVisible ? 90 : 0);
        });
    }

    @FXML
    private void handleClick(ActionEvent event) {
        Button clickedBtn = (Button) event.getSource();
        String id = clickedBtn.getId();

        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }

        clickedBtn.getStyleClass().add("active");

        activeButton = clickedBtn;
        System.out.println(activeButton.getText());
    }

    @FXML
    private void logoutUser() {
        BaseController.logout();
    }
}
