package com.enrollmentsystem.services;

import com.enrollmentsystem.config.AppConfig;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService extends BaseService {
    private final UserRepository _userRepo;

    public UserService(UserRepository userRepo, AuditRepository auditRepo) {
        super(auditRepo);
        _userRepo = userRepo;
    }

    public CompletableFuture<Integer> getTotalCount() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
           try (Connection conn = DatabaseConnection.getConnection()) {
               var totalRows = _userRepo.countUsers(conn);

               if (totalRows == null) {
                   throw new IllegalArgumentException("Failed to get total rows");
               }

               return totalRows;
           } catch (SQLException e) {
               System.out.println(e.getMessage());
               throw new RuntimeException("Failed to get row count.");
           }
        });
    }

    public CompletableFuture<List<UserDTO>> getUsers(int offset) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                return _userRepo.getAllUsers(conn, offset).stream()
                        .map(UserMapper::toDTO)
                        .toList();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to load users.");
            }
        });
    }

    public CompletableFuture<Boolean> createUser(UserDTO userDTO) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                userDTO.setPassword(PasswordHasher.hash(userDTO.getPassword()));

                _userRepo.addUser(conn, UserMapper.toNewModel(userDTO));

                logActivity(
                        conn,
                        userDTO.getUsername(),
                        AuditAction.ADD,
                        AuditModule.USER_MANAGEMENT,
                        "Added user: " + userDTO.getFirstName() + " " + userDTO.getLastName()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to add user.");
            }
        });
    }

    public CompletableFuture<Boolean> updateUserInfo(UserDTO userDTO) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                User existingUser = _userRepo.findUserById(conn, userDTO.getUserId());
                if (existingUser == null) return false;

                existingUser.setFirstName(userDTO.getFirstName());
                existingUser.setLastName(userDTO.getLastName());
                existingUser.setMiddleName(userDTO.getMiddleName());
                existingUser.setRole(userDTO.getRole());
                existingUser.setUsername(userDTO.getUsername());

                int rowsAffected = _userRepo.updateUser(conn, existingUser);
                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        userDTO.getUsername(),
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Updated user: " + userDTO.getFirstName() + " " + userDTO.getLastName()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update user.");
            }
        });
    }

    public CompletableFuture<UserStatus> updateUserStatus(int userId, UserStatus status) {
        if (userId <= 0 || status == null)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid User.")
            );

        UserStatus newStatus = status == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE;

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                User existingUser = _userRepo.findUserById(conn, userId);
                if (existingUser == null) return null;

                int rowsAffected = _userRepo.updateUserStatusById(conn, userId, newStatus);
                if (rowsAffected == 0) return null;

                logActivity(
                        conn,
                        existingUser.getUsername(),
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Deactivated user: " + existingUser.getFirstName() + " " + existingUser.getLastName()
                );

                return newStatus;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to update user status.");
            }
        });
    }

    public CompletableFuture<Boolean> resetPasswordToDefault(int userId) {
        if (userId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid user.")
            );

        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                User existingUser = _userRepo.findUserById(conn, userId);

                String hashedPassword = PasswordHasher.hash(AppConfig.DEFAULT_PASSWORD);

                int rowsAffected = _userRepo.resetUserPassword(conn, userId, hashedPassword);
                if (rowsAffected == 0) return false;

                logActivity(
                        conn,
                        existingUser.getUsername(),
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Reset Password for user: " + existingUser.getFirstName() + " " + existingUser.getLastName()
                );

                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException("Failed to reset user password.");
            }
        });
    }
}
