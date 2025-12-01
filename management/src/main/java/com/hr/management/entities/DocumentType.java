package com.hr.management.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "document_type")
public class DocumentType {
    @Id
    @Column(name = "id_doc_type")
    private String idDocType;

    private String libelle;

    private Boolean obligatoire = true;

    @OneToMany(mappedBy = "documentType")
    private List<DocumentArchive> archives;

    // Getters / Setters
    public String getIdDocType() { return idDocType; }
    public void setIdDocType(String idDocType) { this.idDocType = idDocType; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Boolean getObligatoire() { return obligatoire; }
    public void setObligatoire(Boolean obligatoire) { this.obligatoire = obligatoire; }

    public List<DocumentArchive> getArchives() { return archives; }
    public void setArchives(List<DocumentArchive> archives) { this.archives = archives; }
}
