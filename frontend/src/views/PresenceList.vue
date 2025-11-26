<template>
  <div class="presence-container">
    <h2>Présences de l'employé</h2>

    <!-- Filtres -->
    <div class="filters-card">
      <div class="filter-group">
        <div class="filter-item">
          <label for="employeId">ID Employé</label>
          <input 
            id="employeId"
            v-model="employeId"
            type="text"
            placeholder="Ex: EMP001"
            @keyup.enter="loadPresence"
          />
        </div>

        <div class="filter-item">
          <label for="dateFilter">Filtrer par date</label>
          <select id="dateFilter" v-model="dateFilter" @change="applyDateFilter">
            <option value="all">Toutes les dates</option>
            <option value="today">Aujourd'hui</option>
            <option value="yesterday">Hier</option>
            <option value="week">Cette semaine</option>
            <option value="month">Ce mois</option>
            <option value="custom">Période personnalisée</option>
          </select>
        </div>

        <!-- Période personnalisée -->
        <div v-if="dateFilter === 'custom'" class="custom-date-range">
          <div class="filter-item">
            <label for="startDate">Du</label>
            <input 
              id="startDate"
              v-model="startDate"
              type="date"
              @change="applyDateFilter"
            />
          </div>
          <div class="filter-item">
            <label for="endDate">Au</label>
            <input 
              id="endDate"
              v-model="endDate"
              type="date"
              @change="applyDateFilter"
            />
          </div>
        </div>
      </div>

      <div class="action-buttons">
        <button @click="loadPresence" :disabled="loading" class="btn-primary">
          <span v-if="loading" class="spinner-small"></span>
          {{ loading ? 'Chargement...' : 'Afficher' }}
        </button>
        <button @click="clearFilter" class="btn-secondary" :disabled="loading">
          Effacer
        </button>
      </div>
    </div>

    <!-- Loader -->
    <div v-if="loading" class="loader">
      <div class="spinner"></div>
      Chargement des présences...
    </div>

    <!-- Message d'erreur -->
    <div v-if="error" class="error-message">
      <i class="error-icon">⚠</i>
      {{ error }}
    </div>

    <!-- Message si aucune donnée -->
    <div v-if="!loading && filteredPresences.length === 0 && employeId" class="empty">
      <i class="empty-icon">📭</i>
      Aucune présence trouvée pour l'employé {{ employeId }}
      <span v-if="dateFilter !== 'all'" class="filter-info">
        avec le filtre "{{ getDateFilterLabel() }}"
      </span>
    </div>

    <div v-if="!loading && filteredPresences.length === 0 && !employeId" class="empty">
      <i class="empty-icon">👤</i>
      Veuillez entrer un ID employé
    </div>

    <!-- Tableau des présences -->
    <div v-if="filteredPresences.length > 0" class="table-container">
      <table class="presences-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Mouvement</th>
            <th>Date</th>
            <th>Heure</th>
            <th>Jour</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="p in filteredPresences" :key="p.idPresence">
            <td class="id-cell">{{ p.idPresence }}</td>
            <td>
              <span 
                :class="['badge', p.mouvement === 'Entrée' ? 'in' : 'out']"
              >
                <i :class="p.mouvement === 'Entrée' ? 'in-icon' : 'out-icon'">
                  {{ p.mouvement === 'Entrée' ? '→' : '←' }}
                </i>
                {{ p.mouvement }}
              </span>
            </td>
            <td>{{ formatDate(p.date) }}</td>
            <td class="time-cell">{{ formatTime(p.date) }}</td>
            <td class="day-cell">{{ getDayName(p.date) }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Résumé et statistiques -->
    <div v-if="filteredPresences.length > 0" class="summary-card">
      <div class="stats-grid">
        <div class="stat-item">
          <span class="stat-number">{{ filteredPresences.length }}</span>
          <span class="stat-label">Présence(s)</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ getEntreesCount() }}</span>
          <span class="stat-label">Entrée(s)</span>
        </div>
        <div class="stat-item">
          <span class="stat-number">{{ getSortiesCount() }}</span>
          <span class="stat-label">Sortie(s)</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">Période</span>
          <span class="stat-period">{{ getDateFilterLabel() }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'PresenceList',
  data() {
    return {
      employeId: "",
      presences: [],
      filteredPresences: [],
      loading: false,
      error: null,
      dateFilter: "all",
      startDate: "",
      endDate: ""
    };
  },
  methods: {
    async loadPresence() {
      if (!this.employeId.trim()) {
        alert("Veuillez entrer un ID employé");
        return;
      }

      this.loading = true;
      this.presences = [];
      this.filteredPresences = [];
      this.error = null;

      try {
        const res = await axios.get(`/api/rh/presences/${this.employeId}`);
        console.log("Données reçues:", res.data);
        this.presences = res.data;
        this.applyDateFilter();
        
      } catch (err) {
        console.error("Erreur détaillée:", err);
        
        if (err.response) {
          this.error = `Erreur ${err.response.status}: ${err.response.data?.message || 'Serveur inaccessible'}`;
        } else if (err.request) {
          this.error = "Impossible de contacter le serveur. Vérifiez la connexion.";
        } else {
          this.error = "Erreur de configuration: " + err.message;
        }
        
        alert(this.error);
      } finally {
        this.loading = false;
      }
    },

    applyDateFilter() {
      if (this.presences.length === 0) {
        this.filteredPresences = [];
        return;
      }

      const now = new Date();
      let startDate, endDate;

      switch (this.dateFilter) {
        case 'today':
          startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
          endDate = new Date(now.getFullYear(), now.getMonth(), now.getDate() + 1);
          break;
        
        case 'yesterday':
          startDate = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1);
          endDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
          break;
        
        case 'week':
          const firstDayOfWeek = new Date(now.setDate(now.getDate() - now.getDay()));
          startDate = new Date(firstDayOfWeek.getFullYear(), firstDayOfWeek.getMonth(), firstDayOfWeek.getDate());
          endDate = new Date();
          break;
        
        case 'month':
          startDate = new Date(now.getFullYear(), now.getMonth(), 1);
          endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0);
          break;
        
        case 'custom':
          if (this.startDate && this.endDate) {
            startDate = new Date(this.startDate);
            endDate = new Date(this.endDate);
            endDate.setDate(endDate.getDate() + 1); // Inclure le jour de fin
          } else {
            this.filteredPresences = this.presences;
            return;
          }
          break;
        
        default: // 'all'
          this.filteredPresences = this.presences;
          return;
      }

      this.filteredPresences = this.presences.filter(presence => {
        const presenceDate = new Date(presence.date);
        return presenceDate >= startDate && presenceDate < endDate;
      });
    },

    clearFilter() {
      this.employeId = "";
      this.presences = [];
      this.filteredPresences = [];
      this.error = null;
      this.dateFilter = "all";
      this.startDate = "";
      this.endDate = "";
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      return new Date(dateString).toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    },

    formatTime(dateString) {
      if (!dateString) return 'N/A';
      return new Date(dateString).toLocaleTimeString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },

    getDayName(dateString) {
      if (!dateString) return 'N/A';
      return new Date(dateString).toLocaleDateString('fr-FR', { weekday: 'long' });
    },

    getEntreesCount() {
      return this.filteredPresences.filter(p => p.mouvement === 'Entrée').length;
    },

    getSortiesCount() {
      return this.filteredPresences.filter(p => p.mouvement === 'Sortie').length;
    },

    getDateFilterLabel() {
      const labels = {
        'all': 'Toutes les dates',
        'today': 'Aujourd\'hui',
        'yesterday': 'Hier',
        'week': 'Cette semaine',
        'month': 'Ce mois',
        'custom': `Du ${this.startDate} au ${this.endDate}`
      };
      return labels[this.dateFilter] || 'Filtre personnalisé';
    }
  },
  
  mounted() {
    // Initialiser les dates pour le filtre personnalisé
    const today = new Date();
    const oneWeekAgo = new Date(today);
    oneWeekAgo.setDate(today.getDate() - 7);
    
    this.startDate = oneWeekAgo.toISOString().split('T')[0];
    this.endDate = today.toISOString().split('T')[0];
  }
};
</script>

<style scoped>
.presence-container {
  background: white;
  padding: 24px;
  border-radius: 12px;
  max-width: 900px;
  margin: auto;
  margin-top: 20px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

h2 {
  color: #2c3e50;
  margin-bottom: 24px;
  text-align: center;
  font-weight: 600;
}

/* Filtres */
.filters-card {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
}

.filter-group {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 16px;
}

.filter-item {
  display: flex;
  flex-direction: column;
}

label {
  font-weight: 500;
  margin-bottom: 6px;
  color: #495057;
  font-size: 14px;
}

input, select {
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s;
}

input:focus, select:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.1);
}

.custom-date-range {
  grid-column: 1 / -1;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
  padding: 12px;
  background: #fff;
  border-radius: 6px;
  border: 1px solid #e9ecef;
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  gap: 6px;
}

.btn-primary {
  background: #3498db;
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: #2980b9;
  transform: translateY(-1px);
}

.btn-secondary {
  background: #6c757d;
  color: white;
}

.btn-secondary:hover:not(:disabled) {
  background: #5a6268;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none !important;
}

/* Loader et messages */
.loader {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px;
  color: #666;
}

.spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #f3f3f3;
  border-top: 2px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.spinner-small {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  background: #fee;
  color: #c33;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid #fcc;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 16px 0;
}

.empty {
  text-align: center;
  padding: 40px;
  color: #666;
  font-style: italic;
}

.empty-icon, .error-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.filter-info {
  display: block;
  font-size: 14px;
  color: #888;
  margin-top: 4px;
}

/* Tableau */
.table-container {
  overflow-x: auto;
  margin: 20px 0;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.presences-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
}

.presences-table th {
  background: #f8f9fa;
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
  font-size: 14px;
}

.presences-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  font-size: 14px;
}

.presences-table tr:hover {
  background: #f8f9fa;
}

/* Badges */
.badge {
  padding: 6px 12px;
  border-radius: 20px;
  color: white;
  font-size: 12px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.badge.in {
  background: #27ae60;
}

.badge.out {
  background: #e74c3c;
}

.id-cell {
  font-family: 'Courier New', monospace;
  font-weight: 600;
  color: #2c3e50;
}

.time-cell {
  font-family: 'Courier New', monospace;
  font-weight: 500;
}

.day-cell {
  text-transform: capitalize;
  color: #666;
}

/* Résumé */
.summary-card {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  margin-top: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
}

.stat-item {
  text-align: center;
  padding: 12px;
}

.stat-number {
  display: block;
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: #666;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-top: 4px;
}

.stat-period {
  display: block;
  font-size: 14px;
  color: #3498db;
  font-weight: 500;
  margin-top: 4px;
}

/* Responsive */
@media (max-width: 768px) {
  .presence-container {
    padding: 16px;
    margin: 10px;
  }
  
  .filter-group {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    justify-content: stretch;
  }
  
  button {
    flex: 1;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>