package com.hr.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.dto.ListeScoringPeriodeDTO;
import com.hr.management.dto.ScoringTicketDTO;
import com.hr.management.entities.TicketHisto;
import com.hr.management.repository.TicketHistoRepository;

@Service
public class ScoringService {

    private final TicketHistoRepository ticketHistoRepository;

    public ScoringService(TicketHistoRepository ticketHistoRepository) {
        this.ticketHistoRepository = ticketHistoRepository;
    }

    public ListeScoringPeriodeDTO getScoringPeriodeMonth(int month, int year) {

        List<TicketHisto> histoTicketDate = ticketHistoRepository.getHistoByMonthYear(month, year);

        int countAssigned = 0;
        int countLoading = 0;
        int countFinished = 0;
        int countClosed = 0;
        int countRejected = 0; 

        BigDecimal dureeEstimeeTotale = BigDecimal.ZERO;
        BigDecimal dureeReelleTotale = BigDecimal.ZERO;

        for (TicketHisto th : histoTicketDate) {
            int s = th.getStatut();

            if (s == 10) {
                countAssigned++;
            } else if (s == 20) {
                countLoading++;
            } else if (s == 30) {
                countFinished++;
            } else if (s == 40) {
                countClosed++;
            } else if (s == 50) { 
                countRejected++;
            }

            if (th.getTicket() != null && th.getTicket().getDureeEstimee() != null) {
                dureeEstimeeTotale = dureeEstimeeTotale.add(th.getTicket().getDureeEstimee());
            }

            if (th.getDuree() != null) {
                dureeReelleTotale = dureeReelleTotale.add(th.getDuree());
            }
        }

        BigDecimal scoreProductivite = (countAssigned > 0)
                ? BigDecimal.valueOf(countClosed)
                    .divide(BigDecimal.valueOf(countAssigned), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.TEN)
                : BigDecimal.ZERO;

        BigDecimal scoreEfficacite = dureeReelleTotale.compareTo(BigDecimal.ZERO) > 0 
    ? dureeEstimeeTotale.divide(dureeReelleTotale, 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN)
    : BigDecimal.ZERO;

if (scoreEfficacite.compareTo(BigDecimal.TEN) > 0) {
    scoreEfficacite = BigDecimal.TEN;
}


        int ticketsTermines = countFinished;
        BigDecimal scoreQualite = (ticketsTermines > 0)
                ? BigDecimal.TEN.subtract(
                        BigDecimal.valueOf(countRejected)
                            .divide(BigDecimal.valueOf(ticketsTermines), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.TEN)
                  )
                : BigDecimal.TEN;

        List<ScoringTicketDTO> listeScores = new ArrayList<>();
        listeScores.add(new ScoringTicketDTO("Productivité", scoreProductivite));
        listeScores.add(new ScoringTicketDTO("Efficacité", scoreEfficacite));
        listeScores.add(new ScoringTicketDTO("Qualité", scoreQualite));

        String periode = String.format("%02d-%04d", month, year);
        ListeScoringPeriodeDTO listeScoring = new ListeScoringPeriodeDTO(periode, listeScores);

        return listeScoring;
    }

    public ListeScoringPeriodeDTO getScoringPeriodeYear(int year) { 
        List<TicketHisto> histoTicketDate = ticketHistoRepository.getHistoByYear(year);     

        int countAssigned = 0;
        int countLoading = 0;
        int countFinished = 0;
        int countClosed = 0;
        int countRejected = 0;

        BigDecimal dureeEstimeeTotale = BigDecimal.ZERO;
        BigDecimal dureeReelleTotale = BigDecimal.ZERO;

        for (TicketHisto th : histoTicketDate) {
            int s = th.getStatut();

            if (s == 10) {
                countAssigned++;
            } else if (s == 20) {
                countLoading++;
            } else if (s == 30) {
                countFinished++;
            } else if (s == 40) {
                countClosed++;
            } else if (s == 50) { 
                countRejected++;
            }

            if (th.getTicket() != null && th.getTicket().getDureeEstimee() != null) {
                dureeEstimeeTotale = dureeEstimeeTotale.add(th.getTicket().getDureeEstimee());
            }

            if (th.getDuree() != null) {
                dureeReelleTotale = dureeReelleTotale.add(th.getDuree());
            }
        }

        BigDecimal scoreProductivite = (countAssigned > 0)
                ? BigDecimal.valueOf(countClosed)
                    .divide(BigDecimal.valueOf(countAssigned), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.TEN)
                : BigDecimal.ZERO;

        BigDecimal scoreEfficacite = dureeReelleTotale.compareTo(BigDecimal.ZERO) > 0 
    ? dureeEstimeeTotale.divide(dureeReelleTotale, 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN)
    : BigDecimal.ZERO;

if (scoreEfficacite.compareTo(BigDecimal.TEN) > 0) {
    scoreEfficacite = BigDecimal.TEN;
}


        int ticketsTermines = countFinished;
        BigDecimal scoreQualite = (ticketsTermines > 0)
                ? BigDecimal.TEN.subtract(
                        BigDecimal.valueOf(countRejected)
                            .divide(BigDecimal.valueOf(ticketsTermines), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.TEN)
                  )
                : BigDecimal.TEN;

        List<ScoringTicketDTO> listeScores = new ArrayList<>(); 
        listeScores.add(new ScoringTicketDTO("Productivité", scoreProductivite));
        listeScores.add(new ScoringTicketDTO("Efficacité", scoreEfficacite));
        listeScores.add(new ScoringTicketDTO("Qualité", scoreQualite));             

        String periode = String.format("%04d", year);
        ListeScoringPeriodeDTO listeScoring = new ListeScoringPeriodeDTO(periode, listeScores);

        return listeScoring;    
    }

    public List<ListeScoringPeriodeDTO> getScoringParMois(int year) {
    List<ListeScoringPeriodeDTO> resultats = new ArrayList<>();

    // Parcours des 12 mois
    for (int month = 1; month <= 12; month++) {
        List<TicketHisto> histoTicketDate = ticketHistoRepository.getHistoByMonthYear(month, year);

        int countAssigned = 0;
        int countLoading = 0;
        int countFinished = 0;
        int countClosed = 0;
        int countRejected = 0;

        BigDecimal dureeEstimeeTotale = BigDecimal.ZERO;
        BigDecimal dureeReelleTotale = BigDecimal.ZERO;

        for (TicketHisto th : histoTicketDate) {
            int s = th.getStatut();

            switch (s) {
                case 10 -> countAssigned++;
                case 20 -> countLoading++;
                case 30 -> countFinished++;
                case 40 -> countClosed++;
                case 50 -> countRejected++;
            }

            if (th.getTicket() != null && th.getTicket().getDureeEstimee() != null) {
                dureeEstimeeTotale = dureeEstimeeTotale.add(th.getTicket().getDureeEstimee());
            }
            if (th.getDuree() != null) {
                dureeReelleTotale = dureeReelleTotale.add(th.getDuree());
            }
        }

        // Calcul des scores
        BigDecimal scoreProductivite = (countAssigned > 0)
                ? BigDecimal.valueOf(countClosed)
                    .divide(BigDecimal.valueOf(countAssigned), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.TEN)
                : BigDecimal.ZERO;

        BigDecimal scoreEfficacite = dureeReelleTotale.compareTo(BigDecimal.ZERO) > 0
                ? dureeEstimeeTotale.divide(dureeReelleTotale, 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN)
                : BigDecimal.ZERO;

        if (scoreEfficacite.compareTo(BigDecimal.TEN) > 0) {
            scoreEfficacite = BigDecimal.TEN;
        }

        BigDecimal scoreQualite = (countFinished > 0)
                ? BigDecimal.TEN.subtract(
                        BigDecimal.valueOf(countRejected)
                                .divide(BigDecimal.valueOf(countFinished), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.TEN)
                  )
                : BigDecimal.TEN;

        List<ScoringTicketDTO> listeScores = new ArrayList<>();
        listeScores.add(new ScoringTicketDTO("Productivité", scoreProductivite));
        listeScores.add(new ScoringTicketDTO("Efficacité", scoreEfficacite));
        listeScores.add(new ScoringTicketDTO("Qualité", scoreQualite));

        String periode = String.format("%02d-%04d", month, year);
        resultats.add(new ListeScoringPeriodeDTO(periode, listeScores));
    }

    return resultats;
}
}
