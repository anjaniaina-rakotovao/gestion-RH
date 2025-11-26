<!-- pages/FichePaiePage.vue -->
<template>
  <div class="fiche-paie-page">
    <div class="page-header">
      <h1>📊 Gestion des Fiches de Paie</h1>
      <p>Calcul et gestion des salaires des employés</p>
    </div>
    
    <div class="content-container">
      <FichePaieCalculator />
    </div>
  </div>
</template>

<script>
import FichePaieCalculator from '../components/FichePaieCalculator.vue'

export default {
  name: 'FichePaiePage',
  components: {
    FichePaieCalculator
  }
}
</script>

<style scoped>
.fiche-paie-page {
  padding: 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.page-header p {
  color: #7f8c8d;
  font-size: 16px;
}

.content-container {
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 30px;
}
</style>