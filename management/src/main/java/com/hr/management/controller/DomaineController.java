package com.hr.management.controller;

import com.hr.management.entities.Domaine;
import com.hr.management.service.DomaineService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/domaine")
public class DomaineController {

    private final DomaineService domaineService;

    public DomaineController(DomaineService domaineService) {
        this.domaineService = domaineService;
    }

    @GetMapping("/all")
    public List<Domaine> getAllDomaines() {
        return domaineService.findAllDomaine();
    }
}

