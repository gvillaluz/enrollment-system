package com.enrollmentsystem.dtos;

import com.enrollmentsystem.enums.Role;
import com.enrollmentsystem.enums.UserStatus;

public class UserDTO {
    private int userId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String username;
    private String password;
    private Role role;
    private UserStatus status;

    public UserDTO() {}

    public UserDTO(int userId, String lastName, String firstName, String middleName, String username, String password, Role role, UserStatus status) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
