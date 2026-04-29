package com.enrollmentsystem.viewmodels.core;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.DashboardDTO;
import com.enrollmentsystem.services.DashboardService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class DashboardViewModel {
    private final IntegerProperty schoolYearId = new SimpleIntegerProperty();
    private final StringProperty schoolYear = new SimpleStringProperty();
    private final IntegerProperty totalGrade11Section = new SimpleIntegerProperty();
    private final IntegerProperty totalGrade12Section = new SimpleIntegerProperty();
    private final IntegerProperty totalGrade11 = new SimpleIntegerProperty();
    private final IntegerProperty totalGrade12 = new SimpleIntegerProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final ObservableList<PieChart.Data> grade11Data = FXCollections.observableArrayList();
    private final ObservableList<PieChart.Data> grade12Data = FXCollections.observableArrayList();

    private final DashboardService _service = AppContext.getDashboardService();

    public IntegerProperty schoolYearIdProperty() { return schoolYearId; }
    public StringProperty schoolYearProperty() { return schoolYear; }
    public IntegerProperty totalGrade11SectionProperty() { return totalGrade11Section; }
    public IntegerProperty totalGrade12SectionProperty() { return totalGrade12Section; }
    public IntegerProperty totalGrade11() { return totalGrade11; }
    public IntegerProperty totalGrade12() { return totalGrade12; }
    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<PieChart.Data> getGrade11Data() { return grade11Data; }
    public ObservableList<PieChart.Data> getGrade12Data() { return grade12Data; }

    public void loadData() {
        isLoading.set(true);
        _service.getDashboardData()
                .thenAccept(dashboardDTO -> {
                    Platform.runLater(() -> {
                        if (dashboardDTO != null) {
                            schoolYearId.set(dashboardDTO.getSchoolYearId());
                            schoolYear.set(dashboardDTO.getSchoolYear());
                            totalGrade11Section.set(dashboardDTO.getTotalGrade11Section());
                            totalGrade12Section.set(dashboardDTO.getTotalGrade12Section());
                            totalGrade11.set(dashboardDTO.getTotalGrade11());
                            totalGrade12.set(dashboardDTO.getTotalGrade12());

                            loadChartData(dashboardDTO);
                        }

                        isLoading.set(false);
                    });
                });
    }

    private void loadChartData(DashboardDTO dto) {
        updatePieData(grade11Data, dto.getG11Male(), dto.getG11Female(), dto.getTotalGrade11());
        updatePieData(grade12Data, dto.getG12Male(), dto.getG12Female(), dto.getTotalGrade12());
    }

    private void updatePieData(ObservableList<PieChart.Data> dataList, int male, int female, int total) {
        if (total > 0) {
            double malePercent = (male * 100.0) / total;
            double femalePercent = (female * 100.0) / total;

            dataList.setAll(
                    new PieChart.Data(String.format("Male (%.1f%%)", malePercent), male),
                    new PieChart.Data(String.format("Female (%.1f%%)", femalePercent), female)
            );
        } else {
            dataList.clear();
        }
    }
}
