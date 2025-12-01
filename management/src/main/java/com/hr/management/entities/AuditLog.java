package com.hr.management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @Column(name = "id_audit")
    private String idAudit;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;

    private String action;

    private String tableAffectee;

    private String idRecord;

    private LocalDateTime dateAction = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String details;

    // Getters / Setters
    public String getIdAudit() { return idAudit; }
    public void setIdAudit(String idAudit) { this.idAudit = idAudit; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getTableAffectee() { return tableAffectee; }
    public void setTableAffectee(String tableAffectee) { this.tableAffectee = tableAffectee; }

    public String getIdRecord() { return idRecord; }
    public void setIdRecord(String idRecord) { this.idRecord = idRecord; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
