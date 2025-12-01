package com.hr.management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "document_archive")
public class DocumentArchive {
    @Id
    @Column(name = "id_doc_archive")
    private String idDocArchive;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_doc_type")
    private DocumentType documentType;

    private String cheminFichier;

    private LocalDateTime dateSauvegarde = LocalDateTime.now();

    private LocalDate dateExpiration;

    private Integer version = 1;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    private Boolean sauvegardeLegale = true;

    // Getters / Setters
    public String getIdDocArchive() { return idDocArchive; }
    public void setIdDocArchive(String idDocArchive) { this.idDocArchive = idDocArchive; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateSauvegarde() { return dateSauvegarde; }
    public void setDateSauvegarde(LocalDateTime dateSauvegarde) { this.dateSauvegarde = dateSauvegarde; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Boolean getSauvegardeLegale() { return sauvegardeLegale; }
    public void setSauvegardeLegale(Boolean sauvegardeLegale) { this.sauvegardeLegale = sauvegardeLegale; }
}
