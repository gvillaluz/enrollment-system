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
import com.enrollmentsystem.utils.PasswordHasher;
import com.enrollmentsystem.utils.ValidationHelper;

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
            throw new RuntimeException("All fields are required.");
        }

        return CompletableFuture.supplyAsync(() -> {
            var user = _repo.findByUsername(loginUser.getUsername());

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
                    user.getId(),
                    String.valueOf(user.getId()),
                    AuditAction.LOGIN,
                    AuditModule.AUTH_MANAGEMENT,
                    "User login: " + user.getId()
            );

            return UserMapper.toDTO(user);
        });
    }

    public CompletableFuture<Void> logoutUser() {
        return CompletableFuture.supplyAsync(() -> {
            logActivity(
                    String.valueOf(UserSession.getInstance().getUser().getUserId()),
                    AuditAction.LOGOUT,
                    AuditModule.AUTH_MANAGEMENT,
                    "User logout: " + UserSession.getInstance().getUser().getUserId()
            );
            return null;
        });
    }

    public CompletableFuture<Boolean> updateDefaultPassword(String password) {
        if (password.length() < 8)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid password.")
            );

        return CompletableFuture.supplyAsync(() -> {
            String hashedPassword = PasswordHasher.hash(password);
            int userId = UserSession.getInstance().getUser().getUserId();

            boolean isSaved = _repo.updateUserPassword(hashedPassword, userId);

            if (isSaved) {
                logActivity(
                        String.valueOf(userId),
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Updated default password: " + userId
                );
            }

            return isSaved;
        });
    }
}
