package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.SectionDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SectionManagementViewModel {
    private final ObservableList<SectionViewModel> sections = FXCollections.observableArrayList();

    public ObservableList<SectionViewModel> getSections() { return sections; }

    public void loadSections() {
        sections.addAll(
                new SectionViewModel(new SectionDTO(1, "STEM 12-A", 45, 1, "2025-2026", 1, "STEM", "Room 301")),
                new SectionViewModel(new SectionDTO(2, "STEM 12-B", 40, 1, "2025-2026", 1, "STEM", "Room 302")),

                // ACADEMIC TRACK - ABM
                new SectionViewModel(new SectionDTO(3, "ABM 12-A", 45, 1, "2025-2026", 2, "ABM", "Room 201")),
                new SectionViewModel(new SectionDTO(4, "ABM 12-B", 45, 1, "2025-2026", 2, "ABM", "Room 202")),

                // ACADEMIC TRACK - HUMSS
                new SectionViewModel(new SectionDTO(5, "HUMSS 12-1", 50, 1, "2025-2026", 3, "HUMSS", "Room 101")),
                new SectionViewModel(new SectionDTO(6, "HUMSS 12-2", 50, 1, "2025-2026", 3, "HUMSS", "Room 102")),

                // TECH-VOC TRACK - ICT
                new SectionViewModel(new SectionDTO(7, "ICT 12-PROG", 35, 1, "2025-2026", 6, "ICT", "Comp Lab 1")),
                new SectionViewModel(new SectionDTO(8, "ICT 12-NET", 35, 1, "2025-2026", 6, "ICT", "Comp Lab 2")),

                // TECH-VOC TRACK - HE
                new SectionViewModel(new SectionDTO(9, "HE 12-COOK", 40, 1, "2025-2026", 5, "HE", "Kitchen 1")),
                new SectionViewModel(new SectionDTO(10, "HE 12-TOUR", 40, 1, "2025-2026", 5, "HE", "Function Hall"))
        );
    }
}
