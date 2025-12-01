package com.hr.management.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.hr.management.entities.DocumentArchive;
import com.hr.management.repository.DocumentArchiveRepository;

@Service
public class DocumentArchiveService {

    private final DocumentArchiveRepository repo;

    public DocumentArchiveService(DocumentArchiveRepository repo) {
        this.repo = repo;
    }

    public DocumentArchive save(DocumentArchive doc) {
        doc.setIdDocArchive("DOC-" + System.currentTimeMillis());
        return repo.save(doc);
    }

    public List<DocumentArchive> getAll() {
        return repo.findAll();
    }
}
