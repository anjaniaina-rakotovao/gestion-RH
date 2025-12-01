<template>
    <div class="attendance-summary">
  
      <h2>Résumé des présences</h2>
  
      <!-- Formulaire -->
      <div class="form-group">
        <label for="year">Année</label>
        <input id="year" type="number" v-model="year" min="2000" max="2100" />
      </div>
  
      <div class="form-group">
        <label for="month">Mois (optionnel)</label>
        <select id="month" v-model="month">
          <option value="">Tous</option>
          <option v-for="(m, index) in monthsList" :key="index" :value="index + 1">{{ m }}</option>
        </select>
      </div>
  
      <div class="form-group">
        <label for="domaine">Domaine (optionnel)</label>
        <select id="domaine" v-model="selectedDomaine">
          <option value="">Tous</option>
          <option v-for="d in domaines" :key="d.idDomaine" :value="d.idDomaine">{{ d.libelle }}</option>
        </select>
      </div>
  
      <button @click="fetchSummary" :disabled="loading">Afficher la courbe</button>
  
      <p v-if="message" :class="{ success: success, error: !success }">{{ message }}</p>
  
      <!-- Graphique -->
      <div v-if="showChart" class="chart-container">
        <canvas ref="chartCanvas"></canvas>
      </div>
  
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, nextTick } from 'vue';
  import axios from 'axios';
  import Chart from 'chart.js/auto';
  
  const year = ref(new Date().getFullYear());
  const month = ref('');
  const selectedDomaine = ref('');
  
  const domaines = ref([]);
  const monthsList = ["Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novembre","Décembre"];
  
  const chartCanvas = ref(null);
  let chartInstance = null;
  const showChart = ref(false);
  
  const loading = ref(false);
  const message = ref('');
  const success = ref(false);
  
  // --- Chargement des domaines une seule fois ---
  onMounted(async () => {
    try {
      const res = await axios.get('/api/domaine/all');
      domaines.value = res.data;
    } catch (err) {
      console.error("Erreur récupération domaines", err);
    }
  });
  
  // --- Récupérer les données et générer le graphique ---
  async function fetchSummary() {
    if (!year.value) {
      message.value = "Veuillez saisir une année.";
      success.value = false;
      return;
    }
  
    loading.value = true;
    message.value = '';
    success.value = false;
    showChart.value = false;
  
    try {
      const params = {
        year: year.value,
        month: month.value || null,
        domaine: selectedDomaine.value || null
      };
      const res = await axios.get('/api/presence/summary', { params });
      const data = res.data;
  
    //   message.value = 'Données récupérées avec succès.';
      success.value = true;
  
      showChart.value = true;
      await nextTick(); // attendre que le canvas soit rendu
  
      generateChart(data);
    } catch (err) {
      console.error('Erreur lors de la récupération des données.', err);
      message.value = 'Erreur lors de la récupération des données.';
      success.value = false;
    } finally {
      loading.value = false;
    }
  }
  
  function generateChart(data) {
    if (!chartCanvas.value) {
      console.error("Canvas non trouvé !");
      return;
    }
  
    // Détruire le chart existant si présent
    if (chartInstance) chartInstance.destroy();
  
    chartInstance = new Chart(chartCanvas.value, {
      type: 'line',
      data: {
        labels: data.labels,      // soit mois ou jours selon la réponse backend
        datasets: [
          {
            label: 'Présents',
            data: data.present,
            borderColor: 'green',
            backgroundColor: 'rgba(0,255,0,0.2)',
          },
          {
            label: 'Absents',
            data: data.absent,
            borderColor: 'red',
            backgroundColor: 'rgba(255,0,0,0.2)',
          },
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: 'top' },
        },
        scales: {
          y: { beginAtZero: true }
        }
      }
    });
  }
  </script>
  
  <style scoped>
  .attendance-summary {
    max-width: 600px;
    margin: 20px auto;
    font-family: "Segoe UI", Roboto, sans-serif;
  }
  
  .form-group {
    margin-bottom: 12px;
    display: flex;
    flex-direction: column;
  }
  
  .form-group label {
    margin-bottom: 4px;
    font-weight: 600;
  }
  
  input, select, button {
    padding: 8px 10px;
    border-radius: 6px;
    border: 1px solid #ccc;
    font-size: 14px;
  }
  
  button {
    margin-top: 10px;
    cursor: pointer;
    background: linear-gradient(90deg,#4e73ff,#6a8cff);
    color: white;
    font-weight: 600;
  }
  
  button:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  p.success { color: green; font-weight: 600; }
  p.error { color: red; font-weight: 600; }
  
  .chart-container {
    margin-top: 20px;
  }
  </style>
  