package com.hr.management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hr.management.entities.DocumentArchive;
import com.hr.management.service.DocumentArchiveService;

@RestController
@RequestMapping("/api/documents")
public class DocumentArchiveController {

    private final DocumentArchiveService service;

    public DocumentArchiveController(DocumentArchiveService service) {
        this.service = service;
    }

    @PostMapping
    public DocumentArchive upload(@RequestBody DocumentArchive doc) {
        return service.save(doc);
    }

    @GetMapping
    public List<DocumentArchive> getAll() {
        return service.getAll();
    }
}
