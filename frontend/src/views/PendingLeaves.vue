<template>
  <div class="container-fluid px-4 py-4">
    <!-- En-tête avec titre, statistiques et filtres -->
    <div class="row mb-4">
      <div class="col-md-8">
        <div class="d-flex align-items-center mb-3">
          <div class="header-icon">
            <i class="bi bi-clock-history"></i>
          </div>
          <div>
            <h1 class="page-title mb-1">Validation des Congés</h1>
            <p class="page-subtitle mb-0">
              Gérez et validez les demandes de congés en attente
            </p>
          </div>
        </div>

        <!-- Filtres rapides -->
        <div class="filters-section">
          <div class="filter-tags">
            <button v-for="filter in quickFilters" :key="filter.id" class="filter-tag"
              :class="{ active: activeFilter === filter.id }" @click="applyQuickFilter(filter.id)">
              <i :class="filter.icon"></i>
              {{ filter.label }}
            </button>
          </div>
        </div>
      </div><br>

      <div class="col-md-4">
        <div class="stats-cards">
          <div class="stat-card pending">
            <div class="stat-icon">
              <i class="bi bi-clock"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ conges.length }}</div>
              <div class="stat-label">En attente</div>
            </div>
          </div>
          <div class="stat-card urgent">
            <div class="stat-icon">
              <i class="bi bi-exclamation-triangle"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ urgentCount }}</div>
              <div class="stat-label">Urgent</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Carte principale avec tableau -->
    <div class="main-card">
      <div class="card-header-custom">
        <div class="d-flex justify-content-between align-items-center flex-wrap gap-3">
          <div class="d-flex align-items-center gap-3">
            <h3 class="card-title-custom mb-0">
              <i class="bi bi-list-check me-2"></i>
              Demandes en attente
            </h3>
            <div class="search-box">
              <i class="bi bi-search search-icon"></i>
              <input v-model="searchQuery" type="text" placeholder="Rechercher un employé..." class="search-input">
            </div>
          </div>

          <div class="d-flex gap-2 flex-wrap">
            <div class="dropdown">
              <button class="btn btn-outline-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown">
                <i class="bi bi-filter me-2"></i>
                Filtres
              </button>
              <div class="dropdown-menu dropdown-menu-end p-3" style="min-width: 250px;">
                <div class="mb-3">
                  <label class="form-label small fw-semibold">Type de congé</label>
                  <select v-model="typeFilter" class="form-select form-select-sm">
                    <option value="all">Tous les types</option>
                    <option v-for="type in typeOptions" :key="type" :value="type">
                      {{ type }}
                    </option>
                  </select>
                </div>
                <div class="mb-3">
                  <label class="form-label small fw-semibold">Période</label>
                  <select v-model="periodFilter" class="form-select form-select-sm">
                    <option value="all">Toutes périodes</option>
                    <option value="thisWeek">Cette semaine</option>
                    <option value="nextWeek">Semaine prochaine</option>
                    <option value="thisMonth">Ce mois</option>
                  </select>
                </div>
                <button class="btn btn-sm btn-outline-secondary w-100" @click="clearFilters">
                  <i class="bi bi-arrow-clockwise me-1"></i>
                  Réinitialiser
                </button>
              </div>
            </div>

            <button class="btn btn-primary" @click="loadConges" :disabled="loading">
              <i class="bi bi-arrow-clockwise" :class="{ 'spin': loading }"></i>
              Actualiser
            </button>
          </div>
        </div>
      </div>

      <div class="card-body-custom p-0">
        <!-- Tableau -->
        <div class="table-container">
          <table class="custom-table">
            <thead>
              <tr>
                <th class="column-check">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" :checked="allSelected" @change="toggleAllSelection">
                  </div>
                </th>
                <th class="column-employee">
                  <span>Employé</span>
                </th>
                <th class="column-type">
                  <span>Type</span>
                </th>
                <th class="column-period">
                  <span>Période</span>
                </th>
                <th class="column-duration">
                  <span>Durée</span>
                </th>
                <th class="column-status">
                  <span>Statut</span>
                </th>
                <th class="column-actions">
                  <span>Actions</span>
                </th>
              </tr>
            </thead>

            <tbody>
              <!-- État de chargement -->
              <tr v-if="loading">
                <td colspan="7" class="text-center py-5">
                  <div class="loading-state">
                    <div class="spinner-border text-primary" role="status">
                      <span class="visually-hidden">Chargement...</span>
                    </div>
                    <p class="mt-3 text-muted">Chargement des demandes...</p>
                  </div>
                </td>
              </tr>

              <!-- État vide -->
              <tr v-else-if="filteredConges.length === 0">
                <td colspan="7" class="text-center py-5">
                  <div class="empty-state">
                    <i class="bi bi-check-circle-fill text-success empty-icon"></i>
                    <h5 class="mt-3 text-muted">Aucune demande en attente</h5>
                    <p class="text-muted mb-0">Toutes les demandes ont été traitées</p>
                  </div>
                </td>
              </tr>

              <!-- Liste des congés -->
              <tr v-for="c in filteredConges" :key="c.idConge" class="table-row" :class="{ 
                  'urgent-row': isUrgent(c),
                  'selected': selectedItems.includes(c.idConge)
                }">
                <td class="column-check">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" :checked="selectedItems.includes(c.idConge)"
                      @change="toggleSelection(c.idConge)">
                  </div>
                </td>

                <td class="column-employee">
                  <div class="employee-info">
                    <div class="avatar-wrapper">
                      <div class="avatar">
                        {{ getInitials(c.employeNom, c.employePrenom) }}
                      </div>
                      <div v-if="isUrgent(c)" class="urgent-indicator" title="Demande urgente"></div>
                    </div>
                    <div class="employee-details">
                      <div class="employee-name">
                        {{ c.employePrenom }} {{ c.employeNom }}
                      </div>
                      <div class="employee-id text-muted">
                        #{{ c.idEmploye || c.idConge }}
                      </div>
                    </div>
                  </div>
                </td>

                <td class="column-type">
                  <span class="type-badge" :class="getTypeClass(c.typeConge)">
                    <i :class="getTypeIcon(c.typeConge)"></i>
                    {{ c.typeConge }}
                  </span>
                </td>

                <td class="column-period">
                  <div class="date-range">
                    <div class="date-start">
                      <i class="bi bi-calendar-plus me-1"></i>
                      {{ formatDate(c.dateDebut) }}
                    </div>
                    <div class="date-connector">
                      <i class="bi bi-arrow-right"></i>
                    </div>
                    <div class="date-end">
                      <i class="bi bi-calendar-minus me-1"></i>
                      {{ formatDate(c.dateFin) }}
                    </div>
                  </div>
                  <div v-if="isUrgent(c)" class="urgent-badge">
                    <i class="bi bi-exclamation-circle me-1"></i>
                    Débute bientôt
                  </div>
                </td>

                <td class="column-duration">
                  <div class="duration-info">
                    <span class="duration-days">
                      {{ calculateDuration(c.dateDebut, c.dateFin) }} jour(s)
                    </span>
                    <div class="progress duration-progress">
                      <div class="progress-bar" :class="getProgressBarClass(c)"
                        :style="{ width: getProgressBarWidth(c) + '%' }"></div>
                    </div>
                  </div>
                </td>

                <td class="column-status">
                  <span class="status-badge" :class="getStatusClass(c.statut)">
                    <i class="bi bi-clock me-1"></i>
                    {{ getStatutLabel(c.statut) }}
                  </span>
                </td>

                <td class="column-actions">
                  <div class="actions-container">
                    <div class="btn-group" role="group">
                      <button class="btn btn-success btn-action" @click="changerStatut(c.idConge, 'STAT-APP')"
                        :disabled="processing === c.idConge" title="Valider cette demande">
                        <i class="bi bi-check-lg"></i>
                        <span class="btn-text">Valider</span>
                      </button><br><br>

                      <button class="btn btn-outline-danger btn-action" @click="changerStatut(c.idConge, 'STAT-REJ')"
                        :disabled="processing === c.idConge" title="Rejeter cette demande">
                        <i class="bi bi-x-lg"></i>
                        <span class="btn-text">Rejeter</span>
                      </button>
                    </div>

                    <!-- Menu d'actions supplémentaires -->
                    

                    <!-- Indicateur de traitement -->
                    <div v-if="processing === c.idConge" class="processing-indicator">
                      <div class="spinner-border spinner-border-sm text-primary" role="status"></div>
                    </div>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Pied de page avec actions groupées -->
      <div v-if="selectedItems.length > 0" class="card-footer-custom">
        <div class="bulk-actions">
          <div class="bulk-info">
            <strong>{{ selectedItems.length }}</strong> demande(s) sélectionnée(s)
          </div>
          <div class="bulk-buttons">
            <button class="btn btn-success btn-sm" @click="bulkApprove" :disabled="processing">
              <i class="bi bi-check-all me-1"></i>
              Tout approuver
            </button>
            <button class="btn btn-outline-danger btn-sm" @click="bulkReject" :disabled="processing">
              <i class="bi bi-x-circle me-1"></i>
              Tout rejeter
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Barre de progression pour actions groupées -->
    <div v-if="bulkProcessing" class="bulk-progress-container">
      <div class="bulk-progress">
        <div class="progress">
          <div class="progress-bar progress-bar-striped progress-bar-animated" :style="{ width: bulkProgress + '%' }">
          </div>
        </div>
        <div class="bulk-progress-text">
          Traitement en cours... {{ bulkProgress }}%
        </div>
      </div>
    </div>

    <!-- Toast de notification -->
    <div class="toast-container position-fixed top-0 end-0 p-3">
      <div id="notificationToast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <i class="bi me-2" :class="toastIcon"></i>
          <strong class="me-auto">{{ toastTitle }}</strong>
          <small class="text-muted">à l'instant</small>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          {{ toastMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import axios from "axios";

  export default {
    name: "PendingLeaves",

    data() {
      return {
        conges: [],
        loading: false,
        processing: null,
        bulkProcessing: false,
        bulkProgress: 0,
        selectedItems: [],
        searchQuery: "",
        typeFilter: "all",
        periodFilter: "all",
        activeFilter: "all",
        toastTitle: "",
        toastMessage: "",
        toastIcon: "",

        quickFilters: [
          { id: "all", label: "Toutes", icon: "bi bi-grid" },
          { id: "urgent", label: "Urgent", icon: "bi bi-exclamation-triangle" },
          { id: "thisWeek", label: "Cette semaine", icon: "bi bi-calendar-week" },
          { id: "long", label: "Longue durée", icon: "bi bi-calendar-range" }
        ],

        typeOptions: ["Congé payé", "RTT", "Congé maladie", "Congé exceptionnel", "Formation"]
      }
    },

    computed: {
      filteredConges() {
        let filtered = this.conges;

        // Filtre de recherche
        if (this.searchQuery) {
          const query = this.searchQuery.toLowerCase();
          filtered = filtered.filter(c =>
            c.employeNom.toLowerCase().includes(query) ||
            c.employePrenom.toLowerCase().includes(query) ||
            c.idConge.toString().includes(query)
          );
        }

        // Filtre par type
        if (this.typeFilter !== "all") {
          filtered = filtered.filter(c => c.typeConge === this.typeFilter);
        }

        // Filtres rapides
        if (this.activeFilter === "urgent") {
          filtered = filtered.filter(c => this.isUrgent(c));
        } else if (this.activeFilter === "thisWeek") {
          filtered = filtered.filter(c => this.isThisWeek(c.dateDebut));
        } else if (this.activeFilter === "long") {
          filtered = filtered.filter(c => this.calculateDuration(c.dateDebut, c.dateFin) > 10);
        }

        return filtered;
      },

      allSelected() {
        return this.filteredConges.length > 0 &&
          this.selectedItems.length === this.filteredConges.length;
      },

      urgentCount() {
        return this.conges.filter(c => this.isUrgent(c)).length;
      }
    },

    mounted() {
      this.loadConges();
    },

    methods: {
      async loadConges() {
        this.loading = true;
        try {
          const response = await axios.get("http://localhost:8085/api/rh/attente/details");
          this.conges = response.data;
        } catch (err) {
          console.error(err);
          this.showToast("Erreur", "Impossible de charger les demandes", "bi-exclamation-triangle-fill text-danger");
        } finally {
          this.loading = false;
        }
      },

      async changerStatut(idConge, statutId) {
        this.processing = idConge;
        try {
          await axios.post(`http://localhost:8085/api/rh/conge/${idConge}/statut?statutId=${statutId}`);

          if (statutId === 'STAT-APP') {
            this.showToast("Succès", "Demande validée avec succès", "bi-check-circle-fill text-success");
          } else {
            this.showToast("Succès", "Demande rejetée avec succès", "bi-x-circle-fill text-danger");
          }

          this.loadConges();
          this.selectedItems = this.selectedItems.filter(id => id !== idConge);
        } catch (err) {
          console.error(err);
          this.showToast("Erreur", "Erreur lors du traitement", "bi-exclamation-triangle-fill text-danger");
        } finally {
          this.processing = null;
        }
      },

      async bulkApprove() {
        await this.bulkAction(this.selectedItems, 'STAT-APP', "Toutes les demandes ont été approuvées");
      },

      async bulkReject() {
        await this.bulkAction(this.selectedItems, 'STAT-REJ', "Toutes les demandes ont été rejetées");
      },

      async bulkAction(ids, statutId, successMessage) {
        this.bulkProcessing = true;
        this.bulkProgress = 0;

        try {
          const total = ids.length;
          for (let i = 0; i < ids.length; i++) {
            await axios.post(`http://localhost:8085/api/rh/conge/${ids[i]}/statut?statutId=${statutId}`);
            this.bulkProgress = Math.round(((i + 1) / total) * 100);

            // Petit délai pour voir la progression
            await new Promise(resolve => setTimeout(resolve, 200));
          }

          this.showToast("Succès", successMessage, "bi-check-circle-fill text-success");
          this.loadConges();
          this.selectedItems = [];
        } catch (err) {
          console.error(err);
          this.showToast("Erreur", "Erreur lors du traitement groupé", "bi-exclamation-triangle-fill text-danger");
        } finally {
          this.bulkProcessing = false;
          this.bulkProgress = 0;
        }
      },

      toggleSelection(id) {
        const index = this.selectedItems.indexOf(id);
        if (index > -1) {
          this.selectedItems.splice(index, 1);
        } else {
          this.selectedItems.push(id);
        }
      },

      toggleAllSelection() {
        if (this.allSelected) {
          this.selectedItems = [];
        } else {
          this.selectedItems = this.filteredConges.map(c => c.idConge);
        }
      },

      applyQuickFilter(filterId) {
        this.activeFilter = filterId;
      },

      clearFilters() {
        this.searchQuery = "";
        this.typeFilter = "all";
        this.periodFilter = "all";
        this.activeFilter = "all";
      },

      isUrgent(conge) {
        const startDate = new Date(conge.dateDebut);
        const today = new Date();
        const diffTime = startDate - today;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays <= 2 && diffDays >= 0;
      },

      isThisWeek(dateString) {
        const date = new Date(dateString);
        const today = new Date();
        const startOfWeek = new Date(today.setDate(today.getDate() - today.getDay()));
        const endOfWeek = new Date(startOfWeek);
        endOfWeek.setDate(endOfWeek.getDate() + 6);
        return date >= startOfWeek && date <= endOfWeek;
      },

      calculateDuration(start, end) {
        const startDate = new Date(start);
        const endDate = new Date(end);
        const diffTime = Math.abs(endDate - startDate);
        return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      },

      getProgressBarWidth(conge) {
        const startDate = new Date(conge.dateDebut);
        const endDate = new Date(conge.dateFin);
        const today = new Date();

        if (today < startDate) return 0;
        if (today > endDate) return 100;

        const totalDuration = endDate - startDate;
        const elapsed = today - startDate;
        return Math.min(100, Math.max(0, (elapsed / totalDuration) * 100));
      },

      getProgressBarClass(conge) {
        const progress = this.getProgressBarWidth(conge);
        if (progress === 0) return 'bg-secondary';
        if (progress < 100) return 'bg-warning';
        return 'bg-success';
      },

      getInitials(nom, prenom) {
        return ((prenom?.[0] || '') + (nom?.[0] || '')).toUpperCase();
      },

      formatDate(dateString) {
        if (!dateString) return '';
        return new Date(dateString).toLocaleDateString('fr-FR', {
          day: '2-digit',
          month: '2-digit',
          year: 'numeric'
        });
      },

      getStatutLabel(statut) {
        const statuts = {
          'STAT-ATT': 'En attente',
          'STAT-APP': 'Approuvé',
          'STAT-REJ': 'Rejeté'
        };
        return statuts[statut] || statut;
      },

      getStatusClass(statut) {
        const classes = {
          'STAT-ATT': 'status-pending',
          'STAT-APP': 'status-approved',
          'STAT-REJ': 'status-rejected'
        };
        return classes[statut] || 'status-pending';
      },

      getTypeClass(type) {
        const classes = {
          'Congé payé': 'type-paid',
          'RTT': 'type-rtt',
          'Congé maladie': 'type-sick',
          'Congé exceptionnel': 'type-special',
          'Formation': 'type-training'
        };
        return classes[type] || 'type-default';
      },

      getTypeIcon(type) {
        const icons = {
          'Congé payé': 'bi bi-umbrella',
          'RTT': 'bi bi-clock',
          'Congé maladie': 'bi bi-heart-pulse',
          'Congé exceptionnel': 'bi bi-star',
          'Formation': 'bi bi-mortarboard'
        };
        return icons[type] || 'bi bi-calendar';
      },

      viewDetails(conge) {
        // Implémentez la logique pour voir les détails
        console.log('Voir détails:', conge);
        this.showToast("Info", "Fonctionnalité à implémenter", "bi-info-circle-fill text-info");
      },

      contactEmployee(conge) {
        // Implémentez la logique de contact
        console.log('Contacter:', conge);
        this.showToast("Info", "Fonctionnalité à implémenter", "bi-info-circle-fill text-info");
      },

      showToast(title, message, icon) {
        this.toastTitle = title;
        this.toastMessage = message;
        this.toastIcon = icon;

        // Méthode alternative sans Bootstrap
        const toastElement = document.getElementById('notificationToast');
        if (toastElement) {
          toastElement.classList.add('show');
          toastElement.style.display = 'block';

          // Auto-hide après 5 secondes
          setTimeout(() => {
            toastElement.classList.remove('show');
            toastElement.style.display = 'none';
          }, 5000);
        }
      }
    }
  }
</script>

<style scoped>
/* Version minimale avec meilleure visibilité */
.page-title {
  color: #000;
  font-weight: bold;
  font-size: 1.5rem;
}

.page-subtitle {
  color: #666;
  font-size: 0.9rem;
}

.header-icon {
  background: #007bff;
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}

.header-icon i {
  color: white;
  font-size: 1.2rem;
}

/* Statistiques */
.stats-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 12px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-left: 4px solid #ffc107;
}

.stat-card.urgent {
  border-left-color: #dc3545;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #ffc107;
  color: white;
}

.stat-card.urgent .stat-icon {
  background: #dc3545;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #000;
}

.stat-label {
  font-size: 0.8rem;
  color: #666;
  font-weight: 500;
}

/* Filtres */
.filter-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-tag {
  background: white;
  border: 2px solid #ddd;
  border-radius: 20px;
  padding: 6px 12px;
  font-size: 0.8rem;
  color: #666;
}

.filter-tag.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

/* Carte principale */
.main-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  margin-top: 16px;
}

.card-header-custom {
  padding: 16px;
  border-bottom: 2px solid #eee;
}

.card-title-custom {
  color: #000;
  font-weight: bold;
  font-size: 1.1rem;
}

/* Recherche */
.search-box {
  position: relative;
}

.search-icon {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
}

.search-input {
  padding: 8px 8px 8px 32px;
  border: 2px solid #ddd;
  border-radius: 6px;
  width: 200px;
}

.search-input:focus {
  border-color: #007bff;
  outline: none;
}

/* Boutons */
.btn {
  border-radius: 6px;
  font-weight: 500;
  padding: 8px 12px;
}

.btn-primary {
  background: #007bff;
  border: none;
  color: white;
}

/* Tableau */
.table-container {
  overflow-x: auto;
}

.custom-table {
  width: 100%;
  border-collapse: collapse;
}

.custom-table th {
  background: #f8f9fa;
  padding: 12px;
  text-align: left;
  font-weight: bold;
  color: #000;
  border-bottom: 2px solid #ddd;
}

.custom-table td {
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.table-row:hover {
  background: #f8f9fa;
}

/* Cases à cocher */
.form-check-input {
  width: 18px;
  height: 18px;
}

/* Employé */
.employee-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  background: #007bff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 0.8rem;
}

.employee-name {
  font-weight: bold;
  color: #000;
}

.employee-id {
  font-size: 0.8rem;
  color: #666;
}

/* Badges */
.type-badge {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

.type-paid { background: #e3f2fd; color: #1976d2; }
.type-rtt { background: #f3e5f5; color: #7b1fa2; }
.type-sick { background: #ffebee; color: #c62828; }

.status-badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-pending { background: #fff3cd; color: #856404; }

/* Période */
.date-range {
  display: flex;
  align-items: center;
  gap: 8px;
}

.date-start,
.date-end {
  font-weight: 500;
}

/* Actions */
.actions-container {
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-action {
  padding: 6px 10px;
  font-size: 0.8rem;
  border-radius: 4px;
}

.btn-success {
  background: #28a745;
  color: white;
  border: none;
}

.btn-outline-danger {
  border: 1px solid #dc3545;
  color: #dc3545;
  background: white;
}

/* État vide */
.empty-state {
  padding: 40px;
  text-align: center;
  color: #666;
}

.empty-icon {
  font-size: 2rem;
  color: #28a745;
}

/* Toast */
.toast {
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

/* Responsive */
@media (max-width: 768px) {
  .search-input {
    width: 150px;
  }
  
  .btn-text {
    display: none;
  }
  
  .date-range {
    flex-direction: column;
    gap: 2px;
  }
}
</style>