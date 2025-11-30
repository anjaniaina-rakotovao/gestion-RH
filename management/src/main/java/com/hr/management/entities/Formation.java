package com.hr.management.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "formation")
public class Formation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_formation")
    private Integer idFormation;
    private String titre;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer dureeHeures;
    public Formation(Integer idFormation, String titre, String description, Integer dureeHeures) {
        this.idFormation = idFormation;
        this.titre = titre;
        this.description = description;
        this.dureeHeures = dureeHeures;
    }
    public Formation() {
    }
    // getters / setters
    public Integer getIdFormation() {
        return idFormation;
    }
    public void setIdFormation(Integer idFormation) {
        this.idFormation = idFormation;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getDureeHeures() {
        return dureeHeures;
    }
    public void setDureeHeures(Integer dureeHeures) {
        this.dureeHeures = dureeHeures;
    }
}

