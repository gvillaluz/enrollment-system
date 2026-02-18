package com.enrollmentsystem.controllers.dashboard.core;

import com.enrollmentsystem.controllers.BaseController;
import com.enrollmentsystem.utils.Scroll;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;

public class DashboardController extends BaseController {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private PieChart grade11Chart;
    @FXML
    private PieChart grade12Chart;

    @FXML
    public void initialize() {
        Scroll.fixScrollSpeed(scrollPane);

        ObservableList<PieChart.Data> grade11data = FXCollections.observableArrayList(
                new PieChart.Data("Female (46.5%)", 47),
                new PieChart.Data("Male (53.5%)", 54)
        );
        ObservableList<PieChart.Data> grade12Data = FXCollections.observableArrayList(
                new PieChart.Data("Female (46.5%)", 47),
                new PieChart.Data("Male (53.5%)", 54)
        );

        grade11Chart.setData(grade11data);
        grade12Chart.setData(grade12Data);
    }
}
