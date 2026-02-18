package com.enrollmentsystem.viewmodels.enrollment;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.EnrollmentSummaryDTO;
import com.enrollmentsystem.models.Student;
import com.enrollmentsystem.services.EnrollmentService;
import com.enrollmentsystem.services.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class EnrollmentViewModel {
    private final EnrollmentService _service = AppContext.getEnrollmentService();
    private final ObservableList<EnrollmentSummaryViewModel> studentList = FXCollections.observableArrayList();
    ObservableList<EnrollmentSummaryViewModel> dummyList = FXCollections.observableArrayList();

    public ObservableList<EnrollmentSummaryViewModel> getEnrollmentList() {
        return dummyList;
    }

    public void loadData() {
//        List<EnrollmentSummaryDTO> dtos = _service.getTopSummaries();
//
//        for (EnrollmentSummaryDTO dto : dtos) {
//            var row = new EnrollmentSummaryViewModel(dto);
//            studentList.add(row);
//        }

        var dummy = new EnrollmentSummaryDTO("109823746512", "Santos", "Maria Clara", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", "Enrolled");

        var dummy2 = new EnrollmentSummaryDTO("109823746512", "Santos", "Maria Clara", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", "Enrolled");

        var dummy3 = new EnrollmentSummaryDTO("109823746512", "Santos", "Maria Clara", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", "Enrolled");

        var dummy4 = new EnrollmentSummaryDTO("109823746512", "Santos", "Maria Clara", "Reyes",
                "2025-2026", 11, "1st", "Academic", "STEM", "Einstein", "Enrolled");

        var newDummy = new EnrollmentSummaryViewModel(dummy);
        var newDummy2 = new EnrollmentSummaryViewModel(dummy);
        var newDummy3 = new EnrollmentSummaryViewModel(dummy3);
        var newDummy4 = new EnrollmentSummaryViewModel(dummy4);
        dummyList.add(newDummy);
        dummyList.add(newDummy2);
        dummyList.add(newDummy3);
        dummyList.add(newDummy4);
    }
}
