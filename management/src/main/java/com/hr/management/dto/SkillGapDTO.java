package com.hr.management.dto;

public class SkillGapDTO {
    public SkillGapDTO(Integer idCompetence, String competenceLibelle, Integer niveauAttendu, Integer niveauActuel) {
        this.idCompetence = idCompetence;
        this.competenceLibelle = competenceLibelle;
        this.niveauAttendu = niveauAttendu;
        this.niveauActuel = niveauActuel;
    }
    public SkillGapDTO() {
    }
    private Integer idCompetence;
    private String competenceLibelle;
    private Integer niveauAttendu;
    private Integer niveauActuel; // null si absent
    public Integer getIdCompetence() {
        return idCompetence;
    }
    public void setIdCompetence(Integer idCompetence) {
        this.idCompetence = idCompetence;
    }
    public String getCompetenceLibelle() {
        return competenceLibelle;
    }
    public void setCompetenceLibelle(String competenceLibelle) {
        this.competenceLibelle = competenceLibelle;
    }
    public Integer getNiveauAttendu() {
        return niveauAttendu;
    }
    public void setNiveauAttendu(Integer niveauAttendu) {
        this.niveauAttendu = niveauAttendu;
    }
    public Integer getNiveauActuel() {
        return niveauActuel;
    }
    public void setNiveauActuel(Integer niveauActuel) {
        this.niveauActuel = niveauActuel;
    }
}

