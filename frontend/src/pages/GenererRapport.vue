<template>
    <Navbar />

  <div class="p-6 max-w-3xl mx-auto">
    <h1 class="text-2xl font-bold mb-4">Génération du Rapport de Performance</h1>

    <!-- Formulaire de génération -->
    <form @submit.prevent="genererRapport" class="space-y-4 bg-white p-6 rounded shadow">
      <div class="flex flex-col">
        <label for="idEmploye" class="font-semibold">ID Employé :</label>
        <input type="text" id="idEmploye" v-model="idEmploye" required
               class="border px-2 py-1 rounded" placeholder="EMP001" />
      </div>

      <div class="flex gap-4">
        <div class="flex flex-col flex-1">
          <label for="month" class="font-semibold">Mois :</label>
          <select id="month" v-model.number="month" class="border px-2 py-1 rounded">
            <option v-for="m in 12" :key="m" :value="m">{{ m }}</option>
          </select>
        </div>

        <div class="flex flex-col flex-1">
          <label for="year" class="font-semibold">Année :</label>
          <input type="number" id="year" v-model.number="year" min="2000" max="2100"
                 class="border px-2 py-1 rounded" />
        </div>
      </div>

      <div class="flex flex-col">
        <label for="commentaire" class="font-semibold">Commentaire (optionnel) :</label>
        <textarea id="commentaire" v-model="commentaire" rows="3"
                  class="border px-2 py-1 rounded"></textarea>
      </div>

      <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
        Générer le rapport
      </button>
    </form>

    <!-- Résultat -->
    <div v-if="rapport" class="mt-6 bg-white p-6 rounded shadow">
      <h2 class="text-xl font-semibold mb-4">Rapport généré</h2>
      <div class="flex gap-4 mb-2 items-center">
        <img :src="rapport.employe.imagePath" alt="Profil" class="w-16 h-16 rounded-full object-cover" />
        <div>
          <div class="font-semibold">{{ rapport.employe.prenom }} {{ rapport.employe.nom }}</div>
          <div class="text-gray-600 text-sm">{{ rapport.employe.mail }}</div>
          <div class="text-gray-600 text-sm">{{ rapport.employe.adresse }}</div>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-4 mt-4">
        <div><span class="font-semibold">Période :</span> {{ rapport.periode }}</div>
        <div><span class="font-semibold">Date génération :</span> {{ rapport.dateGeneration }}</div>
        <div><span class="font-semibold">Score Productivité :</span> {{ rapport.scoreProductivite }}</div>
        <div><span class="font-semibold">Score Efficacité :</span> {{ rapport.scoreEfficacite }}</div>
        <div><span class="font-semibold">Score Qualité :</span> {{ rapport.scoreQualite }}</div>
        <div><span class="font-semibold">Commentaire :</span> {{ rapport.commentaire ?? '-' }}</div>
      </div>

      <button @click="telechargerPDF" class="mt-4 bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
        Télécharger PDF
      </button>
    </div>

    <div v-else-if="erreur" class="mt-6 text-red-600 font-semibold">
      {{ erreur }}
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import jsPDF from 'jspdf'
import Navbar from "@/pages/NavBarPerformance.vue"; 


const idEmploye = ref('')
const month = ref(new Date().getMonth() + 1)
const year = ref(new Date().getFullYear())
const commentaire = ref('')

const rapport = ref(null)
const erreur = ref(null)

const genererRapport = async () => {
  rapport.value = null
  erreur.value = null
  try {
    const res = await axios.post('/api/rapport/generer', null, {
      params: {
        idEmploye: idEmploye.value,
        month: month.value,
        year: year.value,
        commentaire: commentaire.value || null
      }
    })
    rapport.value = res.data
  } catch (err) {
    console.error(err)
    erreur.value = 'Impossible de générer le rapport. Vérifiez les paramètres.'
  }
}

const telechargerPDF = () => {
  if (!rapport.value) return

  const doc = new jsPDF()
  doc.setFontSize(18)
  doc.text('Rapport de Performance', 105, 20, { align: 'center' })
  doc.setFontSize(12)
  doc.text(`Employé : ${rapport.value.employe.prenom} ${rapport.value.employe.nom}`, 20, 40)
  doc.text(`ID : ${rapport.value.employe.idEmploye}`, 20, 48)
  doc.text(`Mail : ${rapport.value.employe.mail}`, 20, 56)
  doc.text(`Adresse : ${rapport.value.employe.adresse}`, 20, 64)
  doc.text(`Période : ${rapport.value.periode}`, 20, 72)
  doc.text(`Date génération : ${rapport.value.dateGeneration}`, 20, 80)
  doc.text(`Score Productivité : ${rapport.value.scoreProductivite}`, 20, 88)
  doc.text(`Score Efficacité : ${rapport.value.scoreEfficacite}`, 20, 96)
  doc.text(`Score Qualité : ${rapport.value.scoreQualite}`, 20, 104)
  doc.text(`Commentaire : ${rapport.value.commentaire ?? '-'}`, 20, 112)

  doc.save(`rapport_${rapport.value.employe.idEmploye}_${rapport.value.periode}.pdf`)
}
</script>

<style scoped>
input, select, textarea {
  transition: border-color 0.2s;
}
input:focus, select:focus, textarea:focus {
  border-color: #3b82f6;
  outline: none;
}
</style>
