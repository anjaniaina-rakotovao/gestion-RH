package com.hr.management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hr.management.dto.DocEmployeDTO;
import com.hr.management.dto.ListeScoringPeriodeDTO;
import com.hr.management.dto.OstieCnapsSalaireNetDTO;
import com.hr.management.service.DocEmployeService;
import com.hr.management.service.ScoringService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoringController {

     @Autowired
    private ScoringService scoringService;

   @GetMapping("/mois/{mois}/{annee}")
    public ResponseEntity<ListeScoringPeriodeDTO> getScoringMensuel(
    @PathVariable int mois,
    @PathVariable int annee) {
    ListeScoringPeriodeDTO response = scoringService.getScoringPeriodeMonth(mois, annee);
    return ResponseEntity.ok(response);
    }

    @GetMapping("/annee/{annee}")
    public ResponseEntity<ListeScoringPeriodeDTO> getScoringAnnuel(
    @PathVariable int annee) {
    ListeScoringPeriodeDTO response = scoringService.getScoringPeriodeYear(annee);
    return ResponseEntity.ok(response);
    }

    @GetMapping("/mois/annee/{annee}")
    public ResponseEntity<List<ListeScoringPeriodeDTO>> getScoringMonthlyAnnuel(
    @PathVariable int annee) {
    List<ListeScoringPeriodeDTO> response = scoringService.getScoringParMois(annee);
    return ResponseEntity.ok(response);
    }


}
