<template>
    <Navbar />

  <div class="container">

    <h2 class="title">📄 Générer rapport de performance</h2>

    <!-- FORMULAIRE -->
    <div class="card">
      <div class="form-row">
        <label>ID Employé</label>
        <input v-model="idEmploye" placeholder="Ex : EMP001" />
      </div>

      <div class="form-row">
        <label>Mois</label>
        <input v-model="month" type="number" min="1" max="12" placeholder="1 - 12" />
      </div>

      <div class="form-row">
        <label>Année</label>
        <input v-model="year" type="number" placeholder="2025" />
      </div>

      <div class="form-row">
        <label>Commentaire</label>
        <textarea v-model="commentaire" placeholder="(optionnel)"></textarea>
      </div>

      <button class="btn" @click="genererRapport">Générer</button>

      <p class="message">{{ message }}</p>
    </div>

   <!-- RESULTATS RAPPORT -->
   <div v-if="rapport" class="rapport-card">

      <h3 class="rapport-title">📘 Rapport généré</h3>

      <div class="profil-row">
        <img v-if="rapport.employe.imagePath" :src="rapport.employe.imagePath" :alt="`${rapport.employe.prenom} ${rapport.employe.nom}`" class="profil-img" />
        <div>
          <h4>{{ rapport.employe.prenom }} {{ rapport.employe.nom }}</h4>
          <p class="periode">📅 Période : {{ rapport.periode }}</p>
          <p class="small-info">🆔 ID Rapport : {{ rapport.idRapport }}</p>
        </div>
      </div>

      <!-- Infos employé -->
      <div class="info-grid">
        <div><strong>Sexe :</strong> {{ rapport.employe.sexe.libelle }}</div>
        <div><strong>Contact :</strong> {{ rapport.employe.contact }}</div>
        <div><strong>Email :</strong> {{ rapport.employe.mail }}</div>
        <div><strong>Adresse :</strong> {{ rapport.employe.adresse }}</div>
        <div><strong>Date de naissance :</strong> {{ rapport.employe.dateNaissance }}</div>
      </div>

      <!-- KPI -->
      <div class="kpi-grid">
        <div class="kpi-item">
          <h5>Productivité</h5>
          <p>{{ rapport.scoreProductivite }}</p>
        </div>
        <div class="kpi-item">
          <h5>Efficacité</h5>
          <p>{{ rapport.scoreEfficacite }}</p>
        </div>
        <div class="kpi-item">
          <h5>Qualité</h5>
          <p>{{ rapport.scoreQualite }}</p>
        </div>
      </div>

      <!-- Commentaire -->
      <div class="comment-section" v-if="rapport.commentaire">
        <h5>Commentaire</h5>
        <p>{{ rapport.commentaire }}</p>
      </div>

      <p class="generated-date">
        🕒 Généré le : {{ rapport.dateGeneration }}
      </p>

      <!-- SUGGESTIONS DE FORMATION -->
      <div v-if="suggestions.length" class="suggestion-card">
        <h3 class="rapport-title">💡 Suggestions de formation</h3>
        <ul>
          <li v-for="f in suggestions" :key="f.idFormation">
            <strong>{{ f.titre }}</strong> ({{ f.dureeHeures }} h) - {{ f.description }}
          </li>
        </ul>
      </div>

   </div>
</div>   

</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import Navbar from "@/pages/NavBarCompetence.vue"; 

const idEmploye = ref("");
const month = ref("");
const year = ref("");
const commentaire = ref("");
const message = ref("");
const rapport = ref(null);
const suggestions = ref([]);

const genererRapport = async () => {
  if (!idEmploye.value || !month.value || !year.value) {
    message.value = "Veuillez remplir tous les champs obligatoires.";
    return;
  }

  try {
    // Génération du rapport
    const res = await axios.post(
      "http://localhost:8085/api/rapport/generer",
      null,
      {
        params: {
          idEmploye: idEmploye.value,
          month: month.value,
          year: year.value,
          commentaire: commentaire.value || null,
        },
      }
    );

    rapport.value = res.data;
    message.value = "Rapport généré avec succès !";

    // Récupérer les suggestions pour cet employé et poste
    const posteCode = "P-DEVJR"; // ou à adapter dynamiquement
    const suggRes = await axios.get(`http://localhost:8085/api/competence/suggest/${idEmploye.value}/${posteCode}`);
    suggestions.value = suggRes.data;

  } catch (error) {
    console.error(error);
    message.value = "Erreur lors de la génération.";
  }
};
</script>

<style scoped>
.container { width: 70%; margin: auto; padding: 20px; } .title { text-align: center; margin-bottom: 25px; font-size: 26px; font-weight: bold; } /*** FORMULAIRE ***/ .card { background: #ffffff; padding: 20px; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); margin-bottom: 25px; } .form-row { margin-bottom: 15px; display: flex; flex-direction: column; } .form-row label { font-weight: bold; margin-bottom: 5px; } input, textarea { width: 100%; padding: 8px 10px; border-radius: 6px; border: 1px solid #ccc; } .btn { width: 100%; padding: 12px; background: #2a7ae4; color: white; font-size: 16px; border-radius: 8px; cursor: pointer; margin-top: 10px; } .btn:hover { background: #1d5ebd; } .message { margin-top: 10px; font-weight: bold; text-align: center; } /*** RAPPORT ***/ .rapport-card { background: #fff; padding: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); } .rapport-title { text-align: center; margin-bottom: 15px; } .profil-row { display: flex; align-items: center; gap: 15px; } .profil-img { width: 80px; height: 80px; border-radius: 50%; object-fit: cover; border: 2px solid #ddd; } .periode { font-size: 14px; color: #555; } /*** KPI GRID ***/ .kpi-grid { display: flex; justify-content: space-between; margin-top: 20px; } .kpi-item { background: #f8f9fc; padding: 15px; border-radius: 10px; width: 30%; text-align: center; } .kpi-item h5 { margin-bottom: 5px; } .kpi-item p { font-size: 22px; font-weight: bold; color: #2a7ae4; } /*** COMMENTAIRE ***/ .comment-section { margin-top: 20px; } 
.generated-date { margin-top: 15px; text-align: right; font-size: 13px; color: gray;}

.suggestion-card {
  background: #f1f9ff;
  padding: 15px;
  border-radius: 10px;
  margin-top: 20px;
  box-shadow: 0 3px 10px rgba(0,0,0,0.05);
}

.suggestion-card ul {
  list-style: none;
  padding-left: 0;
}

.suggestion-card li {
  margin-bottom: 10px;
  line-height: 1.4;
}
</style>
