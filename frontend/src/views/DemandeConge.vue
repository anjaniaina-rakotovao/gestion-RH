<template>
  <div class="page-container">

    <div class="card">
      <h2>Demander un congé</h2>

      <!-- ID Employé -->
      <label>ID Employé</label>
      <input type="text" v-model="employeId" placeholder="Ex: EMP001">

      <!-- Type de congé -->
      <label>Type de congé</label>
      <select v-model="typeCongeId">
        <option disabled value="">-- Choisir --</option>
        <option v-for="t in types" :key="t.id" :value="t.id">
          {{ t.libelle }}
        </option>
      </select>

      <!-- Dates -->
      <label>Date début</label>
      <input type="date" v-model="dateDebut">

      <label>Date fin</label>
      <input type="date" v-model="dateFin">

      <button class="btn" @click="envoyerDemande">Envoyer demande</button>

      <p v-if="success" class="success">{{ success }}</p>
      <p v-if="error" class="error">{{ error }}</p>
    </div>

  </div>
</template>

<script>
  import rhService from "../services/rhService.js";

  export default {
    data() {
      return {
        employeId: "",
        typeCongeId: "",
        dateDebut: "",
        dateFin: "",
        success: "",
        error: "",
        types: [
          { id: "TCG-ANN", libelle: "Congé annuel payé" },
          { id: "TCG-MAL", libelle: "Congé maladie" },
          { id: "TCG-MAT", libelle: "Congé maternité" }
        ]
      };
    },

    methods: {
      async envoyerDemande() {
        if (!this.employeId || !this.typeCongeId || !this.dateDebut || !this.dateFin) {
          this.error = "Veuillez remplir tous les champs.";
          this.success = "";
          return;
        }

        try {
          await rhService.postConge({
            employeId: this.employeId,
            typeCongeId: this.typeCongeId,
            dateDebut: this.dateDebut,
            dateFin: this.dateFin
          });


          this.success = "Demande envoyée avec succès !";
          this.error = "";

          // reset
          this.dateDebut = "";
          this.dateFin = "";
          this.typeCongeId = "";
          this.employeId = "";

        } catch (e) {
          console.error(e);
          this.error = "Erreur lors de l’envoi.";
          this.success = "";
        }
      }
    }
  };
</script>

<style scoped>
  .page-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .card {
    width: 350px;
    padding: 20px;
    background: white;
    border-radius: 6px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
  }

  .card h2 {
    margin-bottom: 15px;
  }

  label {
    margin-top: 10px;
    display: block;
    font-weight: bold;
  }

  input,
  select {
    width: 100%;
    padding: 8px;
    margin-top: 5px;
    margin-bottom: 10px;
  }

  .btn {
    width: 100%;
    padding: 10px;
    background: #3498db;
    color: white;
    border: none;
    cursor: pointer;
  }

  .btn:hover {
    background: #2980b9;
  }

  .success {
    color: green;
    margin-top: 10px;
  }

  .error {
    color: red;
    margin-top: 10px;
  }
</style>