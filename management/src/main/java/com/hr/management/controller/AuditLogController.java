package com.hr.management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hr.management.entities.AuditLog;
import com.hr.management.service.AuditLogService;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLog> getAll() {
        return service.getAllLogs();
    }
}
