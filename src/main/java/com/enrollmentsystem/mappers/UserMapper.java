package com.enrollmentsystem.mappers;

import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.viewmodels.admin.UserFormViewModel;

public class UserMapper {
    public static User toNewModel(UserDTO dto) {
        var user = new User();
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    public static User toModel(UserDTO dto) {
        var user = new User();
        user.setId(dto.getUserId());
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());

        return user;
    }

    public static UserDTO toDTO(User user) {
        var dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setMiddleName(user.getMiddleName());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());

        return dto;
    }

    public static UserDTO formToDTO(UserFormViewModel form) {
        var dto = new UserDTO();
        dto.setUserId(form.userIdProperty().get());
        dto.setLastName(form.lastNameProperty().get());
        dto.setFirstName(form.firstNameProperty().get());
        dto.setMiddleName(form.middleNameProperty().get());
        dto.setUsername(form.usernameProperty().get());
        dto.setPassword(form.passwordProperty().get());
        dto.setRole(form.roleProperty().get());

        return dto;
    }
}
