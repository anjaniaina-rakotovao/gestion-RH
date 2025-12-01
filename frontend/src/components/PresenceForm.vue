<template>
    <div class="presence-form">
      <h2>Enregistrer présence</h2>
  
      <div class="form-group">
        <label for="idEmploye">ID Employé</label>
        <input id="idEmploye" v-model="idEmploye" placeholder="EMP001" />
      </div>
  
      <div class="form-group">
        <label for="dateTime">Date & heure</label>
        <input id="dateTime" type="datetime-local" v-model="dateTimeLocal" />
      </div>
  
      <div class="form-group movement">
        <label>Mouvement</label>
        <label class="chk">
          <input type="checkbox" value="entrée" v-model="movementSelection" @change="onlyOne('entrée')"/>
          Entrée
        </label>
        <label class="chk">
          <input type="checkbox" value="sortie" v-model="movementSelection" @change="onlyOne('sortie')"/>
          Sortie
        </label>
      </div>
  
      <div class="actions">
        <button @click="submit" :disabled="loading">Enregistrer</button>
        <button class="secondary" @click="reset" :disabled="loading">Réinitialiser</button>
        <!-- Bouton Close Journey -->
        <button class="close-journey" @click="openCloseModal">Close Journey</button>
      </div>
  
      <p v-if="message" :class="{'success': success, 'error': !success}">{{ message }}</p>
  
      <!-- Popup Close Journey -->
      <div class="modal" v-if="showCloseModal">
        <div class="modal-content">
          <h3>Clôturer la journée</h3>
          <div class="form-group">
            <label for="closeDate">Date</label>
            <input id="closeDate" type="date" v-model="closeDate" />
          </div>
          <div class="actions">
            <button @click="validateClose" :disabled="loading">Valider</button>
            <button class="secondary" @click="closeModal" :disabled="loading">Annuler</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  
  const idEmploye = ref('');
  const dateTimeLocal = ref('');
  const movementSelection = ref([]);
  const loading = ref(false);
  const message = ref('');
  const success = ref(false);
  
  // Popup Close Journey
  const showCloseModal = ref(false);
  const closeDate = ref(new Date().toISOString().split('T')[0]); // date du jour par défaut
  
  function onlyOne(value) {
    if (movementSelection.value.includes(value)) {
      movementSelection.value = [value];
    } else {
      movementSelection.value = [];
    }
  }
  
  function reset() {
    idEmploye.value = '';
    dateTimeLocal.value = '';
    movementSelection.value = [];
    message.value = '';
    success.value = false;
  }
  
  // Construire ISO pour Instant.parse
  function buildIsoDatetime(localValue) {
    if (!localValue) return new Date().toISOString();
    const dt = new Date(localValue);
    return dt.toISOString();
  }
  
  // Submit présence
  async function submit() {
    message.value = '';
    success.value = false;
  
    if (!idEmploye.value) {
      message.value = "L'ID employé est requis.";
      return;
    }
    if (!movementSelection.value.length) {
      message.value = "Sélectionne 'Entrée' ou 'Sortie'.";
      return;
    }
  
    loading.value = true;
    try {
      const movement = movementSelection.value[0];
      const iso = buildIsoDatetime(dateTimeLocal.value);
      const url = `/api/presence/${encodeURIComponent(idEmploye.value)}/${encodeURIComponent(iso)}/${encodeURIComponent(movement)}/add`;
      await axios.post(url);
      success.value = true;
      message.value = 'Présence enregistrée avec succès.';
    } catch (err) {
      console.error(err);
      success.value = false;
      message.value = 'Erreur lors de l’enregistrement.';
    } finally {
      loading.value = false;
    }
  }
  
  // --- Close Journey ---
  function openCloseModal() {
    showCloseModal.value = true;
  }
  
  function closeModal() {
    showCloseModal.value = false;
  }
  
  async function validateClose() {
    if (!closeDate.value) return;
    loading.value = true;
    try {
      const url = `/api/presence/close?date=${encodeURIComponent(closeDate.value)}`;
      await axios.post(url);
      success.value = true;
      message.value = 'Journée clôturée avec succès.';
      closeModal();
    } catch (err) {
      console.error(err);
      success.value = false;
      message.value = 'Erreur lors de la clôture.';
    } finally {
      loading.value = false;
    }
  }
  </script>
  
  <style scoped>
  .presence-form{
    max-width:420px;
    margin:20px auto;
    padding:18px;
    border-radius:12px;
    box-shadow: 0 6px 30px rgba(0,0,0,0.08);
    background: linear-gradient(180deg, #ffffff, #fbfbff);
    font-family: "Segoe UI", Roboto, "Helvetica Neue", Arial;
  }
  .presence-form h2{
    margin:0 0 12px;
    font-size:20px;
    color:#333;
    text-align:center;
  }
  .form-group{
    display:flex;
    flex-direction:column;
    margin-bottom:12px;
  }
  .form-group label{
    margin-bottom:6px;
    font-size:13px;
    color:#444;
  }
  .form-group input[type="text"],
  .form-group input[type="datetime-local"],
  .form-group input[type="email"],
  .form-group input[type="number"],
  .form-group input[type="date"]{
    padding:10px 12px;
    border-radius:8px;
    border:1px solid #e0e0e8;
    outline:none;
    font-size:14px;
    transition:box-shadow .15s, border-color .15s;
  }
  .form-group input:focus{
    box-shadow:0 6px 18px rgba(78,115,255,0.08);
    border-color:#4e73ff;
  }
  .movement{
    display:flex;
    gap:10px;
    align-items:center;
  }
  .chk{
    display:flex;
    align-items:center;
    gap:8px;
    font-size:14px;
    cursor:pointer;
  }
  .actions{
    display:flex;
    gap:8px;
    margin-top:8px;
  }
  button{
    padding:10px 14px;
    border-radius:10px;
    border: none;
    cursor:pointer;
    font-weight:600;
    background: linear-gradient(90deg,#4e73ff,#6a8cff);
    color:white;
    box-shadow: 0 6px 18px rgba(78,115,255,0.18);
  }
  button:disabled{
    opacity:0.6;
    cursor:not-allowed;
  }
  button.secondary{
    background:transparent;
    color:#444;
    border:1px solid #e6e6f0;
    box-shadow:none;
  }
  button.close-journey{
    background: linear-gradient(90deg,#ff6a6a,#ff8787);
  }
  p.success{
    margin-top:12px;
    color: #116b3a;
    font-weight:600;
  }
  p.error{
    margin-top:12px;
    color: #9b1c1c;
    font-weight:600;
  }
  
  /* --- Modal --- */
  .modal{
    position: fixed;
    top:0; left:0; right:0; bottom:0;
    background: rgba(0,0,0,0.5);
    display:flex;
    justify-content:center;
    align-items:center;
    z-index:1000;
  }
  .modal-content{
    background:#fff;
    padding:20px;
    border-radius:12px;
    width:90%;
    max-width:350px;
    box-shadow:0 6px 20px rgba(0,0,0,0.2);
  }
  </style>
  