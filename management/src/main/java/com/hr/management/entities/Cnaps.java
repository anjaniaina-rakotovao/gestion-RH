package com.hr.management.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "cnaps")
public class Cnaps {

    @Id
    @Column(name = "id_cnaps")
    private int idCnaps;

    private double valeur;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String status;

    public Cnaps() {}

    public Cnaps(int idCnaps, double valeur, Date date, String status) {
        this.idCnaps = idCnaps;
        this.valeur = valeur;
        this.date = date;
        this.status = status;
    }

    public int getIdCnaps() { return idCnaps; }
    public void setIdCnaps(int idCnaps) { this.idCnaps = idCnaps; }

    public double getValeur() { return valeur; }
    public void setValeur(double valeur) { this.valeur = valeur; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
