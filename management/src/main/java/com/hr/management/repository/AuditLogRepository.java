package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

}
