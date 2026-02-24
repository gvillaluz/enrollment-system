package com.enrollmentsystem.viewmodels.admin;

import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserManagementViewModel {
    private final UserViewModel formInstance = new UserViewModel(new UserDTO());

    private final ObservableList<UserViewModel> userList = FXCollections.observableArrayList();

    public UserViewModel getFormInstance() { return formInstance; }

    public ObservableList<UserViewModel> getUserList() { return userList; }

    public void loadUsers() {
        userList.addAll(
                new UserViewModel(new UserDTO(1, "Gabriel", "Kian", "L.", "kgabriel", "pass123", Role.ADMIN)),
                new UserViewModel(new UserDTO(2, "Santos", "Maria", "D.", "msantos", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(3, "Cruz", "Juan", "P.", "jcruz", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(4, "Reyes", "Elena", "S.", "ereyes", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(5, "Bautista", "Ricardo", "M.", "rbautista", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(6, "Dela Cruz", "Ana", "G.", "adelacruz", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(7, "Villanueva", "Mark", "T.", "mvillanueva", "pass123", Role.ADMIN)),
                new UserViewModel(new UserDTO(8, "Aquino", "Liza", "F.", "laquino", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(9, "Mercado", "Paolo", "B.", "pmercado", "pass123", Role.ENROLLMENT_STAFF)),
                new UserViewModel(new UserDTO(10, "Pascual", "Sonia", "C.", "spascual", "pass123", Role.ENROLLMENT_STAFF))
        );
    }
}
