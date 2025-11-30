<template>
  <div class="p-6">
    <h1 class="text-2xl font-bold mb-4">Scores des employés</h1>

    <!-- Formulaire unique -->
    <form @submit.prevent="fetchScores" class="flex items-center gap-4 mb-6">
      <label for="annee">Année :</label>
      <input type="number" id="annee" v-model.number="annee" min="2000" max="2100" class="border px-2 py-1 rounded" />

      <label for="mois">Mois :</label>
      <select v-model.number="mois" id="mois" class="border px-2 py-1 rounded">
        <option v-for="m in 12" :key="m" :value="m">{{ m }}</option>
      </select>

      <button type="submit" class="bg-blue-500 text-white px-4 py-1 rounded">Afficher</button>
    </form>

    <!-- KPI Annuel -->
    <div v-if="scoresAnnuel.length" class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Score Annuel {{ annee }}</h2>
      <div class="kpi-grid">
        <div v-for="(item, index) in scoresAnnuel" :key="index" class="kpi-card annual">
          <div class="kpi-icon">📊</div>
          <div class="kpi-content">
            <h3>{{ item.nom }}</h3>
            <div class="kpi-value">{{ item.score }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- KPI Mensuel -->
    <div v-if="scoresMensuel.length" class="mb-6">
      <h2 class="text-xl font-semibold mb-2">Score Mois {{ mois }} / {{ annee }}</h2>
      <div class="kpi-grid">
        <div v-for="(item, index) in scoresMensuel" :key="index" class="kpi-card monthly">
          <div class="kpi-icon">📈</div>
          <div class="kpi-content">
            <h3>{{ item.nom }}</h3>
            <div class="kpi-value">{{ item.score }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Graphique annuel par mois -->
    <div class="chart-card">
  <h4>📈 Évolution des Scores Annuel ({{ annee }})</h4>
  <div class="chart-container">
    <canvas ref="chartCanvas"></canvas>
  </div>
</div>

<!-- Mois le plus haut -->
<div v-if="moisMaxScores" class="text-center text-gray-700 font-semibold text-base mt-2">
  📈 Mois le plus haut pour {{ annee }} : {{ moisMaxScores }}
</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { Chart, LineController, LineElement, PointElement, CategoryScale, LinearScale, Title, Tooltip, Legend } from 'chart.js'

Chart.register(LineController, LineElement, PointElement, CategoryScale, LinearScale, Title, Tooltip, Legend)

const annee = ref(new Date().getFullYear())
const mois = ref(new Date().getMonth() + 1)

const scoresAnnuel = ref([])
const scoresMensuel = ref([])

const chartCanvas = ref(null)
let chartInstance = null
const moisMaxScores = ref(null)

const moisLabels = ['Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre']

const fetchScores = async () => {
  try { const resAnn = await axios.get(`/api/score/annee/${annee.value}`); scoresAnnuel.value = resAnn.data.listeScoringPeriode } catch { scoresAnnuel.value = [] }
  try { const resMois = await axios.get(`/api/score/mois/${mois.value}/${annee.value}`); scoresMensuel.value = resMois.data.listeScoringPeriode } catch { scoresMensuel.value = [] }

  try {
    const resGraph = await axios.get(`/api/score/mois/annee/${annee.value}`)
    const labels = resGraph.data.map(item => item.periode.split('-')[0])
    const prod = resGraph.data.map(item => item.listeScoringPeriode.find(s => s.nom === 'Productivité')?.score ?? 0)
    const efficacite = resGraph.data.map(item => item.listeScoringPeriode.find(s => s.nom === 'Efficacité')?.score ?? 0)
    const qualite = resGraph.data.map(item => item.listeScoringPeriode.find(s => s.nom === 'Qualité')?.score ?? 0)

    // Calcul du mois le plus haut
    const totalScores = prod.map((p,i) => p + efficacite[i] + qualite[i])
    const maxIndex = totalScores.indexOf(Math.max(...totalScores))
    moisMaxScores.value = moisLabels[labels[maxIndex]-1]

    if (chartInstance) {
      chartInstance.data.labels = labels.map(m => moisLabels[m-1])
      chartInstance.data.datasets[0].data = prod
      chartInstance.data.datasets[1].data = efficacite
      chartInstance.data.datasets[2].data = qualite
      chartInstance.update()
    } else {
      chartInstance = new Chart(chartCanvas.value, {
        type: 'line',
        data: {
          labels: labels.map(m => moisLabels[m-1]),
          datasets: [
            { label: 'Productivité', data: prod, borderColor: 'red', backgroundColor: 'rgba(255,0,0,0.2)', pointRadius: 4 },
            { label: 'Efficacité', data: efficacite, borderColor: 'blue', backgroundColor: 'rgba(0,0,255,0.2)', pointRadius: 4 },
            { label: 'Qualité', data: qualite, borderColor: 'green', backgroundColor: 'rgba(0,255,0,0.2)', pointRadius: 4 }
          ]
        },
        options: { responsive: true, maintainAspectRatio: false }
      })
    }
  } catch { if (chartInstance) chartInstance.destroy() }
}

onMounted(fetchScores)
</script>

<style scoped>
.kpi-grid { display: flex; flex-wrap: wrap; gap: 15px; }
.kpi-card { background: white; padding: 20px; border-radius: 12px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); flex: 1 1 120px; display: flex; align-items: center; gap: 12px; }
.kpi-icon { font-size: 1.8em; }
.kpi-content h3 { margin: 0; font-size: 14px; font-weight: 600; }
.kpi-value { font-size: 18px; font-weight: bold; }
.kpi-card.annual { border-left: 4px solid #3498db; }
.kpi-card.monthly { border-left: 4px solid #e74c3c; }
.chart-container {
  height: 250px; /* important */
  position: relative;
}
.chart-card { background: white; padding: 20px; border-radius: 12px; box-shadow: 0 2px 6px rgba(0,0,0,0.1); margin-top: 20px; }
</style>
