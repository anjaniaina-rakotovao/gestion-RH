package com.hr.management.entities;

import java.time.LocalDate; 

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "employe_competence")
public class EmployeCompetence {

    @EmbeddedId
    private EmployeCompetenceKey id;

    @ManyToOne
    @MapsId("idEmploye")
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @MapsId("idCompetence")
    @JoinColumn(name = "id_competence")
    private Competence competence;

    private Integer niveauObtenu;
    private LocalDate dateAttribution;
    public EmployeCompetence(EmployeCompetenceKey id, Employe employe, Competence competence, Integer niveauObtenu,
            LocalDate dateAttribution) {
        this.id = id;
        this.employe = employe;
        this.competence = competence;
        this.niveauObtenu = niveauObtenu;
        this.dateAttribution = dateAttribution;
    }
    public EmployeCompetence() {
    }
    // getters / setters
    public EmployeCompetenceKey getId() {
        return id;
    }
    public void setId(EmployeCompetenceKey id) {
        this.id = id;
    }
    public Employe getEmploye() {
        return employe;
    }
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    public Competence getCompetence() {
        return competence;
    }
    public void setCompetence(Competence competence) {
        this.competence = competence;
    }
    public Integer getNiveauObtenu() {
        return niveauObtenu;
    }
    public void setNiveauObtenu(Integer niveauObtenu) {
        this.niveauObtenu = niveauObtenu;
    }
    public LocalDate getDateAttribution() {
        return dateAttribution;
    }
    public void setDateAttribution(LocalDate dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
}


