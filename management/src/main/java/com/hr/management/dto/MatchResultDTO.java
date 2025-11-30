package com.hr.management.dto;

import java.math.BigDecimal;
import java.util.List;
public class MatchResultDTO {
    private String idPoste;
    private String idEmploye;
    private BigDecimal matchScore; // 0..100
    private List<SkillGapDTO> gaps; // compétences manquantes ou insuffisantes
    // getters / setters, constructor

    public MatchResultDTO() {}
    public MatchResultDTO(String idPoste, String idEmploye, BigDecimal matchScore, List<SkillGapDTO> gaps) {
        this.idPoste = idPoste;
        this.idEmploye = idEmploye;
        this.matchScore = matchScore;
        this.gaps = gaps;
    }
    public String getIdPoste() {
        return idPoste;
    }
    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }
    public String getIdEmploye() {
        return idEmploye;
    }
    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }
    public BigDecimal getMatchScore() {
        return matchScore;
    }
    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }
    public List<SkillGapDTO> getGaps() {
        return gaps;
    }
    public void setGaps(List<SkillGapDTO> gaps) {
        this.gaps = gaps;
    }

}

