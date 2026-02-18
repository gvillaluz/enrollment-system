package com.enrollmentsystem.controllers.dashboard.core;

import com.enrollmentsystem.controllers.BaseController;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.utils.ViewNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController extends BaseController {
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
    private Button dashboardButton;

    private Button activeButton;

    @FXML
    public void initialize() {
        BaseController.setSession(UserSession.getInstance());
        BaseController.setRoot(root);

        welcomeLabel.setText("Welcome, " + BaseController.session.getUser().getFirstName() + "!");

        dashboardButton.getStyleClass().add("active");
        ViewNavigator.switchContent(getFolderFor(dashboardButton.getId()), dashboardButton.getId(), root);
        activeButton = dashboardButton;

        subMenuBtns.setVisible(false);
        subMenuBtns.setManaged(false);
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

        if (id.equals("AddGrade11")) {
            BaseController.setCurrentGradeLevel(11);
            id = "Enrollment";
        }
        else if (id.equals("AddGrade12")) {
            BaseController.setCurrentGradeLevel(12);
            id = "Enrollment";
        }

        String module = getFolderFor(id);
        ViewNavigator.switchContent(module, id, root);

        if (activeButton != null) {
            activeButton.getStyleClass().remove("active");
        }
        clickedBtn.getStyleClass().add("active");
        activeButton = clickedBtn;
    }

    @FXML
    private void logoutUser() {
        BaseController.logout();
    }

    private String getFolderFor(String id) {
        return switch (id) {
            case "Dashboard" -> "core";
            case "Enrollment", "ClasslistGenerator", "RequirementsChecklist", "RunBatchSectioning",
                 "StudentRecords" -> "enrollment";
            case "ManageSections", "ManageStrands", "ManageTracks", "SchoolYearModule" -> "academic";
            default -> "admin";
        };
    }
}
