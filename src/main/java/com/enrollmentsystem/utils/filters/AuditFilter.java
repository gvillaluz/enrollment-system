package com.enrollmentsystem.utils.filters;

import com.enrollmentsystem.enums.AuditAction;
import com.enrollmentsystem.enums.AuditModule;

import java.time.LocalDateTime;

public class AuditFilter {
    private String username;
    private AuditAction action;
    private AuditModule module;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private int offset;

    public AuditFilter(String username, AuditAction action, AuditModule module, LocalDateTime dateFrom, LocalDateTime dateTo, int offset) {
        this.username = username;
        this.action = action;
        this.module = module;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.offset = offset;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public AuditModule getModule() { return module; }
    public void setModule(AuditModule module) { this.module = module; }

    public LocalDateTime getDateFrom() { return dateFrom; }
    public void setDateFrom(LocalDateTime dateFrom) { this.dateFrom = dateFrom; }

    public LocalDateTime getDateTo() { return dateTo; }
    public void setDateTo(LocalDateTime dateTo) { this.dateTo = dateTo; }

    public int getOffset() { return offset; }
    public void setOffset(int offset) { this.offset = offset; }
}
