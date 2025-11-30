package com.hr.management.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hr.management.entities.RapportPerformance;
import com.hr.management.service.RapportPerformanceService;

@RestController
@RequestMapping("/api/rapport")
public class RapportPerformanceController {

    private final RapportPerformanceService service;

    public RapportPerformanceController(RapportPerformanceService service) {
        this.service = service;
    }

    @PostMapping("/generer")
    public RapportPerformance generer(
        @RequestParam String idEmploye,
        @RequestParam int month,
        @RequestParam int year,
        @RequestParam(required = false) String commentaire
    ) {
        return service.genererRapport(idEmploye, month, year, commentaire);
    }
}

