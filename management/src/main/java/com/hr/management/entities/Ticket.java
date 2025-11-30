package com.hr.management.entities;



import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @Column(name = "id_ticket")
    private String idTicket;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    @JsonManagedReference(value = "employe-tickets")
    private Employe employe;

    @Column(name = "titre")
    private String titre;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "duree_estimee")
    private BigDecimal dureeEstimee;

    public Ticket(String idTicket, Employe employe, String titre, String libelle, BigDecimal dureeEstimee) {
        this.idTicket = idTicket;
        this.employe = employe;
        this.titre = titre;
        this.libelle = libelle;
        this.dureeEstimee = dureeEstimee;
    }

    public Ticket() {
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getDureeEstimee() {
        return dureeEstimee;
    }

    public void setDureeEstimee(BigDecimal dureeEstimee) {
        this.dureeEstimee = dureeEstimee;
    }

}
