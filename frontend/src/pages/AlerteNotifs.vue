<template>
  <div class="alerts-container">
    <h1>🚨 Alertes & Notifications</h1>

    <!-- ALERTES CONTRATS -->
    <div class="alerts-section">
      <h2>📄 Fin de Contrat</h2>
      <div v-if="alertes.alertsContrats && alertes.alertsContrats.length > 0" class="alerts-grid">
        <div v-for="alerte in alertes.alertsContrats" :key="alerte.id" class="alert-card contrat">
          <div class="alert-icon">📅</div>
          <div class="alert-content">
            <h3>{{ alerte.employeNom }}</h3>
            <p>Contrat {{ alerte.typeContrat }} se termine le {{ formaterDate(alerte.dateFin) }}</p>
            <span class="alert-urgence">{{ alerte.joursRestants }} jours restants</span>
          </div>
        </div>
      </div>
      <div v-else class="no-alerts">
        <div class="no-alerts-icon">✅</div>
        <p>Aucune alerte de fin de contrat</p>
      </div>
    </div>

    <!-- ALERTES CONGÉS -->
    <div class="alerts-section">
      <h2>🏖️ Congés Non Pris</h2>
      <div v-if="alertes.alertsConges && alertes.alertsConges.length > 0" class="alerts-grid">
        <div v-for="alerte in alertes.alertsConges" :key="alerte.id" class="alert-card conges">
          <div class="alert-icon">⚠️</div>
          <div class="alert-content">
            <h3>{{ alerte.employeNom }}</h3>
            <p>{{ alerte.joursRestants }} jours de congés à utiliser</p>
            <span class="alert-urgence">Échéance: {{ formaterDate(alerte.dateEcheance) }}</span>
          </div>
        </div>
      </div>
      <div v-else class="no-alerts">
        <div class="no-alerts-icon">✅</div>
        <p>Aucune alerte de congés</p>
      </div>
    </div>

    <!-- STATISTIQUES ALERTES -->
    <div class="stats-alerts">
      <div class="stat-alert">
        <div class="stat-value">{{ totalAlertes }}</div>
        <div class="stat-label">Alertes actives</div>
      </div>
      <div class="stat-alert">
        <div class="stat-value">{{ alertesContratsCount }}</div>
        <div class="stat-label">Fin de contrat</div>
      </div>
      <div class="stat-alert">
        <div class="stat-value">{{ alertesCongesCount }}</div>
        <div class="stat-label">Congés non pris</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      alertes: {
        alertsContrats: [],
        alertsConges: []
      }
    }
  },
  async created() {
    await this.chargerAlertes();
    // Polling toutes les 5 minutes
    setInterval(this.chargerAlertes, 300000);
  },
  computed: {
    totalAlertes() {
      return (this.alertes.alertsContrats?.length || 0) + (this.alertes.alertsConges?.length || 0);
    },
    alertesContratsCount() {
      return this.alertes.alertsContrats?.length || 0;
    },
    alertesCongesCount() {
      return this.alertes.alertsConges?.length || 0;
    }
  },
  methods: {
    async chargerAlertes() {
      try {
        const response = await axios.get('/employes/alertes');
        this.alertes = response.data;
      } catch (error) {
        console.error('Erreur chargement alertes:', error);
      }
    },

    formaterDate(dateString) {
      return new Date(dateString).toLocaleDateString('fr-FR');
    }
  }
}
</script>

<style scoped>
.alerts-container {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.alerts-section {
  margin-bottom: 40px;
}

.alerts-section h2 {
  color: #2c3e50;
  border-bottom: 2px solid #e74c3c;
  padding-bottom: 10px;
  margin-bottom: 20px;
}

.alerts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.alert-card {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid;
  background: #f8f9fa;
  transition: all 0.3s ease;
}

.alert-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.alert-card.contrat {
  border-left-color: #e74c3c;
}

.alert-card.conges {
  border-left-color: #f39c12;
}

.alert-icon {
  font-size: 24px;
}

.alert-content h3 {
  margin: 0 0 8px 0;
  color: #2c3e50;
  font-size: 16px;
}

.alert-content p {
  margin: 0 0 8px 0;
  color: #5a6c7d;
  font-size: 14px;
}

.alert-urgence {
  font-size: 12px;
  color: #e74c3c;
  font-weight: 600;
  background: rgba(231, 76, 60, 0.1);
  padding: 4px 8px;
  border-radius: 4px;
}

.no-alerts {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
}

.no-alerts-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.stats-alerts {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 20px;
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid #ecf0f1;
}

.stat-alert {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #e74c3c;
  margin-bottom: 8px;
}

.stat-label {
  color: #7f8c8d;
  font-size: 14px;
}
</style>