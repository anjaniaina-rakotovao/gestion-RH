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

    public long getScore(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScore'");
    }


}
