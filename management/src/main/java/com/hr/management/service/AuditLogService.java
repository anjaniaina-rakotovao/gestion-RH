package com.hr.management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.entities.AuditLog;
import com.hr.management.entities.Utilisateur;
import com.hr.management.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository repo;

    public AuditLogService(AuditLogRepository repo) {
        this.repo = repo;
    }

    public void logAction(String action, String table, String idRecord, Utilisateur user) {
        AuditLog log = new AuditLog();

        log.setIdAudit("AUD-" + System.currentTimeMillis());
        log.setUtilisateur(user);
        log.setAction(action);
        log.setTableAffectee(table);
        log.setIdRecord(idRecord);
        log.setDateAction(LocalDateTime.now());

        repo.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return repo.findAll();
    }
}
