package com.hr.management.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "irsa")
public class Irsa {

    @Id
    @Column(name = "id_irsa")
    private int idIrsa;

    @Column(name = "montant_min")
    private double montantMin;

    @Column(name = "montant_max")
    private Double montantMax; // Peut être NULL

    private double taux;

    @Temporal(TemporalType.DATE)
    private Date date;

    public Irsa() {}

    public Irsa(int idIrsa, double montantMin, Double montantMax, double taux, Date date) {
        this.idIrsa = idIrsa;
        this.montantMin = montantMin;
        this.montantMax = montantMax;
        this.taux = taux;
        this.date = date;
    }

    public int getIdIrsa() { return idIrsa; }
    public void setIdIrsa(int idIrsa) { this.idIrsa = idIrsa; }

    public double getMontantMin() { return montantMin; }
    public void setMontantMin(double montantMin) { this.montantMin = montantMin; }

    public Double getMontantMax() { return montantMax; }
    public void setMontantMax(Double montantMax) { this.montantMax = montantMax; }

    public double getTaux() { return taux; }
    public void setTaux(double taux) { this.taux = taux; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
