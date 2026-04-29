package com.enrollmentsystem.services;

import com.enrollmentsystem.dtos.LoginDTO;
import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.mappers.UserMapper;
import com.enrollmentsystem.models.User;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.UserRepository;
import com.enrollmentsystem.utils.DatabaseConnection;
import com.enrollmentsystem.utils.PasswordHasher;
import com.enrollmentsystem.utils.ValidationHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AuthService extends BaseService {
    private final UserRepository _repo;

    public AuthService(UserRepository repo, AuditRepository auditRepo) {
        super(auditRepo);
        _repo = repo;
    }

    public CompletableFuture<UserDTO> loginUser(LoginDTO loginUser) {
        if (ValidationHelper.isNullOrEmpty(loginUser.getUsername()) || ValidationHelper.isNullOrEmpty(loginUser.getPassword())) {
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("All fields are required.")
            );
        }

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                var user = _repo.findByUsername(conn, loginUser.getUsername());

                if (user == null) {
                    throw new IllegalArgumentException("Invalid username or password.");
                }

                if (user.getStatus() == UserStatus.INACTIVE) {
                    throw new IllegalArgumentException("Account Inactive: Your account has been deactivated.");
                }

                if (!PasswordHasher.compare(loginUser.getPassword(), user.getPassword())) {
                    throw new IllegalArgumentException("Invalid username or password.");
                }

                logActivity(
                        conn,
                        user.getId(),
                        user.getUsername(),
                        AuditAction.LOGIN,
                        AuditModule.AUTH_MANAGEMENT,
                        "User login: " + user.getFirstName() + " " + user.getLastName()
                );

                return UserMapper.toDTO(user);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to log user.");
            }
        });
    }

    public CompletableFuture<Void> logoutUser() {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                logActivity(
                        String.valueOf(UserSession.getInstance().getUser().getUserId()),
                        AuditAction.LOGOUT,
                        AuditModule.AUTH_MANAGEMENT,
                        "User logout: " + UserSession.getInstance().getUser().getUserId()
                );
                return null;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to logout user.");
            }
        });
    }

    public CompletableFuture<Boolean> updateDefaultPassword(String password) {
        if (password.length() < 8)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid password.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String hashedPassword = PasswordHasher.hash(password);
                int userId = UserSession.getInstance().getUser().getUserId();
                String username = UserSession.getInstance().getUser().getUsername();
                String fullName = UserSession.getInstance().getUser().getFirstName() + " " + UserSession.getInstance().getUser().getLastName();

                int rowsAffected = _repo.updateUserPassword(conn, hashedPassword, userId);

                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        username,
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Updated default password: " + fullName
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to set password into default.");
            }
        });
    }
}
