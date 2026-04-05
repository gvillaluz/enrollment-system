package com.enrollmentsystem.services;

import com.enrollmentsystem.config.AppConfig;
import com.enrollmentsystem.dtos.UserDTO;
import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;
import com.enrollmentsystem.enums.UserStatus;
import com.enrollmentsystem.mappers.UserMapper;
import com.enrollmentsystem.models.UserSession;
import com.enrollmentsystem.repositories.AuditRepository;
import com.enrollmentsystem.repositories.UserRepository;
import com.enrollmentsystem.utils.PasswordHasher;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService extends BaseService {
    private final UserRepository _userRepo;

    public UserService(UserRepository userRepo, AuditRepository auditRepo) {
        _userRepo = userRepo;
        super(auditRepo);
    }

    public CompletableFuture<Integer> getTotalCount() {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
           var totalRows = _userRepo.countUsers();

           if (totalRows == null) {
               throw new IllegalArgumentException("Failed to get total rows");
           }

           return totalRows;
        });
    }

    public CompletableFuture<List<UserDTO>> getUsers(int offset) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            var dtos = _userRepo.getAllUsers(offset);

            if (dtos == null || dtos.isEmpty()) {
                return null;
            }

            return dtos.stream()
                    .map(UserMapper::toDTO)
                    .toList();
        });
    }

    public CompletableFuture<Boolean> createUser(UserDTO userDTO) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            userDTO.setPassword(PasswordHasher.hash(userDTO.getPassword()));

            Integer genUserId = _userRepo.addUser(UserMapper.toNewModel(userDTO));

            if (genUserId == null)  {
                throw new IllegalArgumentException("Failed to add user");
            }

            logActivity(
                    genUserId.toString(),
                    AuditAction.ADD,
                    AuditModule.USER_MANAGEMENT,
                    "Added user: " + genUserId
            );

            return true;
        });
    }

    public CompletableFuture<Boolean> updateUserInfo(UserDTO userDTO) {
        if (!UserSession.getInstance().isAdmin())
            return CompletableFuture.failedFuture(
                    new SecurityException("Unauthorized Access.")
            );

        return CompletableFuture.supplyAsync(() -> {
            boolean isSaved = _userRepo.updateUser(UserMapper.toModel(userDTO));

            if (isSaved)
                logActivity(
                        String.valueOf(userDTO.getUserId()),
                        AuditAction.UPDATE,
                        AuditModule.USER_MANAGEMENT,
                        "Updated user: " + userDTO.getUserId()
                );

            return isSaved;
        });
    }

    public CompletableFuture<UserStatus> updateUserStatus(int userId, UserStatus status) {
        if (userId <= 0 || status == null)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid User.")
            );

        UserStatus newStatus = status == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE;

        return CompletableFuture.supplyAsync(() -> {
            if (!_userRepo.updateUserStatusById(userId, newStatus)) {
                throw new IllegalArgumentException("Failed to update user status");
            }

            logActivity(
                    String.valueOf(userId),
                    AuditAction.UPDATE,
                    AuditModule.USER_MANAGEMENT,
                    "Deactivated user: " + userId
            );

            return newStatus;
        });
    }

    public CompletableFuture<Boolean> resetPasswordToDefault(int userId) {
        if (userId <= 0)
            return CompletableFuture.failedFuture(
                    new IllegalArgumentException("Invalid user.")
            );

        return CompletableFuture.supplyAsync(() -> {
            String hashedPassword = PasswordHasher.hash(AppConfig.DEFAULT_PASSWORD);

           if (!_userRepo.resetUserPassword(userId, hashedPassword)) {
               return false;
           }

           logActivity(
                   String.valueOf(userId),
                   AuditAction.UPDATE,
                   AuditModule.USER_MANAGEMENT,
                   "Reset Password: " + userId
           );

           return true;
        });
    }
}
