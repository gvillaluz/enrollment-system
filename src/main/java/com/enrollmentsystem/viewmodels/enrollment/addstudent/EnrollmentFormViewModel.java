package com.enrollmentsystem.viewmodels.enrollment.addstudent;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.EnrollmentFormDTO;
import com.enrollmentsystem.dtos.SectionDTO;
import com.enrollmentsystem.dtos.StrandDTO;
import com.enrollmentsystem.dtos.TrackDTO;
import com.enrollmentsystem.mappers.AcademicMapper;
import com.enrollmentsystem.mappers.RequirementMapper;
import com.enrollmentsystem.mappers.StudentMapper;
import com.enrollmentsystem.services.EnrollmentService;
import com.enrollmentsystem.services.SectionService;
import com.enrollmentsystem.services.StrandService;
import com.enrollmentsystem.services.TrackService;
import com.enrollmentsystem.utils.ValidationHelper;
import com.enrollmentsystem.viewmodels.enrollment.requirements.StudentRequirementViewModel;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EnrollmentFormViewModel {
    private final IntegerProperty enrollmentId = new SimpleIntegerProperty();
    private final LearnerInformationViewModel learnerInformationVM = new LearnerInformationViewModel();
    private final AcademicInformationViewModel academicInformationVM = new AcademicInformationViewModel();

    private final TrackService _trackService = AppContext.getTrackService();
    private final StrandService _strandService = AppContext.getStrandService();
    private final SectionService _sectionService = AppContext.getSectionService();

    private final Map<Integer, StudentRequirementViewModel> requirementMap = new HashMap<>();
    private final Map<Integer, List<SectionDTO>> strandSectionMap = new HashMap<>();
    private final Map<Integer, List<StrandDTO>> trackStrandMap = new HashMap<>();
    private final ObservableList<TrackDTO> trackList = FXCollections.observableArrayList();

    public int gradeLevel;
    public boolean isEditing;
    private Runnable onSaveSuccess;
    private final EnrollmentService _service = AppContext.getEnrollmentService();

    public EnrollmentFormViewModel() {
        initializeRequirements();
    }

    public LearnerInformationViewModel getLearnerInformationVM() { return learnerInformationVM; }
    public AcademicInformationViewModel getAcademicInformationVM() { return academicInformationVM; }
    public Map<Integer, StudentRequirementViewModel> getRequirementMap() { return requirementMap; }

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }
    public void setGradeLevel(int gradeLevel) { this.gradeLevel = gradeLevel; }

    public void clearRequirements() {
        requirementMap.values().forEach(vm -> {
            vm.submittedProperty().set(false);
        });
    }

    public void initializeRequirements() {
        requirementMap.clear();

        List<Integer> ids = List.of(1, 2, 3, 4, 5, 6, 7);

        for (Integer id : ids) {
            var vm = new StudentRequirementViewModel();
            vm.requirementRefIdProperty().set(id);
            vm.submittedProperty().set(false);
            requirementMap.put(id, vm);
        }
    }

    public void loadTracksAndStrands() {
        CompletableFuture<List<TrackDTO>> trackFuture = _trackService.loadTracks();
        CompletableFuture<List<StrandDTO>> strandFuture = _strandService.loadStrands();
        CompletableFuture<List<SectionDTO>> sectionFuture = _sectionService.getAllSections();

        CompletableFuture.allOf(strandFuture, sectionFuture, trackFuture)
            .thenAccept(v -> {
                List<StrandDTO> strands = strandFuture.join();
                List<SectionDTO> sections = sectionFuture.join();
                List<TrackDTO> tracks = trackFuture.join();

                Platform.runLater(() -> {
                    trackStrandMap.clear();
                    for (StrandDTO s : strands) {
                        trackStrandMap.computeIfAbsent(s.getTrackId(), k -> new ArrayList<>()).add(s);
                    }

                    strandSectionMap.clear();
                    for (SectionDTO s : sections) {
                        strandSectionMap.computeIfAbsent(s.getStrandId(), k -> new ArrayList<>()).add(s);
                    }

                    trackList.addAll(tracks);
                });
            });
    }

    public ObservableList<TrackDTO> getTracks() {
        return trackList;
    }

    public List<StrandDTO> getStrandsByTrackId(int trackId) {
        return trackStrandMap.getOrDefault(trackId, new ArrayList<>());
    }

    public List<SectionDTO> getSectionsByStrandId(int strandId) {
        List<SectionDTO> allSectionsInStrand = strandSectionMap.getOrDefault(strandId, new ArrayList<>());

        String gradePrefix = String.valueOf(this.gradeLevel);

        return allSectionsInStrand.stream()
                .filter(section -> section.getSectionName() != null &&
                        section.getSectionName().contains(gradePrefix))
                .toList();
    }

    public void setEditData(EnrollmentFormDTO dto) {
        if (dto != null) {
            learnerInformationVM.updateFromDTO(dto);
            academicInformationVM.updateFromDTO(dto);
            enrollmentId.set(dto.getEnrollmentId());

            requirementMap.values().forEach(vm -> vm.submittedProperty().set(false));

            if (dto.getRequirementDTOS() != null) {
                for (var reqDto : dto.getRequirementDTOS()) {
                    var existingVM = requirementMap.get(reqDto.getRequirementRefId());
                    if (existingVM != null) {
                        existingVM.submittedProperty().set(reqDto.isSubmitted());
                    }
                }
            }

            isEditing = true;
        } else {
            requirementMap.values().forEach(vm -> vm.submittedProperty().set(false));
            isEditing = false;
        }
    }

    public void getStudentInfoByLRN(String lrn) {
        if (lrn == null || lrn.length() != 12) {
            return;
        }

        _service.loadEnrollmentDataByLRN(lrn)
                .thenAccept(dto -> {
                    Platform.runLater(() -> {
                        if (dto != null) {
                            setEditData(dto);
                        }
                    });
                });
    }

    public CompletableFuture<Boolean> saveEnrollment() {
        if (!ValidationHelper.isValid(learnerInformationVM, academicInformationVM))
            return CompletableFuture.failedFuture(
                new IllegalArgumentException("Mandatory fields must not be empty")
            );

        String lrn = learnerInformationVM.lrnProperty().get();
        requirementMap.values().forEach(vm -> vm.studentLrnProperty().set(lrn));

        var enrollFormDTO = new EnrollmentFormDTO();
        StudentMapper.mapToFormDTO(learnerInformationVM, enrollFormDTO);
        AcademicMapper.mapToFormDTO(academicInformationVM, enrollFormDTO);
        enrollFormDTO.setRequirementDTOS(RequirementMapper.toDTOList(requirementMap));
        enrollFormDTO.setGradeLevel(gradeLevel);

        if (!isEditing) {
            return _service.processEnrollment(enrollFormDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    });
        } else {
            enrollFormDTO.setEnrollmentId(enrollmentId.get());
            return _service.editEnrollment(enrollFormDTO)
                    .thenApply(success -> {
                        if (success && onSaveSuccess != null) {
                            onSaveSuccess.run();
                        }
                        return success;
                    })
;        }
    }
}
