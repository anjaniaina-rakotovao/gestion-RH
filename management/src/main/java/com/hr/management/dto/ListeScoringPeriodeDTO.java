package com.hr.management.dto;
import java.util.List;


public class ListeScoringPeriodeDTO {
    private String periode;
    private List<ScoringTicketDTO> listeScoringPeriode;

    public ListeScoringPeriodeDTO(){

    }

    public ListeScoringPeriodeDTO(String periode, List<ScoringTicketDTO> listeScoringPeriode){
        this.periode = periode;
        this.listeScoringPeriode = listeScoringPeriode;
    }

    public String getPeriode(){
        return periode;
    }

    public List<ScoringTicketDTO> getListeScoringPeriode(){
        return this.listeScoringPeriode;
    } 

    public void setPeriode(String periode){
        this.periode = periode;
    }

    public void setListeScoringPeriode(List<ScoringTicketDTO> listeScoringPeriode){
        this.listeScoringPeriode = listeScoringPeriode;
    }
public long getScore(String nom) {
    if (listeScoringPeriode == null) {
        return 0L;
    }

    return listeScoringPeriode.stream()
            .filter(s -> s.getNom().equalsIgnoreCase(nom)) // on filtre sur le nom
            .mapToLong(s -> s.getScore() != null ? s.getScore().longValue() : 0L) // conversion BigDecimal -> long
            .sum();
}



}
