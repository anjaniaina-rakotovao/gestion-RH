package com.hr.management.dto;

import java.util.List;
public class FormationSuggestionDTO {
    public FormationSuggestionDTO(Integer idFormation, String titre, String description,
            List<Integer> competencesCouvrant) {
        this.idFormation = idFormation;
        this.titre = titre;
        this.description = description;
        this.competencesCouvrant = competencesCouvrant;
    }
    public FormationSuggestionDTO() {
    }
    private Integer idFormation;
    private String titre;
    private String description;
    private List<Integer> competencesCouvrant; // ids compétences
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
    public List<Integer> getCompetencesCouvrant() {
        return competencesCouvrant;
    }
    public void setCompetencesCouvrant(List<Integer> competencesCouvrant) {
        this.competencesCouvrant = competencesCouvrant;
    }
}

