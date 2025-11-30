<template>
  <div class="p-6 max-w-xl mx-auto">
    <h1 class="text-2xl font-bold mb-4">Générer un rapport de performance</h1>

    <!-- Champ Employé avec autocomplete -->
    <div class="mb-4 relative">
      <label class="font-semibold mr-2">Employé :</label>
      <input
        type="text"
        v-model="search"
        @input="searchEmployes"
        placeholder="Tapez le nom, prénom ou ID"
        class="border px-2 py-1 rounded w-full"
        autocomplete="off"
      />
      <ul v-if="suggestions.length" class="absolute border bg-white w-full mt-1 max-h-48 overflow-auto z-10">
        <li
          v-for="emp in suggestions"
          :key="emp.idEmploye"
          @click="selectEmploye(emp)"
          class="px-2 py-1 hover:bg-gray-200 cursor-pointer flex items-center gap-2"
        >
          <img :src="emp.imagePath" alt="Profil" class="w-6 h-6 rounded-full object-cover" />
          {{ emp.prenom }} {{ emp.nom }} ({{ emp.idEmploye }})
        </li>
      </ul>
    </div>

    <!-- Sélecteur mois/année -->
    <div class="mb-4 flex gap-4">
      <div>
        <label class="font-semibold mr-2">Mois :</label>
        <select v-model.number="month" class="border px-2 py-1 rounded">
          <option v-for="m in 12" :key="m" :value="m">{{ m }}</option>
        </select>
      </div>
      <div>
        <label class="font-semibold mr-2">Année :</label>
        <input type="number" v-model.number="year" min="2000" max="2100" class="border px-2 py-1 rounded" />
      </div>
    </div>

    <div class="mb-4">
      <label class="font-semibold mr-2">Commentaire :</label>
      <input type="text" v-model="commentaire" placeholder="Commentaire (optionnel)" class="border px-2 py-1 rounded w-full" />
    </div>

    <button @click="genererRapport" class="bg-blue-500 text-white px-4 py-2 rounded mb-4">Générer le rapport</button>

    <!-- Affichage du rapport -->
    <div v-if="rapport" class="mt-6 p-4 border rounded bg-white shadow">
      <h2 class="text-xl font-semibold mb-2">Rapport généré</h2>
      
      <div class="flex items-center gap-4 mb-2">
        <img :src="rapport.employe.imagePath" alt="Profil" class="w-12 h-12 rounded-full object-cover" />
        <p><strong>Employé :</strong> {{ rapport.employe.prenom }} {{ rapport.employe.nom }}</p>
      </div>

      <p><strong>Période :</strong> {{ rapport.periode }}</p>
      <p><strong>Score Productivité :</strong> {{ rapport.scoreProductivite }}</p>
      <p><strong>Score Efficacité :</strong> {{ rapport.scoreEfficacite }}</p>
      <p><strong>Score Qualité :</strong> {{ rapport.scoreQualite }}</p>
      <p v-if="rapport.commentaire"><strong>Commentaire :</strong> {{ rapport.commentaire }}</p>
      <p><strong>Date génération :</strong> {{ rapport.dateGeneration }}</p>

      <button @click="downloadPDF" class="bg-green-500 text-white px-4 py-2 rounded mt-4">Télécharger PDF</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import jsPDF from 'jspdf'

const search = ref('')
const suggestions = ref([])
const idEmploye = ref('')
const month = ref(new Date().getMonth() + 1)
const year = ref(new Date().getFullYear())
const commentaire = ref('')
const rapport = ref(null)

// Rechercher tous les employés (endpoint global)
const searchEmployes = async () => {
  if (!search.value) {
    suggestions.value = []
    return
  }
  try {
    const res = await axios.get(`http://localhost:8085/api/employes`) // TOUS les employés
    suggestions.value = res.data.filter(emp =>
      emp.nom.toLowerCase().includes(search.value.toLowerCase()) ||
      emp.prenom.toLowerCase().includes(search.value.toLowerCase()) ||
      emp.idEmploye.toLowerCase().includes(search.value.toLowerCase())
    )
  } catch (err) {
    console.error(err)
  }
}

const selectEmploye = (emp) => {
  idEmploye.value = emp.idEmploye
  search.value = `${emp.prenom} ${emp.nom}`
  suggestions.value = []
}

// Générer le rapport
const genererRapport = async () => {
  if (!idEmploye.value) return alert('Veuillez sélectionner un employé')
  try {
    const res = await axios.post(`http://localhost:8085/api/rapport/generer`, null, {
      params: { idEmploye: idEmploye.value, month: month.value, year: year.value, commentaire: commentaire.value || null }
    })
    rapport.value = res.data
  } catch (err) {
    console.error(err)
    alert('Impossible de générer le rapport')
  }
}

// Télécharger le rapport en PDF
const downloadPDF = () => {
  if (!rapport.value) return
  const doc = new jsPDF()
  doc.setFontSize(16)
  doc.text(`Rapport de performance - ${rapport.value.periode}`, 10, 20)
  doc.setFontSize(12)
  doc.text(`Employé : ${rapport.value.employe.prenom} ${rapport.value.employe.nom}`, 10, 30)
  doc.text(`Score Productivité : ${rapport.value.scoreProductivite}`, 10, 40)
  doc.text(`Score Efficacité : ${rapport.value.scoreEfficacite}`, 10, 50)
  doc.text(`Score Qualité : ${rapport.value.scoreQualite}`, 10, 60)
  if (rapport.value.commentaire) doc.text(`Commentaire : ${rapport.value.commentaire}`, 10, 70)
  doc.text(`Date génération : ${rapport.value.dateGeneration}`, 10, 80)
  doc.save(`rapport_${rapport.value.employe.idEmploye}_${rapport.value.periode}.pdf`)
}
</script>

<style scoped>
ul { list-style: none; padding: 0; margin: 0; }
li { cursor: pointer; padding: 4px 8px; display: flex; align-items: center; gap: 4px; }
li:hover { background: #f0f0f0; }
img { object-fit: cover; }
</style>
