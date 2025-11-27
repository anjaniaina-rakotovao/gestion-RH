package com.hr.management.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ostie")
public class Ostie {

    @Id
    @Column(name = "id_ostie")
    private int idOstie;

    private double valeur;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String status;

    public Ostie() {}

    public Ostie(int idOstie, double valeur, Date date, String status) {
        this.idOstie = idOstie;
        this.valeur = valeur;
        this.date = date;
        this.status = status;
    }

    public int getIdOstie() { return idOstie; }
    public void setIdOstie(int idOstie) { this.idOstie = idOstie; }

    public double getValeur() { return valeur; }
    public void setValeur(double valeur) { this.valeur = valeur; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
