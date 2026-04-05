package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.services.UserService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserManagementViewModel {
    private final IntegerProperty totalPages = new SimpleIntegerProperty(1);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(true);

    private final int PAGE_LIMIT = 15;

    private final UserService _service = AppContext.getUserService();
    private final ObservableList<UserViewModel> userList = FXCollections.observableArrayList();

    public IntegerProperty totalPagesProperty() { return totalPages; }
    public BooleanProperty loadingProperty() { return isLoading; }

    public ObservableList<UserViewModel> getUserList() { return userList; }

    public void loadUsers(int pageIndex) {
        userList.clear();
        isLoading.set(true);

        int offset = pageIndex * PAGE_LIMIT;

        CompletableFuture<List<UserDTO>> listTask = _service.getUsers(offset);
        CompletableFuture<Integer> countTask = _service.getTotalCount();

        CompletableFuture.allOf(listTask, countTask).thenAccept(v -> {
            List<UserDTO> userDTOS = listTask.join();
            Integer totalRows = countTask.join();

            Platform.runLater(() -> {
                int pages = (int) Math.ceil((double) totalRows / PAGE_LIMIT);
                totalPages.set(Math.max(pages, 1));

                userList.setAll(
                        userDTOS.stream().map(UserViewModel::new).toList()
                );

                isLoading.set(false);
            });
        })
        .exceptionally(ex -> {
            Platform.runLater(() -> isLoading.set(false));
            return null;
        });
    }

    public CompletableFuture<Boolean> updateUserStatus(int userId, UserStatus status) {
        return _service.updateUserStatus(userId, status)
                .thenApply(newStatus -> {
                    if (newStatus != null) {
                        Platform.runLater(() -> {
                            userList.stream()
                                    .filter(u -> u.userIdProperty().get() == userId)
                                    .findFirst()
                                    .ifPresent(u -> u.statusProperty().set(newStatus));
                        });
                    }
                    return true;
                });
    }

    public CompletableFuture<Boolean> resetPassword(int userId) {
        if (userId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid user.")
            );

        return _service.resetPasswordToDefault(userId);
    }
}
