package com.enrollmentsystem.viewmodels.enrollment.student;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.dtos.StudentRecordDTO;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.enums.Gender;
import com.enrollmentsystem.enums.Semester;
import com.enrollmentsystem.services.SectionService;
import com.enrollmentsystem.services.StrandService;
import com.enrollmentsystem.services.StudentService;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.filters.StudentFilter;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StudentRecordViewModel {
    private final StringProperty lrn = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final ObjectProperty<Gender> gender = new SimpleObjectProperty<>();
    private final IntegerProperty gradeLevel = new SimpleIntegerProperty();
    private final ObjectProperty<Semester> semester = new SimpleObjectProperty<>();
    private final IntegerProperty trackId = new SimpleIntegerProperty();
    private final StringProperty trackCode = new SimpleStringProperty();
    private final IntegerProperty strandId = new SimpleIntegerProperty();
    private final StringProperty strandCode = new SimpleStringProperty();
    private final IntegerProperty sectionId = new SimpleIntegerProperty();
    private final StringProperty sectionName = new SimpleStringProperty();

    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);

    public StudentRecordViewModel() { initializeDropDowns(); }

    private final ObservableList<TrackDTO> tracks = FXCollections.observableArrayList();
    private final ObservableList<StrandDTO> strands = FXCollections.observableArrayList();
    private final ObservableList<SectionDTO> sections = FXCollections.observableArrayList();

    private final ObservableList<RecordViewModel> records = FXCollections.observableArrayList();

    private final StudentService _service = AppContext.getStudentService();
    private final TrackService _trackService = AppContext.getTrackService();
    private final StrandService _strandService = AppContext.getStrandService();
    private final SectionService _sectionService = AppContext.getSectionService();

    private final int PAGE_LIMIT = 16;

    public StringProperty lrnProperty() { return lrn; }
    public StringProperty lastNameProperty() { return lastName; }
    public StringProperty firstNameProperty() { return firstName; }
    public ObjectProperty<Gender> genderProperty() { return gender; }
    public IntegerProperty gradeLevelProperty() { return gradeLevel; }
    public ObjectProperty<Semester> semesterProperty() { return semester; }
    public IntegerProperty trackIdProperty() { return trackId; }
    public StringProperty trackCodeProperty() { return trackCode; }
    public IntegerProperty strandIdProperty() { return strandId; }
    public StringProperty strandCodeProperty() { return strandCode; }
    public IntegerProperty sectionIdProperty() { return sectionId; }
    public StringProperty sectionNameProperty() { return sectionName; }

    public BooleanProperty loadingProperty() { return isLoading; }
    public IntegerProperty totalPagesProperty() { return totalPages; }

    public ObservableList<RecordViewModel> getRecords() { return records; }
    public ObservableList<TrackDTO> getTrackList() { return tracks; }
    public ObservableList<StrandDTO> getStrandList() { return strands; }
    public ObservableList<SectionDTO> getSectionList() { return sections; }

    public void initializeDropDowns() {
        _trackService.loadTracks().thenAccept(list -> Platform.runLater(() -> tracks.setAll(list)));
        _strandService.loadStrands().thenAccept(list -> Platform.runLater(() -> strands.setAll(list)));
        _sectionService.getAllSections().thenAccept(list -> Platform.runLater(() -> sections.setAll(list)));
    }

    public void loadStudentRecords(int pageIndex) {
        isLoading.set(true);

        int offset = pageIndex * PAGE_LIMIT;

        var filter = new StudentFilter(
            lrn.get(),
            lastName.get(),
            firstName.get(),
            gender.get(),
            gradeLevel.get(),
            semester.get(),
            trackId.get(),
            strandId.get(),
            sectionId.get(),
            offset
        );

        System.out.println("--- Student Filter Debug ---");
        System.out.println("LRN: " + filter.getLrn());
        System.out.println("Name: " + filter.getFirstName() + " " + filter.getLastName());
        System.out.println("Gender: " + filter.getGender());
        System.out.println("Grade: " + filter.getGradeLevel());
        System.out.println("Term: " + filter.getSemester());
        System.out.println("Track ID: " + filter.getTrackId());
        System.out.println("Strand ID: " + filter.getStrandId());
        System.out.println("Section ID: " + filter.getSectionId());
        System.out.println("Offset: " + filter.getOffset());
        System.out.println("---------------------------");

        CompletableFuture<Integer> countTask = _service.getRecordCount(filter);
        CompletableFuture<List<StudentRecordDTO>> listTask = _service.getLatest20Records(filter);

        CompletableFuture.allOf(countTask, listTask).thenAccept(v -> {
            Integer totalRows = countTask.join();
            List<StudentRecordDTO> records = listTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));

                this.records.setAll(records.stream()
                        .map(RecordViewModel::new)
                        .toList());

                isLoading.set(false);
            });
        })
        .exceptionally(ex -> {
            Platform.runLater(() -> isLoading.set(false));
            System.out.println(ex.getMessage());
            return null;
        });
    }

    public void clearFields() {
        lrn.set(null);
        lastName.set(null);
        firstName.set(null);
        gender.set(null);
        gradeLevel.set(0);
        semester.set(null);
        trackId.set(0);
        trackCode.set(null);
        strandId.set(0);
        strandCode.set(null);
        sectionId.set(0);
        sectionName.set(null);
    }
}
