<template>
  <div>
    <h2>Marquer présence</h2>
    <input v-model="employeId" placeholder="ID Employé" />
    <select v-model="mouvement">
      <option value="ENTREE">Entrée</option>
      <option value="SORTIE">Sortie</option>
    </select>
    <button @click="submitPresence">Marquer</button>
    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import rhService from '../services/rhService';

export default {
  data() {
    return {
      employeId: '',
      mouvement: 'ENTREE',
      message: ''
    };
  },
  methods: {
    submitPresence() {
      rhService.marquerPresence(this.employeId, this.mouvement)
        .then(() => { this.message = 'Présence enregistrée !'; })
        .catch(err => { this.message = 'Erreur : ' + err; });
    }
  }
}
</script>
