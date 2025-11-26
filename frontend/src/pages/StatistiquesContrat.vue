<template>
  <div class="stats-container">
    <h1>📑 Statistiques des Contrats</h1>

    <div class="filtres-section">
      <div class="filtre-group">
        <label for="anneeContrats">Année:</label>
        <select v-model="anneeSelectionnee" id="anneeContrats" class="select-filtre">
          <option v-for="annee in anneesDisponibles" :key="annee" :value="annee">{{ annee }}</option>
        </select>
      </div>
    </div>

    <!-- RÉPARTITION DES CONTRATS -->
    <div class="charts-grid">
      <!-- Camembert répartition -->
      <div class="chart-card">
        <h3>📊 Répartition des Types de Contrats ({{ anneeSelectionnee }})</h3>
        <div class="pie-chart-container">
          <canvas ref="contratsPieChart"></canvas>
        </div>
      </div>

      <!-- Évolution temporelle -->
      <div class="chart-card">
        <h3>📈 Évolution des Contrats par Type</h3>
        <div class="line-chart-container">
          <canvas ref="contratsLineChart"></canvas>
        </div>
      </div>
    </div>

    <!-- TABLEAU DÉTAILLÉ -->
    <div class="table-section">
      <h3>📋 Détail Mensuel des Contrats</h3>
      <div class="table-container">
        <table class="data-table">
          <thead>
            <tr>
              <th>Mois</th>
              <th>Total</th>
              <th>CDI</th>
              <th>CDD</th>
              <th>Apprentissage</th>
              <th>% CDI</th>
              <th>% CDD</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="mois in donneesContrats" :key="mois.periode">
              <td>{{ formaterPeriode(mois.periode) }}</td>
              <td class="number">{{ mois.totalContrats }}</td>
              <td class="number">{{ mois.nombreCDI }}</td>
              <td class="number">{{ mois.nombreCDD }}</td>
              <td class="number">{{ mois.nombreApprentissage }}</td>
              <td class="percentage">{{ mois.pourcentageCDI.toFixed(1) }}%</td>
              <td class="percentage">{{ mois.pourcentageCDD.toFixed(1) }}%</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Chart, registerables } from 'chart.js';

Chart.register(...registerables);

export default {
  data() {
    return {
      anneeSelectionnee: new Date().getFullYear(),
      donneesContrats: [],
      anneesDisponibles: [2025, 2024, 2023],
      contratsPieChart: null,
      contratsLineChart: null
    }
  },
  async created() {
    await this.chargerContrats();
  },
  watch: {
    anneeSelectionnee() {
      this.chargerContrats();
    }
  },
  methods: {
    async chargerContrats() {
      try {
        const response = await axios.get(`/employes/statistiques/contrats/annee/${this.anneeSelectionnee}`);
        this.donneesContrats = response.data;
        this.creerGraphiquesContrats();
      } catch (error) {
        console.error('Erreur chargement contrats:', error);
      }
    },

    formaterPeriode(periode) {
      const [annee, mois] = periode.split('-');
      const nomsMois = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Jun', 'Jul', 'Aoû', 'Sep', 'Oct', 'Nov', 'Déc'];
      return `${nomsMois[parseInt(mois) - 1]} ${annee}`;
    },

    creerGraphiquesContrats() {
      this.creerCamembertContrats();
      this.creerCourbeContrats();
    },

    creerCamembertContrats() {
      if (this.contratsPieChart) this.contratsPieChart.destroy();

      // Agréger les données de l'année
      const totalCDI = this.donneesContrats.reduce((sum, mois) => sum + mois.nombreCDI, 0);
      const totalCDD = this.donneesContrats.reduce((sum, mois) => sum + mois.nombreCDD, 0);
      const totalApprentissage = this.donneesContrats.reduce((sum, mois) => sum + mois.nombreApprentissage, 0);

      const ctx = this.$refs.contratsPieChart.getContext('2d');
      this.contratsPieChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: ['CDI', 'CDD', 'Apprentissage'],
          datasets: [{
            data: [totalCDI, totalCDD, totalApprentissage],
            backgroundColor: ['#2ecc71', '#e74c3c', '#3498db'],
            borderWidth: 2,
            borderColor: '#fff'
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'bottom' },
            tooltip: {
              callbacks: {
                label: function(context) {
                  const total = context.dataset.data.reduce((a, b) => a + b, 0);
                  const percentage = Math.round((context.raw / total) * 100);
                  return `${context.label}: ${context.raw} (${percentage}%)`;
                }
              }
            }
          }
        }
      });
    },

    creerCourbeContrats() {
      if (this.contratsLineChart) this.contratsLineChart.destroy();

      const ctx = this.$refs.contratsLineChart.getContext('2d');
      this.contratsLineChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: this.donneesContrats.map(m => this.formaterPeriode(m.periode)),
          datasets: [
            {
              label: 'CDI',
              data: this.donneesContrats.map(m => m.nombreCDI),
              borderColor: '#2ecc71',
              backgroundColor: 'rgba(46, 204, 113, 0.1)',
              tension: 0.4,
              fill: true
            },
            {
              label: 'CDD',
              data: this.donneesContrats.map(m => m.nombreCDD),
              borderColor: '#e74c3c',
              backgroundColor: 'rgba(231, 76, 60, 0.1)',
              tension: 0.4,
              fill: true
            },
            {
              label: 'Apprentissage',
              data: this.donneesContrats.map(m => m.nombreApprentissage),
              borderColor: '#3498db',
              backgroundColor: 'rgba(52, 152, 219, 0.1)',
              tension: 0.4,
              fill: true
            }
          ]
        },
        options: {
          responsive: true,
          interaction: {
            mode: 'index',
            intersect: false
          },
          scales: {
            y: {
              beginAtZero: true,
              title: { display: true, text: 'Nombre de contrats' }
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
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* Header principal amélioré */
.stats-container h1 {
  color: #2c3e50;
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 3px solid #4CAF50;
  text-align: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Section des filtres améliorée */
.filtres-section {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
  border-left: 5px solid #4CAF50;
}

.filtre-group {
  display: flex;
  align-items: center;
  gap: 15px;
}

.filtre-group label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.1rem;
  min-width: 80px;
}

/* Input/Select amélioré */
.select-filtre {
  padding: 12px 16px;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 1rem;
  background: white;
  color: #2c3e50;
  min-width: 150px;
  transition: all 0.3s ease;
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='m6 8 4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 12px center;
  background-repeat: no-repeat;
  background-size: 16px;
  appearance: none;
  cursor: pointer;
}

.select-filtre:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
  transform: translateY(-1px);
}

.select-filtre:hover {
  border-color: #bdc3c7;
}

/* Grid des graphiques */
.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 25px;
  margin-bottom: 30px;
}

.chart-card {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.15);
}

.chart-card h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
  font-size: 1.3rem;
  font-weight: 600;
  padding-bottom: 12px;
  border-bottom: 2px solid #ecf0f1;
}

.pie-chart-container,
.line-chart-container {
  height: 300px;
  position: relative;
}

/* Section tableau améliorée */
.table-section {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-top: 30px;
  border-left: 5px solid #3498db;
}

.table-section h3 {
  margin: 0 0 20px 0;
  color: #2c3e50;
  font-size: 1.3rem;
  font-weight: 600;
  padding-bottom: 12px;
  border-bottom: 2px solid #ecf0f1;
}

.table-container {
  overflow-x: auto;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  min-width: 800px;
}

.data-table th {
  background: linear-gradient(135deg, #34495e 0%, #2c3e50 100%);
  color: white;
  font-weight: 600;
  padding: 16px 12px;
  text-align: left;
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  position: sticky;
  top: 0;
}

.data-table th:last-child {
  border-right: none;
}

.data-table td {
  padding: 14px 12px;
  border-bottom: 1px solid #ecf0f1;
  color: #2c3e50;
}

.data-table tbody tr {
  transition: background-color 0.2s ease;
}

.data-table tbody tr:hover {
  background-color: #f8f9fa;
}

.data-table tbody tr:nth-child(even) {
  background-color: #fafbfc;
}

.data-table tbody tr:nth-child(even):hover {
  background-color: #f1f3f4;
}

.data-table .number {
  text-align: right;
  font-family: 'Courier New', Consolas, monospace;
  font-weight: 500;
  color: #2c3e50;
}

.data-table .percentage {
  text-align: right;
  color: #2c3e50;
  font-weight: 600;
  font-family: 'Courier New', Consolas, monospace;
}

/* États de chargement et erreur (pour cohérence) */
.loading-state {
  background: #e3f2fd;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #2196F3;
  margin: 15px 0;
  font-weight: 500;
}

.error-state {
  background: #ffebee;
  padding: 15px;
  border-radius: 8px;
  border-left: 4px solid #f44336;
  margin: 15px 0;
  font-weight: 500;
  color: #c62828;
}

/* Responsive */
@media (max-width: 1024px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-card {
    margin-bottom: 20px;
  }
}

@media (max-width: 768px) {
  .stats-container {
    padding: 15px;
  }
  
  .stats-container h1 {
    font-size: 1.8rem;
    text-align: left;
  }
  
  .filtres-section {
    padding: 20px;
  }
  
  .filtre-group {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .select-filtre {
    width: 100%;
  }
  
  .chart-card,
  .table-section {
    padding: 20px;
  }
  
  .data-table {
    font-size: 13px;
  }
  
  .data-table th,
  .data-table td {
    padding: 12px 8px;
  }
}

@media (max-width: 480px) {
  .stats-container h1 {
    font-size: 1.5rem;
  }
  
  .chart-card h3,
  .table-section h3 {
    font-size: 1.1rem;
  }
  
  .pie-chart-container,
  .line-chart-container {
    height: 250px;
  }
}
</style>