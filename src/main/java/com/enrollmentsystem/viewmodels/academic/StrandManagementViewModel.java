package com.enrollmentsystem.viewmodels.academic;

import com.enrollmentsystem.dtos.StrandDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StrandManagementViewModel {
    private final ObservableList<StrandViewModel> strands = FXCollections.observableArrayList();
    private final StrandViewModel formViewModel = new StrandViewModel(new StrandDTO());

    public StrandViewModel getFormViewModel() { return formViewModel; }
    public ObservableList<StrandViewModel> getStrands() { return strands; }

    public void loadStrands() {
        strands.addAll(
                new StrandViewModel(new StrandDTO(1, "STEM", "Science, Technology, Engineering, and Mathematics", 1, "Academic")),
                new StrandViewModel(new StrandDTO(2, "ABM", "Accountancy, Business, and Management", 1, "Academic")),
                new StrandViewModel(new StrandDTO(3, "HUMSS", "Humanities and Social Sciences", 1, "Academic")),
                new StrandViewModel(new StrandDTO(4, "GAS", "General Academic Strand", 1, "Academic")),

                // TECH-VOC TRACK (TrackId: 2)
                new StrandViewModel(new StrandDTO(5, "HE", "Home Economics", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(6, "ICT", "Information and Communications Technology", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(7, "IA", "Industrial Arts", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(8, "AFA", "Agri-Fishery Arts", 2, "Tech-Voc")),

                // DUMMY DATA FOR SCROLLING/TESTING
                new StrandViewModel(new StrandDTO(9, "STEM-B", "STEM - Health Sciences Focus", 1, "Academic")),
                new StrandViewModel(new StrandDTO(10, "ICT-PROG", "ICT - Computer Programming", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(11, "HE-CUL", "HE - Culinary Arts", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(12, "IA-ELEC", "IA - Electrical Installation", 2, "Tech-Voc")),
                new StrandViewModel(new StrandDTO(13, "SPORTS", "Sports Track", 3, "Sports")),
                new StrandViewModel(new StrandDTO(14, "A&D", "Arts and Design Track", 4, "Arts and Design")),
                new StrandViewModel(new StrandDTO(15, "TVL-AUTO", "TVL - Automotive Servicing", 2, "Tech-Voc"))
        );
    }

    public void saveStrand() {
        System.out.println(formViewModel.trackCodeProperty().get());
        System.out.println(formViewModel.strandCodeProperty().get());
        System.out.println(formViewModel.descriptionProperty().get());

        formViewModel.trackCodeProperty().set("");
        formViewModel.strandCodeProperty().set("");
        formViewModel.descriptionProperty().set("");
    }
}
