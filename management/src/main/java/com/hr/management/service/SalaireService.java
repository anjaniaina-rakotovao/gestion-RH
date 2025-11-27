package com.hr.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.management.dto.IrsaResponseDTO;
import com.hr.management.dto.IrsaTrancheDTO;
import com.hr.management.dto.SalaireDTO;
import com.hr.management.entities.Cnaps;
import com.hr.management.entities.HistoMouvement;
import com.hr.management.entities.Irsa;
import com.hr.management.entities.Ostie;
import com.hr.management.repository.CnapsRepository;
import com.hr.management.repository.HistoMouvementRepository;
import com.hr.management.repository.IrsaRepository;
import com.hr.management.repository.OstieRepository;
import com.hr.management.repository.SalaireRepository;

@Service
public class SalaireService {

    @Autowired
    private SalaireRepository salaireRepository;

    @Autowired
    private HistoMouvementRepository histoMouvementRepository;

    @Autowired
    private CnapsRepository cnapsRepository;

    @Autowired
    private IrsaRepository irsaRepository;

    @Autowired
    private OstieRepository ostieRepository;

    public BigDecimal getCnaps(int mois, int annee, String status) {
        LocalDate d = LocalDate.of(annee, mois, 1);

        Cnaps cnaps = cnapsRepository.findTopByStatusAndDateLessThanEqualOrderByDateDesc(
                status,
                java.sql.Date.valueOf(d));

        if (cnaps == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(cnaps.getValeur());
    }

    public BigDecimal getOstie(int mois, int annee, String status) {
        LocalDate d = LocalDate.of(annee, mois, 1);

        Ostie ostie = ostieRepository.findTopByStatusAndDateLessThanEqualOrderByDateDesc(
                status,
                java.sql.Date.valueOf(d));

        if (ostie == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(ostie.getValeur());
    }

    public List<SalaireDTO> getAllSalaires() {
        return salaireRepository.findAll().stream()
                .map(s -> new SalaireDTO(
                        s.getIdSalaire(),
                        s.getMontant(),
                        s.getPoste() != null ? s.getPoste().getIdPoste() : null))
                .collect(Collectors.toList());
    }

    public BigDecimal getRetenuSurAbsence(String idEmploye, int mois, int annee) {
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());

        String idPoste = histoMouvementRepository.findIdPosteByEmploye(idEmploye, debutMois, finMois);
        if (idPoste == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal salaireBase = salaireRepository.findByPosteId(idPoste)
                .map(s -> s.getMontant())
                .orElse(BigDecimal.ZERO);

        Integer nbAbsence = histoMouvementRepository.findAbsenceByEmployeAndMonth(idEmploye, debutMois, finMois);
        if (nbAbsence == null || nbAbsence == 0) {
            return BigDecimal.ZERO;
        }

        return salaireBase
                .multiply(new BigDecimal(nbAbsence))
                .divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSalaireBase(String idEmploye, int mois, int annee) {
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());

        String idPoste = histoMouvementRepository.findIdPosteByEmploye(idEmploye, debutMois, finMois);
        if (idPoste == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal salaireBrut = salaireRepository.findByPosteId(idPoste)
                .map(s -> s.getMontant())
                .orElse(BigDecimal.ZERO);

        BigDecimal retenue = getRetenuSurAbsence(idEmploye, mois, annee);

        return salaireBrut.subtract(retenue)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTauxJournalier(String idEmploye, int mois, int annee) {
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());

        String idPoste = histoMouvementRepository.findIdPosteByEmploye(idEmploye, debutMois, finMois);
        if (idPoste == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal salaireBrut = salaireRepository.findByPosteId(idPoste)
                .map(s -> s.getMontant())
                .orElse(BigDecimal.ZERO);

        return salaireBrut
                .divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTauxHoraire(String idEmploye, int mois, int annee) {
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());

        String idPoste = histoMouvementRepository.findIdPosteByEmploye(idEmploye, debutMois, finMois);
        if (idPoste == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal salaireBrut = salaireRepository.findByPosteId(idPoste)
                .map(s -> s.getMontant())
                .orElse(BigDecimal.ZERO);

        return salaireBrut
                .divide(new BigDecimal(173.33), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMontantHeureSup(String idEmploye, int mois, int annee) {
        LocalDate debutMois = LocalDate.of(annee, mois, 1);
        LocalDate finMois = debutMois.withDayOfMonth(debutMois.lengthOfMonth());

        BigDecimal tauxHoraire = getTauxHoraire(idEmploye, mois, annee);
        if (tauxHoraire.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // Définir les jours fériés pour l'année
        List<LocalDate> joursFeries = List.of(
                LocalDate.of(annee, 1, 1), // Nouvel An
                LocalDate.of(annee, 12, 25), // Noël
                LocalDate.of(annee, 6, 26) // 26 juin
        );

        // Récupérer tous les mouvements de l'employé pour le mois
        List<HistoMouvement> mouvements = histoMouvementRepository.findAll().stream()
                .filter(h -> h.getEmploye().getIdEmploye().equals(idEmploye))
                .filter(h -> !h.getDate().isBefore(debutMois) && !h.getDate().isAfter(finMois))
                .collect(Collectors.toList());

        // Grouper les mouvements par semaine ISO
        Map<Integer, List<HistoMouvement>> semaines = mouvements.stream()
                .collect(Collectors.groupingBy(h -> h.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)));

        BigDecimal total = BigDecimal.ZERO;

        for (List<HistoMouvement> semaine : semaines.values()) {
            int heuresSemaineNormales = 0;

            for (HistoMouvement h : semaine) {
                DayOfWeek day = h.getDate().getDayOfWeek();
                int heuresSup = h.getHeureSup() != null ? h.getHeureSup() : 0;

                if (joursFeries.contains(h.getDate())) {
                    // Jour férié : 2 × taux horaire
                    total = total
                            .add(tauxHoraire.multiply(BigDecimal.valueOf(heuresSup)).multiply(BigDecimal.valueOf(2.0)));
                } else if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                    // Week-end : 1.4 × taux horaire
                    total = total
                            .add(tauxHoraire.multiply(BigDecimal.valueOf(heuresSup)).multiply(BigDecimal.valueOf(1.4)));
                } else {
                    // Semaine normale
                    heuresSemaineNormales += heuresSup;
                }
            }

            // Heures normales de la semaine : 1.3 × pour les 8 premières heures, 1.5 × pour
            // les 12 suivantes
            int heuresPremieres8 = Math.min(heuresSemaineNormales, 8);
            int heuresSuivantes12 = Math.max(0, Math.min(heuresSemaineNormales - 8, 12));

            total = total
                    .add(tauxHoraire.multiply(BigDecimal.valueOf(heuresPremieres8)).multiply(BigDecimal.valueOf(1.3)));
            total = total
                    .add(tauxHoraire.multiply(BigDecimal.valueOf(heuresSuivantes12)).multiply(BigDecimal.valueOf(1.5)));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSalaireBrut(String idEmploye, int mois, int annee) {
        BigDecimal salaireBase = getSalaireBase(idEmploye, mois, annee);
        BigDecimal heureSup = getMontantHeureSup(idEmploye, mois, annee);

        return salaireBase
                .add(heureSup);
    }

    public BigDecimal getCnapsPercent(String idEmploye, int mois, int annee) {
        BigDecimal salaireBrut = getSalaireBrut(idEmploye, mois, annee);
        BigDecimal getCnaps = getCnaps(mois, annee, "employe");

        BigDecimal valueCnaps = salaireBrut.multiply(getCnaps);

        BigDecimal plafond = new BigDecimal("10080");

        if (valueCnaps.compareTo(plafond) <= 0) {
            return valueCnaps.setScale(2, RoundingMode.HALF_UP);
        } else {
            return plafond;
        }
    }

    public BigDecimal getOstiePercent(String idEmploye, int mois, int annee) {
        BigDecimal salaireBrut = getSalaireBrut(idEmploye, mois, annee);
        BigDecimal ostie = getOstie(mois, annee, "employe");
        BigDecimal ValueOstie = salaireBrut.multiply(ostie);

        return ValueOstie;
    }

    public BigDecimal getRevenuImposable(String idEmploye, int mois, int annee) {
        BigDecimal cnaps = getCnapsPercent(idEmploye, mois, annee);
        BigDecimal ostie = getOstiePercent(idEmploye, mois, annee);
        BigDecimal salaireBrut = getSalaireBrut(idEmploye, mois, annee);

        return salaireBrut.subtract(cnaps.add(ostie)).setScale(2, RoundingMode.HALF_UP);
    }

    // public IrsaResponseDTO getIrsa(String idEmploye, int mois, int annee) {
    // BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

    // List<IrsaTrancheDTO> tranchesDTO = new ArrayList<>();
    // BigDecimal totalIrsa = BigDecimal.ZERO;

    // LocalDate dateRecherche = LocalDate.of(annee, mois, 1);

    // List<Irsa> tranchesDb = irsaRepository
    // .findByDateLessThanEqualOrderByMontantMinAsc(java.sql.Date.valueOf(dateRecherche));

    // // Application exacte des formules Excel
    // for (int i = 0; i < tranchesDb.size(); i++) {
    // Irsa tranche = tranchesDb.get(i);
    // BigDecimal min = BigDecimal.valueOf(tranche.getMontantMin());
    // BigDecimal max = tranche.getMontantMax() == null ? null :
    // BigDecimal.valueOf(tranche.getMontantMax());
    // BigDecimal taux = BigDecimal.valueOf(tranche.getTaux());

    // BigDecimal montantTranche = BigDecimal.ZERO;

    // if (i == 1) {
    // if (revenu.compareTo(BigDecimal.valueOf(350000)) > 0) {
    // montantTranche = BigDecimal.valueOf(50000).multiply(taux);
    // }
    // }
    // else if (i == 2) {
    // if (revenu.compareTo(BigDecimal.valueOf(400000)) > 0) {
    // montantTranche = BigDecimal.valueOf(100000).multiply(taux);
    // }
    // }
    // else if (i == 3) {
    // if (revenu.compareTo(BigDecimal.valueOf(500000)) > 0) {
    // montantTranche = BigDecimal.valueOf(100000).multiply(taux);
    // }
    // }
    // else if (i == 4) {
    // if (revenu.compareTo(BigDecimal.valueOf(600000)) > 0) {
    // montantTranche = BigDecimal.valueOf(100000).multiply(taux);
    // }
    // }
    // else if (i == 5) {
    // if (revenu.compareTo(BigDecimal.valueOf(4000000)) > 0) {
    // montantTranche = revenu.subtract(BigDecimal.valueOf(4000000)).multiply(taux);
    // }
    // }

    // montantTranche = montantTranche.setScale(0, RoundingMode.HALF_UP);
    // totalIrsa = totalIrsa.add(montantTranche);

    // String libelle = (max == null) ? min + " et plus" : min + " - " + max;
    // tranchesDTO.add(new IrsaTrancheDTO(libelle, montantTranche));
    // }

    // return new IrsaResponseDTO(
    // mois,
    // annee,
    // idEmploye,
    // tranchesDTO,
    // totalIrsa.setScale(0, RoundingMode.HALF_UP)
    // );
    // }
    public BigDecimal getIrsa5(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> tranches = irsaRepository.findLatestByTaux(0.05, dateRecherche);

        if (tranches == null || tranches.isEmpty()) {
            throw new RuntimeException("Tranche 5% non trouvée pour la date " + dateRecherche);
        }

        Irsa tranche5 = tranches.get(0);
        BigDecimal minTranche = BigDecimal.valueOf(tranche5.getMontantMin()); 
        BigDecimal maxTranche = BigDecimal.valueOf(tranche5.getMontantMax()); 
        BigDecimal taux = BigDecimal.valueOf(tranche5.getTaux()); 

        
        if (revenu.compareTo(minTranche.subtract(BigDecimal.ONE)) > 0) { 
            BigDecimal largeur = maxTranche.subtract(minTranche.subtract(BigDecimal.ONE)); 
            return largeur.multiply(taux).setScale(0, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getIrsa10(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> tranches = irsaRepository.findLatestByTaux(0.10, dateRecherche);

        if (tranches == null || tranches.isEmpty()) {
            throw new RuntimeException("Tranche 10% non trouvée pour la date " + dateRecherche);
        }

        Irsa tranche10 = tranches.get(0);
        BigDecimal minTranche = BigDecimal.valueOf(tranche10.getMontantMin()); 
        BigDecimal maxTranche = BigDecimal.valueOf(tranche10.getMontantMax()); 
        BigDecimal taux = BigDecimal.valueOf(tranche10.getTaux()); 

        if (revenu.compareTo(minTranche.subtract(BigDecimal.ONE)) > 0) { 
            BigDecimal largeur = maxTranche.subtract(minTranche.subtract(BigDecimal.ONE)); 
            return largeur.multiply(taux).setScale(0, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getIrsa15(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> tranches = irsaRepository.findLatestByTaux(0.15, dateRecherche);

        if (tranches == null || tranches.isEmpty()) {
            throw new RuntimeException("Tranche 15% non trouvée pour la date " + dateRecherche);
        }

        Irsa tranche15 = tranches.get(0);
        BigDecimal minTranche = BigDecimal.valueOf(tranche15.getMontantMin()); 
        BigDecimal maxTranche = BigDecimal.valueOf(tranche15.getMontantMax()); 
        BigDecimal taux = BigDecimal.valueOf(tranche15.getTaux()); 

        if (revenu.compareTo(minTranche.subtract(BigDecimal.ONE)) > 0) { 
            BigDecimal largeur = maxTranche.subtract(minTranche.subtract(BigDecimal.ONE)); 
            return largeur.multiply(taux).setScale(0, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getIrsa20(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> tranches = irsaRepository.findLatestByTaux(0.20, dateRecherche);

        if (tranches == null || tranches.isEmpty()) {
            throw new RuntimeException("Tranche 20% non trouvée pour la date " + dateRecherche);
        }

        Irsa tranche20 = tranches.get(0);
        BigDecimal taux = BigDecimal.valueOf(tranche20.getTaux()); 

        if (revenu.compareTo(BigDecimal.valueOf(600000)) > 0) {
            return BigDecimal.valueOf(100000).multiply(taux).setScale(0, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getIrsa25(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> tranches = irsaRepository.findLatestByTaux(0.25, dateRecherche);

        if (tranches == null || tranches.isEmpty()) {
            throw new RuntimeException("Tranche 25% non trouvée pour la date " + dateRecherche);
        }

        Irsa tranche25 = tranches.get(0);
        BigDecimal minTranche = BigDecimal.valueOf(tranche25.getMontantMin()); 
        BigDecimal taux = BigDecimal.valueOf(tranche25.getTaux()); 

        if (revenu.compareTo(minTranche.subtract(BigDecimal.ONE)) > 0) { 
            return revenu.subtract(minTranche.subtract(BigDecimal.ONE)).multiply(taux).setScale(0,
                    RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public IrsaResponseDTO getIrsa(String idEmploye, int mois, int annee) {
        BigDecimal revenu = getRevenuImposable(idEmploye, mois, annee);

        List<IrsaTrancheDTO> tranchesDTO = new ArrayList<>();
        BigDecimal totalIrsa = BigDecimal.ZERO;

        Date dateRecherche = java.sql.Date.valueOf(LocalDate.of(annee, mois, 1));
        List<Irsa> allTranches = irsaRepository.findByDateLessThanEqualOrderByMontantMinAsc(dateRecherche);

        BigDecimal irsa5 = getIrsa5(idEmploye, mois, annee);
        BigDecimal irsa10 = getIrsa10(idEmploye, mois, annee);
        BigDecimal irsa15 = getIrsa15(idEmploye, mois, annee);
        BigDecimal irsa20 = getIrsa20(idEmploye, mois, annee);
        BigDecimal irsa25 = getIrsa25(idEmploye, mois, annee);

        for (Irsa tranche : allTranches) {
            String libelle = (tranche.getMontantMax() == null)
                    ? tranche.getMontantMin() + " et plus"
                    : tranche.getMontantMin() + " - " + tranche.getMontantMax();

            BigDecimal montant = BigDecimal.ZERO;
            if (tranche.getTaux() == 0.05)
                montant = irsa5;
            else if (tranche.getTaux() == 0.10)
                montant = irsa10;
            else if (tranche.getTaux() == 0.15)
                montant = irsa15;
            else if (tranche.getTaux() == 0.20)
                montant = irsa20;
            else if (tranche.getTaux() == 0.25)
                montant = irsa25;

            tranchesDTO.add(new IrsaTrancheDTO(libelle, montant));
        }

        totalIrsa = irsa5.add(irsa10).add(irsa15).add(irsa20).add(irsa25);

        return new IrsaResponseDTO(
                mois,
                annee,
                idEmploye,
                tranchesDTO,
                totalIrsa);
    }

    public BigDecimal getRetenuTotal(String idEmploye, int mois, int annee) {
        BigDecimal cnaps = getCnapsPercent(idEmploye, mois, annee);
        BigDecimal ostie = getOstiePercent(idEmploye, mois, annee);
        IrsaResponseDTO irsa = getIrsa(idEmploye, mois, annee);
        BigDecimal irsaMontant = irsa.getTotalIrsa();

        return cnaps.add(ostie).add(irsaMontant).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSalaireNet(String idEmploye, int mois, int annee) {
        BigDecimal salaireBrut = getSalaireBrut(idEmploye, mois, annee);
        BigDecimal retenuTotal = getRetenuTotal(idEmploye, mois, annee);

        return salaireBrut.subtract(retenuTotal).setScale(2, RoundingMode.HALF_UP);
    }

}
