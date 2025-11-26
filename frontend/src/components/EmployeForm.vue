<!-- FichePaieSearch.vue -->
<script>
export default {
  data() {
    return {
      idEmploye: "",
      mois: "",
      annee: new Date().getFullYear()
    };
  },
  methods: {
    goToResult() {
      this.$router.push(`/fiche-paie/resultat/${this.idEmploye}/${this.mois}/${this.annee}`);
    }
  }
};
</script>

<template>
  <div class="search-container">
    <div class="search-card">
      
      <div class="card-header">
        <div class="header-icon">🔍</div>
        <div class="header-text">
          <h2 class="card-title">Recherche de Fiche de Paie</h2>
          <p class="card-description">Entrez les informations de l'employé pour consulter sa fiche</p>
        </div>
      </div>

      <form @submit.prevent="goToResult" class="search-form">
        
        <div class="form-group">
          <label for="idEmploye" class="form-label">
            <span class="label-icon">👤</span>
            Identifiant Employé
          </label>
          <input 
            id="idEmploye"
            v-model="idEmploye" 
            type="text"
            class="form-input"
            placeholder="Ex: EMP001" 
            required 
          />
          <span class="input-hint">Saisissez l'ID unique de l'employé</span>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="mois" class="form-label">
              Mois
            </label>
            <select 
              id="mois"
              v-model="mois" 
              class="form-input form-select"
              required
            >
              <option value="" disabled>Sélectionner</option>
              <option value="1">Janvier</option>
              <option value="2">Février</option>
              <option value="3">Mars</option>
              <option value="4">Avril</option>
              <option value="5">Mai</option>
              <option value="6">Juin</option>
              <option value="7">Juillet</option>
              <option value="8">Août</option>
              <option value="9">Septembre</option>
              <option value="10">Octobre</option>
              <option value="11">Novembre</option>
              <option value="12">Décembre</option>
            </select>
          </div>

          <div class="form-group">
            <label for="annee" class="form-label">
              Année
            </label>
            <input 
              id="annee"
              v-model="annee" 
              type="number"
              class="form-input"
              placeholder="2025" 
              min="2000" 
              max="2100" 
              required 
            />
          </div>
        </div>

        <button type="submit" class="submit-button">
          <span class="button-text">Afficher la fiche de paie</span>
          <span class="button-arrow">→</span>
        </button>

      </form>
    </div>
  </div>
</template>

<style scoped>
.search-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem 1rem;
  min-height: 80vh;
  display: flex;
  align-items: center;
}

.search-card {
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 2.5rem;
  width: 100%;
  border: 1px solid #e1e5e9;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 2px solid #f1f3f4;
}

.header-icon {
  font-size: 2.5rem;
  background:  #4CAF50;
  border-radius: 12px;
  padding: 0.5rem;
}

.header-text h2 {
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
  font-size: 1.75rem;
  font-weight: 700;
}

.card-description {
  margin: 0;
  color: #7f8c8d;
  font-size: 1rem;
  line-height: 1.5;
}

.search-form {
  display: flex;
  flex-direction: column;
  gap: 1.75rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.75rem;
  font-weight: 600;
  color: #2c3e50;
  font-size: 1rem;
}

.label-icon {
  font-size: 1.2rem;
}

.form-input {
  padding: 1rem 1.25rem;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: white;
  font-family: inherit;
}

.form-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  transform: translateY(-1px);
}

.form-input::placeholder {
  color: #a0a4a8;
}

.form-select {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='m6 8 4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 1rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
  padding-right: 3rem;
  appearance: none;
}

.input-hint {
  font-size: 0.875rem;
  color: #7f8c8d;
  margin-top: 0.5rem;
  font-style: italic;
}

.submit-button {
  background:  #4CAF50;
  color: white;
  border: none;
  padding: 1.25rem 2rem;
  border-radius: 12px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: space-between;
  transition: all 0.3s ease;
  margin-top: 1rem;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.submit-button:active {
  transform: translateY(0);
}

.button-arrow {
  font-size: 1.2rem;
  transition: transform 0.3s ease;
}

.submit-button:hover .button-arrow {
  transform: translateX(4px);
}

/* Responsive */
@media (max-width: 768px) {
  .search-container {
    padding: 1rem;
  }
  
  .search-card {
    padding: 2rem 1.5rem;
  }
  
  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }
  
  .card-header {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }
  
  .header-icon {
    font-size: 2rem;
  }
  
  .header-text h2 {
    font-size: 1.5rem;
  }
}

@media (max-width: 480px) {
  .search-card {
    padding: 1.5rem 1rem;
  }
  
  .submit-button {
    padding: 1rem 1.5rem;
    font-size: 1rem;
  }
}
</style>