package com.enrollmentsystem.viewmodels.core;

import com.enrollmentsystem.config.AppContext;
import com.enrollmentsystem.services.AuthService;

import java.util.concurrent.CompletableFuture;

public class MainViewModel {
    private final AuthService _service = AppContext.getAuthService();

    public CompletableFuture<Void> logoutUser() {
        return _service.logoutUser();
    }
}
