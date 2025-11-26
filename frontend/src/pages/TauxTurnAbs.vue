<template>
  <div class="stats-container">
    <h1>📊 Tableau de Bord RH - Turnover & Absentéisme</h1>

    <!-- FILTRES -->
    <div class="filtres-section">
      <div class="filtre-group">
        <label for="annee">Année:</label>
        <select v-model="anneeSelectionnee" class="select-filtre" @change="chargerDonnees">
          <option v-for="annee in anneesDisponibles" :key="annee" :value="annee">{{ annee }}</option>
        </select>
      </div>
      <div class="filtre-group">
        <label for="mois">Mois:</label>
        <select v-model="moisSelectionne" class="select-filtre" @change="chargerDonnees">
          <option v-for="mois in moisDisponibles" :key="mois.num" :value="mois.num">
            {{ mois.label }}
          </option>
        </select>
      </div>
    </div>

    <!-- CHARGEMENT -->
    <div v-if="loading" class="loading">
      🔄 Chargement des données...
    </div>

    <!-- KPI CARDS -->
    <div v-else class="kpi-grid">
      <div class="kpi-card turnover">
        <div class="kpi-icon">🔄</div>
        <div class="kpi-content">
          <h3>Taux de Turnover</h3>
          <div class="kpi-value">{{ tauxTurnoverActuel }}%</div>
          <div class="kpi-detail">{{ turnoverData.nombreDeparts }} départ(s)</div>
        </div>
      </div>

      <div class="kpi-card absenteisme">
        <div class="kpi-icon">🏥</div>
        <div class="kpi-content">
          <h3>Taux d'Absentéisme</h3>
          <div class="kpi-value">{{ tauxAbsenteismeActuel }}%</div>
          <div class="kpi-detail">{{ turnoverData.joursAbsenceTotaux }} jour(s)</div>
        </div>
      </div>

      <div class="kpi-card anciennete">
        <div class="kpi-icon">📅</div>
        <div class="kpi-content">
          <h3>Ancienneté Moyenne</h3>
          <div class="kpi-value">{{ ancienneteMoyenne }} mois</div>
          <div class="kpi-detail">{{ turnoverData.effectifMoyen }} employés</div>
        </div>
      </div>

      <div class="kpi-card conges">
        <div class="kpi-icon">🏖️</div>
        <div class="kpi-content">
          <h3>Congés Non Pris</h3>
          <div class="kpi-value">{{ turnoverData.congesNonPris }}</div>
          <div class="kpi-detail">{{ turnoverData.congesPris }} pris</div>
        </div>
      </div>
    </div>

    <!-- ALERTE CORRECTION DONNÉES -->
    <div v-if="!loading && donneesAvecProbleme" class="alert-warning">
      <strong>⚠️ Note:</strong> Certaines données peuvent nécessiter une correction côté backend.
      Les taux supérieurs à 100% indiquent un problème de calcul.
    </div>

    <!-- GRAPHIQUES -->
    <div v-if="!loading && historiqueReel.length > 0" class="charts-section">
      <h3>📈 Visualisations des Données</h3>
      
      <div class="charts-grid">
        <!-- Évolution du turnover sur l'année -->
        <div class="chart-card">
          <h4>Évolution du Turnover ({{ anneeSelectionnee }})</h4>
          <div class="chart-container">
            <canvas ref="turnoverChart" width="400" height="200"></canvas>
          </div>
        </div>

        <!-- Comparaison turnover vs absentéisme -->
        <div class="chart-card">
          <h4>Turnover vs Absentéisme</h4>
          <div class="chart-container">
            <canvas ref="comparisonChart" width="400" height="200"></canvas>
          </div>
        </div>
      </div>
    </div>

    <!-- DONNÉES DÉTAILLÉES -->
    <div v-if="!loading" class="details-section">
      <h3>📋 Détail des Données</h3>
      
      <!-- TURNOVER PAR POSTE POUR LE MOIS -->
      <div class="postes-turnover">
        <h4>Turnover par Poste ({{ moisSelectionne }}/{{ anneeSelectionnee }})</h4>
        <div class="postes-grid">
          <div v-for="poste in turnoverPostesCorriges" :key="poste.idPoste" class="poste-card">
            <div class="poste-header">
              <strong>{{ poste.posteLibelle }}</strong>
              <span class="domaine-badge">{{ poste.domaineLibelle }}</span>
            </div>
            <div class="poste-stats">
              <div class="stat">
                <span class="label">Effectif début:</span>
                <span class="value">{{ poste.effectifDebut }}</span>
              </div>
              <div class="stat">
                <span class="label">Effectif fin:</span>
                <span class="value">{{ poste.effectifFin }}</span>
              </div>
              <div class="stat">
                <span class="label">Arrivées:</span>
                <span class="value arrivee">{{ poste.nombreArrivees }}</span>
              </div>
              <div class="stat">
                <span class="label">Départs:</span>
                <span class="value depart">{{ poste.nombreDeparts }}</span>
              </div>
              <div class="stat">
                <span class="label">Turnover:</span>
                <span class="value turnover" :class="{'data-problem': poste.tauxTurnover > 100}">
                  {{ poste.tauxTurnover.toFixed(1) }}%
                  <span v-if="poste.tauxTurnover > 100" class="problem-indicator">⚠️</span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- HISTORIQUE DES MOIS -->
      <div class="historique-table">
        <h4>Historique des Indicateurs ({{ anneeSelectionnee }})</h4>
        <div class="table-container">
          <table class="data-table">
            <thead>
              <tr>
                <th>Mois</th>
                <th>Turnover</th>
                <th>Absentéisme</th>
                <th>Départs</th>
                <th>Effectif</th>
                <th>Ancienneté</th>
                <th>Statut</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="mois in historiqueReel" :key="mois.periode">
                <td>{{ formaterPeriode(mois.periode) }}</td>
                <td class="percentage" :class="{'data-problem': mois.tauxTurnover > 100}">
                  {{ (mois.tauxTurnover).toFixed(1) }}%
                  <span v-if="mois.tauxTurnover > 100" class="problem-indicator">⚠️</span>
                </td>
                <td class="percentage">{{ (mois.tauxAbsenteisme).toFixed(1) }}%</td>
                <td class="number">{{ mois.nombreDeparts }}</td>
                <td class="number">{{ mois.effectifMoyen }}</td>
                <td class="number">{{ mois.ancienneteteMoyenne?.toFixed(1) || '0.0' }}m</td>
                <td>
                  <span v-if="mois.tauxTurnover > 100" class="status-badge problem">Donnée suspecte</span>
                  <span v-else-if="mois.tauxTurnover > 20" class="status-badge warning">Élevé</span>
                  <span v-else-if="mois.tauxTurnover > 5" class="status-badge normal">Normal</span>
                  <span v-else class="status-badge good">Bas</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      anneeSelectionnee: new Date().getFullYear(),
      moisSelectionne: new Date().getMonth() + 1,
      turnoverData: {},
      turnoverPostes: [],
      historiqueReel: [],
      anneesDisponibles: [2025, 2024, 2023],
      moisDisponibles: [
        { num: 1, label: 'Janvier' }, { num: 2, label: 'Février' }, { num: 3, label: 'Mars' },
        { num: 4, label: 'Avril' }, { num: 5, label: 'Mai' }, { num: 6, label: 'Juin' },
        { num: 7, label: 'Juillet' }, { num: 8, label: 'Août' }, { num: 9, label: 'Septembre' },
        { num: 10, label: 'Octobre' }, { num: 11, label: 'Novembre' }, { num: 12, label: 'Décembre' }
      ],
      loading: false,
      chartsInitialized: false
    }
  },
  computed: {
    tauxTurnoverActuel() {
      return (this.turnoverData.tauxTurnover).toFixed(1);
    },
    tauxAbsenteismeActuel() {
      return (this.turnoverData.tauxAbsenteisme).toFixed(1);
    },
    ancienneteMoyenne() {
      return this.turnoverData.ancienneteteMoyenne?.toFixed(1) || '0.0';
    },
    
    // Correction manuelle des données problématiques
    turnoverPostesCorriges() {
      return this.turnoverPostes.map(poste => {
        // Si le taux est anormalement élevé (> 1000%), c'est probablement une erreur de calcul
        let tauxCorrige = poste.tauxTurnover;
        if (poste.tauxTurnover > 1000) {
          // Essayons de recalculer correctement
          const effectifMoyen = (poste.effectifDebut + poste.effectifFin) / 2;
          tauxCorrige = effectifMoyen > 0 ? (poste.nombreDeparts / effectifMoyen) * 100 : 0;
        }
        
        return {
          ...poste,
          tauxTurnover: tauxCorrige
        };
      });
    },
    
    donneesAvecProbleme() {
      return this.historiqueReel.some(mois => mois.tauxTurnover > 100) ||
             this.turnoverPostes.some(poste => poste.tauxTurnover > 100);
    }
  },
  async created() {
    await this.chargerDonnees();
  },
  methods: {
    async chargerDonnees() {
      this.loading = true;
      this.chartsInitialized = false;
      
      try {
        // 1. Charger les données du mois sélectionné
        const response = await axios.get(
          `/employes/statistiques/turnover/${this.anneeSelectionnee}/${this.moisSelectionne}`
        );
        this.turnoverData = response.data;

        // 2. Charger le détail par poste pour ce mois
        await this.chargerTurnoverPostes();

        // 3. Charger l'historique de l'année pour les graphiques
        await this.chargerHistoriqueReel();

        // 4. Attendre que le DOM soit mis à jour avant de créer les graphiques
        this.$nextTick(() => {
          this.creerGraphiques();
        });

      } catch (error) {
        console.error('Erreur chargement données:', error);
      } finally {
        this.loading = false;
      }
    },

    async chargerTurnoverPostes() {
      try {
        const response = await axios.get(
          `/employes/statistiques/turnover/tous-postes/${this.anneeSelectionnee}/${this.moisSelectionne}`
        );
        this.turnoverPostes = response.data;
      } catch (error) {
        console.error('Erreur chargement turnover postes:', error);
        this.turnoverPostes = [];
      }
    },

    async chargerHistoriqueReel() {
      try {
        const promesses = [];
        for (let mois = 1; mois <= 12; mois++) {
          promesses.push(
            axios.get(`/employes/statistiques/turnover/${this.anneeSelectionnee}/${mois}`)
              .then(response => ({
                periode: `${this.anneeSelectionnee}-${mois.toString().padStart(2, '0')}`,
                ...response.data
              }))
              .catch(() => null)
          );
        }

        const resultats = await Promise.all(promesses);
        this.historiqueReel = resultats.filter(mois => mois !== null);

      } catch (error) {
        console.error('Erreur chargement historique:', error);
        this.historiqueReel = [];
      }
    },

    formaterPeriode(periode) {
      const [annee, mois] = periode.split('-');
      const nomsMois = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aoû', 'Sep', 'Oct', 'Nov', 'Déc'];
      return `${nomsMois[parseInt(mois) - 1]}`;
    },

    async creerGraphiques() {
      if (this.historiqueReel.length === 0) {
        console.log('Aucune donnée pour créer les graphiques');
        return;
      }

      // Importer Chart.js dynamiquement
      const { Chart, registerables } = await import('chart.js');
      Chart.register(...registerables);

      this.creerGraphiqueTurnover(Chart);
      this.creerGraphiqueComparaison(Chart);
      this.chartsInitialized = true;
    },

    creerGraphiqueTurnover(Chart) {
      const canvas = this.$refs.turnoverChart;
      if (!canvas) return;

      const ctx = canvas.getContext('2d');
      
      if (canvas.chart) {
        canvas.chart.destroy();
      }

      // Limiter les valeurs à 100% maximum pour éviter les échelles démesurées
      const donneesLimitees = this.historiqueReel.map(h => 
        Math.min(h.tauxTurnover, 100)
      );

      canvas.chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: this.historiqueReel.map(h => this.formaterPeriode(h.periode)),
          datasets: [{
            label: 'Taux de Turnover (%)',
            data: donneesLimitees,
            borderColor: '#e74c3c',
            backgroundColor: 'rgba(231, 76, 60, 0.1)',
            tension: 0.4,
            fill: true,
            borderWidth: 3
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: true, position: 'top' },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const valeurReelle = context.dataset.data[context.dataIndex];
                  const valeurOriginale = context.dataset.originalData[context.dataIndex];
                  let tooltip = `Turnover: ${valeurReelle.toFixed(1)}%`;
                  if (valeurOriginale > 100) {
                    tooltip += ` (donnée limitée - original: ${valeurOriginale.toFixed(1)}%)`;
                  }
                  return tooltip;
                }
              }
            }
          },
          scales: {
            y: {
              beginAtZero: true,
              max: 100, // Limiter l'échelle Y à 100%
              title: { display: true, text: 'Taux (%)' },
              ticks: { callback: value => value + '%' }
            }
          }
        }
      });

      // Stocker les données originales pour les tooltips
      canvas.chart.data.datasets[0].originalData = this.historiqueReel.map(h => h.tauxTurnover);
    },

    creerGraphiqueComparaison(Chart) {
      const canvas = this.$refs.comparisonChart;
      if (!canvas) return;

      const ctx = canvas.getContext('2d');
      
      if (canvas.chart) {
        canvas.chart.destroy();
      }

      // Limiter les valeurs pour ce graphique aussi
      const turnoverLimite = this.historiqueReel.map(h => Math.min(h.tauxTurnover, 100));
      const absenteismeLimite = this.historiqueReel.map(h => Math.min(h.tauxAbsenteisme, 100));

      canvas.chart = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: this.historiqueReel.map(h => this.formaterPeriode(h.periode)),
          datasets: [
            {
              label: 'Turnover (%)',
              data: turnoverLimite,
              backgroundColor: 'rgba(231, 76, 60, 0.8)',
              borderColor: '#e74c3c',
              borderWidth: 1
            },
            {
              label: 'Absentéisme (%)',
              data: absenteismeLimite,
              backgroundColor: 'rgba(52, 152, 219, 0.8)',
              borderColor: '#3498db',
              borderWidth: 1
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            x: { stacked: false },
            y: {
              beginAtZero: true,
              max: 100,
              title: { display: true, text: 'Taux (%)' },
              ticks: { callback: value => value + '%' }
            }
          }
        }
      });
    }
  }
}
</script>


<style scoped>
.stats-container {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 0 auto;
}

h1 {
  color: #2c3e50;
  text-align: center;
  margin-bottom: 30px;
  font-size: 2em;
}

/* FILTRES */
.filtres-section {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  align-items: flex-end;
  flex-wrap: wrap;
}

.filtre-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filtre-group label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 14px;
}

.select-filtre {
  padding: 10px 12px;
  border: 1px solid #bdc3c7;
  border-radius: 4px;
  font-size: 14px;
  background-color: white;
  cursor: pointer;
  min-width: 150px;
}

/* KPI CARDS */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.kpi-card {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 20px;
  border-left: 5px solid;
  transition: transform 0.3s ease;
}

.kpi-card:hover {
  transform: translateY(-5px);
}

.kpi-card.turnover { border-left-color: #e74c3c; }
.kpi-card.absenteisme { border-left-color: #3498db; }
.kpi-card.anciennete { border-left-color: #f39c12; }
.kpi-card.conges { border-left-color: #2ecc71; }

.kpi-icon {
  font-size: 2.5em;
}

.kpi-content h3 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 14px;
  font-weight: 600;
}

.kpi-value {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
}

.kpi-card.turnover .kpi-value { color: #e74c3c; }
.kpi-card.absenteisme .kpi-value { color: #3498db; }
.kpi-card.anciennete .kpi-value { color: #f39c12; }
.kpi-card.conges .kpi-value { color: #2ecc71; }

.kpi-detail {
  color: #7f8c8d;
  font-size: 12px;
  margin: 0;
}

/* SECTION GRAPHIQUES */
.charts-section {
  margin-bottom: 30px;
}

.charts-section h3 {
  color: #2c3e50;
  margin-bottom: 20px;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 25px;
}

.chart-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border: 1px solid #e1e8ed;
}

.chart-card h4 {
  margin: 0 0 15px 0;
  color: #2c3e50;
  font-size: 16px;
  text-align: center;
}

.chart-container {
  height: 300px;
  position: relative;
}

/* SECTION DÉTAILS */
.details-section {
  margin-top: 30px;
}

.details-section h3 {
  color: #2c3e50;
  margin-bottom: 20px;
  border-bottom: 2px solid #3498db;
  padding-bottom: 10px;
}

.postes-turnover {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 20px;
}

.postes-turnover h4 {
  margin: 0 0 15px 0;
  color: #2c3e50;
}

.postes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 15px;
}

.poste-card {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #3498db;
}

.poste-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
}

.domaine-badge {
  background: #3498db;
  color: white;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 10px;
  font-weight: 600;
}

.poste-stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.stat {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.stat .label {
  color: #7f8c8d;
}

.stat .value {
  font-weight: 600;
}

.stat .value.arrivee { color: #2ecc71; }
.stat .value.depart { color: #e74c3c; }
.stat .value.turnover { color: #f39c12; }

.historique-table {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ecf0f1;
}

.data-table th {
  background-color: #34495e;
  color: white;
  font-weight: 600;
  position: sticky;
  top: 0;
}

.data-table tr:hover {
  background-color: #f8f9fa;
}

.data-table .number {
  text-align: right;
  font-family: 'Courier New', monospace;
  font-weight: 600;
}

.data-table .percentage {
  text-align: right;
  color: #2c3e50;
  font-weight: 600;
}

/* ÉTATS */
.loading {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
  font-size: 16px;
}

.no-charts {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 8px;
  margin: 20px 0;
}

.no-charts-icon {
  font-size: 48px;
  margin-bottom: 15px;
  opacity: 0.5;
}

/* RESPONSIVE */
@media (max-width: 768px) {
  .stats-container {
    padding: 15px;
  }

  .filtres-section {
    flex-direction: column;
    align-items: stretch;
  }

  .kpi-grid {
    grid-template-columns: 1fr;
  }

  .kpi-card {
    padding: 20px;
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }

  .chart-container {
    height: 250px;
  }

  .postes-grid {
    grid-template-columns: 1fr;
  }
}
.alert-warning {
  background: #fff3cd;
  border: 1px solid #ffeaa7;
  border-radius: 6px;
  padding: 15px;
  margin: 20px 0;
  color: #856404;
}

.data-problem {
  color: #e74c3c;
  font-weight: bold;
}

.problem-indicator {
  margin-left: 5px;
  font-size: 12px;
}

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 600;
}

.status-badge.problem {
  background: #e74c3c;
  color: white;
}

.status-badge.warning {
  background: #f39c12;
  color: white;
}

.status-badge.normal {
  background: #3498db;
  color: white;
}

.status-badge.good {
  background: #2ecc71;
  color: white;
}
</style>