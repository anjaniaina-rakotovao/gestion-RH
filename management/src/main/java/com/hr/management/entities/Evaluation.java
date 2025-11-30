package com.hr.management.entities;
import java.math.BigDecimal;
import java.time.LocalDate;
   
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluation;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    private String periode;
    private BigDecimal productivite;
    private BigDecimal efficacite;
    private BigDecimal qualite;
    private BigDecimal scoreGlobal;
    private LocalDate dateEvaluation;
    public Evaluation(Integer idEvaluation, Employe employe, String periode, BigDecimal productivite,
            BigDecimal efficacite, BigDecimal qualite, BigDecimal scoreGlobal, LocalDate dateEvaluation) {
        this.idEvaluation = idEvaluation;
        this.employe = employe;
        this.periode = periode;
        this.productivite = productivite;
        this.efficacite = efficacite;
        this.qualite = qualite;
        this.scoreGlobal = scoreGlobal;
        this.dateEvaluation = dateEvaluation;
    }
    public Evaluation() {
    }
    public Integer getIdEvaluation() {
        return idEvaluation;
    }
    public void setIdEvaluation(Integer idEvaluation) {
        this.idEvaluation = idEvaluation;
    }
    public Employe getEmploye() {
        return employe;
    }
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    public String getPeriode() {
        return periode;
    }
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    public BigDecimal getProductivite() {
        return productivite;
    }
    public void setProductivite(BigDecimal productivite) {
        this.productivite = productivite;
    }
    public BigDecimal getEfficacite() {
        return efficacite;
    }
    public void setEfficacite(BigDecimal efficacite) {
        this.efficacite = efficacite;
    }
    public BigDecimal getQualite() {
        return qualite;
    }
    public void setQualite(BigDecimal qualite) {
        this.qualite = qualite;
    }
    public BigDecimal getScoreGlobal() {
        return scoreGlobal;
    }
    public void setScoreGlobal(BigDecimal scoreGlobal) {
        this.scoreGlobal = scoreGlobal;
    }
    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }
    public void setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }
}


