package com.enrollmentsystem.controllers.dashboard.enrollment;

import com.enrollmentsystem.utils.ViewNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

public class EnrollmentFormController {
//    @FXML private ToggleButton learnerTab, acadTab, requirementsTab;
//    private ToggleGroup navTab;

    @FXML
    public void initialize() {
//        navTab = new ToggleGroup();
//        learnerTab.setToggleGroup(navTab);
//        acadTab.setToggleGroup(navTab);
//        requirementsTab.setToggleGroup(navTab);
//
//        learnerTab.setSelected(true);
    }

    @FXML
    private void exitPopup(ActionEvent event) {
        Button exitBtn = (Button) event.getSource();

        ViewNavigator.exitAddPopup(exitBtn);
    }
}
