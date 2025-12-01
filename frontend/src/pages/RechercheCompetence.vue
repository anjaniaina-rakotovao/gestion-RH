<template>
    <Navbar />

  <div class="container">


    <h2 class="title">🔍 Rechercher employés par compétence</h2>

    <!-- FORMULAIRE DE RECHERCHE -->
    <div class="card">
      <div class="form-row">
        <label>Code Poste</label>
        <input v-model="posteCode" placeholder="Ex : P-DEVSR" />
      </div>
      <button class="btn" @click="searchByCompetence">Rechercher</button>
    </div>

    <!-- LISTE DES EMPLOYÉS -->
    <div v-if="matchingEmployes.length" class="rapport-card">
      <h3 class="rapport-title">👥 Employés correspondant à la compétence</h3>

      <div v-for="emp in matchingEmployes" :key="emp.idEmploye" class="profil-row">
           <img v-if="emp.imagePath" :src="emp.imagePath" :alt="`${emp.prenom} ${emp.nom}`" class="profil-img" />
        <div>
          <h4>{{ emp.prenom }} {{ emp.nom }}</h4>
          <p>📧 {{ emp.mail }}</p>
          <p>📞 {{ emp.contact }}</p>
          <p>🏠 {{ emp.adresse }}</p>
        </div>
      </div>
    </div>

    <p v-else-if="posteCode" class="message">Aucun employé trouvé pour ce poste.</p>

  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import Navbar from "@/pages/NavBarCompetence.vue"; 


const posteCode = ref("");
const matchingEmployes = ref([]);

const searchByCompetence = async () => {
  if (!posteCode.value) {
    alert("Veuillez entrer un code de poste");
    return;
  }

  try {
    const res = await axios.get(`http://localhost:8085/api/competence/matching/${posteCode.value}`);
    matchingEmployes.value = res.data;
  } catch (err) {
    console.error(err);
    alert("Erreur lors de la recherche");
  }
};
</script>

<style scoped>
.container {
  width: 70%;
  margin: auto;
  padding: 20px;
}

.title {
  text-align: center;
  margin-bottom: 25px;
  font-size: 26px;
  font-weight: bold;
}

/* FORMULAIRE */
.card {
  background: #ffffff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  margin-bottom: 25px;
}

.form-row {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.form-row label {
  font-weight: bold;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.btn {
  width: 100%;
  padding: 12px;
  background: #2a7ae4;
  color: white;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 10px;
}

.btn:hover {
  background: #1d5ebd;
}

.message {
  margin-top: 10px;
  font-weight: bold;
  text-align: center;
}

/* LISTE EMPLOYÉS */
.rapport-card {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

.rapport-title {
  text-align: center;
  margin-bottom: 15px;
}

.profil-row {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.profil-img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ddd;
}
</style>
