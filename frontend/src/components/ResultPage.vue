<!-- FichePaieResult.vue -->
<script>
import axios from 'axios';

export default {
  props: ['idEmploye', 'mois', 'annee'],

  data() {
    return {
      salaire: null,
      loading: true,
      error: null
    };
  },

  async mounted() {
    await this.fetchSalaireData();
  },

  methods: {
    async fetchSalaireData() {
      try {
        const endpoints = [
          `/api/employe/${this.idEmploye}/${this.mois}/${this.annee}/PosteEmploye`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/salaireBase`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/retenuSurAbsence`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/tauxHeureSup`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/irsaTranche`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/salaireBrut`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/retenuTotal`,
          `/api/salaire/${this.idEmploye}/${this.mois}/${this.annee}/details`
        ];

        const requests = endpoints.map(url => 
          axios.get(url)
            .then(response => response.data)
            .catch(err => {
              console.warn(`Échec de ${url}:`, err.response?.status || err.message);
              return null;
            })
        );

        const responses = await Promise.all(requests);

        const hasEssentialData = responses[0] !== null && responses[7] !== null;
        
        if (!hasEssentialData) {
          throw new Error('Impossible de récupérer les données depuis le serveur');
        }

        this.salaire = {
          info: responses[0] || {},
          salaireBase: responses[1]?.montant ?? 0,
          retenuAbsence: responses[2]?.montant ?? 0,
          heureSup: responses[3]?.indemnite ?? 0,
          irsa: responses[4] || {},
          salaireBrut: responses[5]?.montant ?? 0,
          retenuTotal: responses[6] ?? 0,
          cnaps: responses[7]?.cnaps ?? 0,
          ostie: responses[7]?.ostie ?? 0,
          salaireNet: responses[7]?.salaireNet ?? 0
        };

      } catch (err) {
        console.error('Erreur détaillée:', err);
        this.error = `Erreur: ${err.message}. Vérifiez la connexion au serveur.`;
      } finally {
        this.loading = false;
      }
    },

    getNomMois(mois) {
      const moisNoms = [
        '', 'Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin',
        'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'
      ];
      return moisNoms[parseInt(mois)] || mois;
    },

    retryConnection() {
      this.loading = true;
      this.error = null;
      this.salaire = null;
      this.fetchSalaireData();
    },

    formatMontant(montant) {
      return new Intl.NumberFormat('fr-FR').format(montant);
    }
  }
};
</script>

<template>
  <div class="page-wrapper">
    <div class="fiche-container">

      <!-- En-tête -->
      <div class="header-section">
        <h1 class="title">FICHE DE PAIE</h1>
        <div class="period">{{ getNomMois(mois) }} {{ annee }}</div>
        <div class="employee-id">Employé N° {{ idEmploye }}</div>
      </div>

      <!-- États de chargement et d'erreur -->
      <div v-if="loading" class="status-message loading">
        <div class="spinner"></div>
        <div>Chargement des données de paie...</div>
      </div>

      <div v-if="error" class="status-message error">
        <div class="error-icon">❌</div>
        <div class="error-text">{{ error }}</div>
        <button @click="retryConnection" class="retry-button">
          🔄 Réessayer la connexion
        </button>
      </div>

      <!-- Tableau principal -->
      <div v-if="!loading && salaire" class="table-container">
        
        <!-- Informations employé -->
        <table class="excel-table info-table">
          <thead>
            <tr>
              <th colspan="4" class="table-section-header">INFORMATIONS EMPLOYÉ</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="label-cell"><strong>Nom</strong></td>
              <td class="value-cell">{{ salaire.info.nom || 'Non disponible' }}</td>
              <td class="label-cell"><strong>Prénom</strong></td>
              <td class="value-cell">{{ salaire.info.prenom || 'Non disponible' }}</td>
            </tr>
            <tr>
              <td class="label-cell"><strong>Poste</strong></td>
              <td class="value-cell" colspan="3">{{ salaire.info.poste || 'Non disponible' }}</td>
            </tr>
          </tbody>
        </table>

        <!-- Rémunérations -->
        <table class="excel-table remuneration-table">
          <thead>
            <tr>
              <th colspan="3" class="table-section-header">RÉMUNÉRATIONS</th>
            </tr>
            <tr class="column-headers">
              <th class="header-cell">Description</th>
              <th class="header-cell">Montant</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td class="label-cell">Salaire de base</td>
              <td class="value-cell numeric">{{ formatMontant(salaire.salaireBase) }} Ar</td>
            </tr>
            <tr v-if="salaire.heureSup > 0">
              <td class="label-cell">Heures supplémentaires</td>
              <td class="value-cell numeric positive">+ {{ formatMontant(salaire.heureSup) }} Ar</td>
            </tr>
            <tr class="total-row">
              <td class="label-cell"><strong>SALAIRE BRUT</strong></td>
              <td class="value-cell numeric total-cell"><strong>{{ formatMontant(salaire.salaireBrut) }} Ar</strong></td>
            </tr>
          </tbody>
        </table>

        <!-- Retenues -->
        <table class="excel-table deductions-table">
          <thead>
            <tr>
              <th colspan="3" class="table-section-header">RETENUES ET COTISATIONS</th>
            </tr>
            <tr class="column-headers">
              <th class="header-cell">Description</th>
              <th class="header-cell">Montant</th>
              <th class="header-cell">Base/Taux</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="salaire.retenuAbsence > 0">
              <td class="label-cell">Retenu sur absence</td>
              <td class="value-cell numeric negative">- {{ formatMontant(salaire.retenuAbsence) }} Ar</td>
              <td class="note-cell">Absences non justifiées</td>
            </tr>
            
            <!-- IRSA -->
            <tr v-if="salaire.irsa.totalIrsa > 0">
              <td class="label-cell">IRSA - Impôt sur le revenu</td>
              <td class="value-cell numeric negative">- {{ formatMontant(salaire.irsa.totalIrsa) }} Ar</td>
              <td class="note-cell">Impôt progressif</td>
            </tr>
            
            <!-- Détails des tranches IRSA -->
            <tr v-for="tranche in (salaire.irsa.tranches || [])" 
                :key="tranche.libelle"
                class="sub-item">
              <td class="label-cell sub-label">{{ tranche.libelle }}</td>
              <td class="value-cell numeric sub-value">{{ formatMontant(tranche.montant) }} Ar</td>
              <td class="note-cell">Tranche IRSA</td>
            </tr>

            <!-- Cotisations sociales -->
            <tr>
              <td class="label-cell">CNaPS (1%)</td>
              <td class="value-cell numeric negative">- {{ formatMontant(salaire.cnaps) }} Ar</td>
              <td class="note-cell">1% du salaire brut</td>
            </tr>
            <tr>
              <td class="label-cell">OSTIE (1%)</td>
              <td class="value-cell numeric negative">- {{ formatMontant(salaire.ostie) }} Ar</td>
              <td class="note-cell">1% du salaire brut</td>
            </tr>

            <tr class="total-row">
              <td class="label-cell"><strong>TOTAL DES RETENUES</strong></td>
              <td class="value-cell numeric total-cell negative"><strong>- {{ formatMontant(salaire.retenuTotal) }} Ar</strong></td>
              <td class="note-cell">Somme des retenues</td>
            </tr>
          </tbody>
        </table>

        <!-- Salaire Net -->
        <table class="excel-table net-salary-table">
          <thead>
            <tr>
              <th colspan="3" class="table-section-header highlight">SALAIRE NET À PAYER</th>
            </tr>
          </thead>
          <tbody>
            <tr class="final-calculation">
              <td class="label-cell">Salaire Brut</td>
              <td class="value-cell numeric">{{ formatMontant(salaire.salaireBrut) }} Ar</td>
              <td class="note-cell"></td>
            </tr>
            <tr class="final-calculation">
              <td class="label-cell">Total des retenues</td>
              <td class="value-cell numeric negative">- {{ formatMontant(salaire.retenuTotal) }} Ar</td>
              <td class="note-cell"></td>
            </tr>
            <tr class="net-total-row">
              <td class="label-cell"><strong>NET À PAYER</strong></td>
              <td class="value-cell numeric net-total-cell"><strong>{{ formatMontant(salaire.salaireNet) }} Ar</strong></td>
              <td class="note-cell">Montant à verser</td>
            </tr>
          </tbody>
        </table>

      </div>

    </div>
  </div>
</template>

<style scoped>
.page-wrapper {
  padding: 2rem 1rem;
  background: #f8f9fa;
  min-height: 100vh;
}

.fiche-container {
  max-width: 1000px;
  margin: 0 auto;
}

/* En-tête */
.header-section {
  text-align: center;
  margin-bottom: 2rem;
  padding: 2rem;
  background: white;
  border: 1px solid #d0d7e0;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}

.title {
  color: #2c3e50;
  margin-bottom: 0.5rem;
  font-size: 1.8rem;
  font-weight: 600;
}

.period, .employee-id {
  color: #5f6368;
  font-size: 1.1rem;
  margin: 0.25rem 0;
}

/* Conteneur des tableaux */
.table-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* Style général des tableaux Excel-like */
.excel-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border: 1px solid #d0d7e0;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* En-têtes de section */
.table-section-header {
  background: #2c3e50;
  color: white;
  padding: 12px 16px;
  text-align: center;
  font-size: 1.1rem;
  font-weight: 600;
  border-bottom: 1px solid #d0d7e0;
}

.table-section-header.highlight {
  background: #27ae60;
}

/* En-têtes de colonnes */
.column-headers {
  background: #f8f9fa;
}

.header-cell {
  padding: 10px 12px;
  text-align: left;
  font-weight: 600;
  color: #2c3e50;
  border-bottom: 2px solid #d0d7e0;
  border-right: 1px solid #e9ecef;
  background: #f1f3f4;
}

.header-cell:last-child {
  border-right: none;
}

/* Cellules */
.label-cell {
  padding: 10px 12px;
  border: 1px solid #e9ecef;
  background: #fafbfc;
  font-weight: 500;
  color: #5f6368;
  width: 30%;
}

.value-cell {
  padding: 10px 12px;
  border: 1px solid #e9ecef;
  background: white;
  color: #2c3e50;
  width: 25%;
}

.note-cell {
  padding: 10px 12px;
  border: 1px solid #e9ecef;
  background: white;
  color: #7f8c8d;
  font-size: 0.9rem;
  width: 45%;
}

/* Alignement numérique */
.numeric {
  text-align: right;
  font-family: 'Courier New', Consolas, monospace;
  font-weight: 500;
}

/* Couleurs pour les montants */
.positive {
  color: #27ae60;
  font-weight: 600;
}

.negative {
  color: #e74c3c;
  font-weight: 600;
}

/* Lignes de total */
.total-row {
  background: #f8f9fa;
  border-top: 2px solid #d0d7e0;
}

.total-cell {
  background: #e8f5e8;
  font-size: 1.1rem;
}

.net-total-row {
  background: #e8f5e8;
  border-top: 3px double #27ae60;
  border-bottom: 3px double #27ae60;
}

.net-total-cell {
  background: #27ae60;
  color: white;
  font-size: 1.2rem;
}

/* Sous-éléments (tranches IRSA) */
.sub-item {
  background: #fafbfc;
}

.sub-label {
  padding-left: 24px;
  font-size: 0.9rem;
  color: #7f8c8d;
}

.sub-value {
  font-size: 0.9rem;
  color: #7f8c8d;
}

/* Lignes de calcul final */
.final-calculation {
  border-bottom: 1px dashed #e9ecef;
}

/* États de chargement et d'erreur */
.status-message {
  text-align: center;
  padding: 3rem 2rem;
  background: white;
  border: 1px solid #d0d7e0;
  border-radius: 4px;
  margin: 2rem 0;
}

.loading {
  color: #3498db;
  background: #f8f9fa;
}

.error {
  color: #e74c3c;
  background: #fdf2f2;
  border-color: #f5c6cb;
}

.error-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.error-text {
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
  font-weight: 500;
}

.retry-button {
  background: #3498db;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  transition: background-color 0.2s;
}

.retry-button:hover {
  background: #2980b9;
}

.spinner {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 768px) {
  .page-wrapper {
    padding: 1rem 0.5rem;
  }
  
  .header-section {
    padding: 1.5rem 1rem;
    margin-bottom: 1.5rem;
  }
  
  .title {
    font-size: 1.5rem;
  }
  
  .table-container {
    gap: 1rem;
  }
  
  .excel-table {
    font-size: 0.9rem;
  }
  
  .label-cell,
  .value-cell,
  .note-cell {
    padding: 8px 10px;
  }
  
  .table-section-header {
    padding: 10px 12px;
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .excel-table {
    font-size: 0.8rem;
  }
  
  .label-cell,
  .value-cell,
  .note-cell {
    padding: 6px 8px;
  }
  
  .numeric {
    font-size: 0.85rem;
  }
}
</style>