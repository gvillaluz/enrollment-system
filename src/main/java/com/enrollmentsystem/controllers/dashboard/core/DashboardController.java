package com.enrollmentsystem.controllers.dashboard.core;

import com.enrollmentsystem.utils.Scroll;
import com.enrollmentsystem.viewmodels.core.DashboardViewModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class DashboardController {
    @FXML private ScrollPane scrollPane;
    @FXML private PieChart grade11Chart;
    @FXML private PieChart grade12Chart;

    @FXML private Label schoolYearLabel, totalSectionsGrade11, totalSectionsGrade12, grade11Label, grade12Label;

    @FXML private Button refreshBtn;

    private final DashboardViewModel viewModel = new DashboardViewModel();

    @FXML
    public void initialize() {
        Scroll.fixScrollSpeed(scrollPane);
        setupLabel();
        setupChart();
        setupRefreshButton();

        viewModel.loadData();
    }

    @FXML
    public void onRefresh(ActionEvent event) {
        viewModel.loadData();
    }

    private void setupLabel() {
        schoolYearLabel.textProperty().bind(viewModel.schoolYearProperty());
        totalSectionsGrade11.textProperty().bind(
                Bindings.when(viewModel.totalGrade11SectionProperty().isEqualTo(0))
                        .then("No sections yet")
                        .otherwise(viewModel.totalGrade11SectionProperty().asString())
        );
        totalSectionsGrade12.textProperty().bind(
                Bindings.when(viewModel.totalGrade12SectionProperty().isEqualTo(0))
                        .then("No sections yet")
                        .otherwise(viewModel.totalGrade11SectionProperty().asString())
        );
        grade11Label.textProperty().bind(viewModel.totalGrade11().asString());
        grade12Label.textProperty().bind(viewModel.totalGrade12().asString());
    }

    private void setupChart() {
        grade11Chart.setData(viewModel.getGrade11Data());
        grade12Chart.setData(viewModel.getGrade12Data());
    }

    private void setupRefreshButton() {
        FontIcon refreshIcon = new FontIcon("fas-redo");
        refreshIcon.getStyleClass().add("refresh-icon");

        refreshBtn.setGraphic(refreshIcon);
        refreshBtn.setGraphicTextGap(8);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), refreshIcon);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);

        viewModel.loadingProperty().addListener((obs, wasProcessing, isNowProcessing) -> {
            if (isNowProcessing) {
                rotateTransition.play();
            } else {
                rotateTransition.stop();
                refreshIcon.setRotate(0);
            }
        });
    }
}
