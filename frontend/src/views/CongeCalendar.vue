<template>
  <div class="calendar-container">
    <h2>Calendrier des Congés</h2>

    <div class="navigation-controls">
      <div class="year-navigation">
        <button @click="previousYear" title="Année précédente">◀◀</button>
        <span class="year-display">{{ currentYear }}</span>
        <button @click="nextYear" title="Année suivante">▶▶</button>
      </div>

      <div class="month-navigation">
        <button @click="previousMonth" title="Mois précédent">◀</button>
        <select v-model="selectedMonth" @change="goToSpecificMonth">
          <option v-for="month in months" :key="month.value" :value="month.value">
            {{ month.label }}
          </option>
        </select>
        <button @click="nextMonth" title="Mois suivant">▶</button>
      </div>

      <div class="quick-actions">
        <button @click="goToToday" class="today-btn">Aujourd'hui</button>
        <button @click="toggleView" class="view-toggle">
          {{ currentView === 'dayGridMonth' ? 'Vue Semaine' : 'Vue Mois' }}
        </button>
      </div>
    </div>

    <!-- Légende des couleurs -->
    <div class="color-legend">
      <div class="legend-item">
        <span class="color-dot" style="background-color: #ff8ac5"></span>
        <span>Femme</span>
      </div>
      <div class="legend-item">
        <span class="color-dot" style="background-color: #4fa3ff"></span>
        <span>Homme</span>
      </div>
      <div class="legend-item">
        <span class="color-dot" style="background-color: #999999"></span>
        <span>Non spécifié</span>
      </div>
    </div>

    <div class="input-group">
      <input 
        v-model="employeId"
        type="text"
        placeholder="Ex: EMP001"
      />
      <button @click="loadCongesByEmployee">Filtrer</button>
      <button class="reset" @click="loadAllConges">Tous</button>
    </div>

    <div v-if="loading" class="loader">Chargement ...</div>

    <div ref="calendarRef" class="calendar-wrapper"></div>

    <div v-if="!loading && calendarInitialized && events.length === 0" class="empty">
      Aucun congé trouvé.
    </div>

    <div v-if="showDebug" class="debug-info">
      <p>Vue: {{ currentView === 'dayGridMonth' ? 'Mois' : 'Semaine' }}</p>
      <p>Période: {{ currentPeriod }}</p>
      <p>Événements chargés: {{ events.length }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'CalendarConge',
  
  data() {
    return {
      employeId: "",
      loading: false,
      events: [],
      calendar: null,
      calendarInitialized: false,
      showDebug: false,
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth(),
      currentView: 'dayGridMonth',
      currentPeriod: '',
      selectedMonth: new Date().getMonth(),
      
      months: [
        { value: 0, label: 'Janvier' },
        { value: 1, label: 'Février' },
        { value: 2, label: 'Mars' },
        { value: 3, label: 'Avril' },
        { value: 4, label: 'Mai' },
        { value: 5, label: 'Juin' },
        { value: 6, label: 'Juillet' },
        { value: 7, label: 'Août' },
        { value: 8, label: 'Septembre' },
        { value: 9, label: 'Octobre' },
        { value: 10, label: 'Novembre' },
        { value: 11, label: 'Décembre' }
      ]
    };
  },

  async mounted() {
    await this.initializeCalendar();
    await this.loadAllConges();
  },

  beforeUnmount() {
    if (this.calendar) {
      this.calendar.destroy();
    }
  },

  methods: {
    async initializeCalendar() {
      try {
        const { Calendar } = await import('@fullcalendar/core');
        const { default: dayGridPlugin } = await import('@fullcalendar/daygrid');
        
        this.calendar = new Calendar(this.$refs.calendarRef, {
          plugins: [dayGridPlugin],
          initialView: 'dayGridMonth',
          initialDate: new Date(),
          events: [],
          height: 'auto',
          headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,dayGridWeek'
          },
          datesSet: (dateInfo) => {
            this.currentPeriod = dateInfo.view.title;
            this.currentView = dateInfo.view.type;
            this.updateNavigationState(dateInfo.start);
          },
          viewDidMount: (viewInfo) => {
            this.currentView = viewInfo.view.type;
          }
        });
        
        this.calendar.render();
        this.calendarInitialized = true;
        
      } catch (error) {
        console.error('Erreur initialisation:', error);
      }
    },

    updateNavigationState(startDate) {
      this.currentYear = startDate.getFullYear();
      this.currentMonth = startDate.getMonth();
      this.selectedMonth = startDate.getMonth();
    },

    previousYear() { if (this.calendar) this.calendar.prevYear(); },
    nextYear() { if (this.calendar) this.calendar.nextYear(); },
    previousMonth() { if (this.calendar) this.calendar.prev(); },
    nextMonth() { if (this.calendar) this.calendar.next(); },

    goToSpecificMonth() {
      if (this.calendar) {
        const targetDate = new Date(this.currentYear, this.selectedMonth, 1);
        this.calendar.gotoDate(targetDate);
      }
    },

    goToToday() {
      if (this.calendar) {
        this.calendar.today();
        this.selectedMonth = new Date().getMonth();
      }
    },

    toggleView() {
      if (this.calendar) {
        const newView = this.currentView === 'dayGridMonth' ? 'dayGridWeek' : 'dayGridMonth';
        this.calendar.changeView(newView);
      }
    },

    async updateCalendarEvents() {
      if (!this.calendar) return;

      this.calendar.removeAllEvents();
      
      this.events.forEach(event => {
        try {
          const addedEvent = this.calendar.addEvent(event);
          console.log("Événement ajouté:", event.title, "Couleur:", event.color);
        } catch (error) {
          console.error("Erreur ajout événement:", error, event);
        }
      });

      this.calendar.render();
    },

    async loadAllConges() {
      this.loading = true;
      try {
        const res = await axios.get("http://localhost:8085/api/rh/conges"); 
        this.events = this.formatEvents(res.data);
        console.log("Événements formatés:", this.events);
        
        await this.updateCalendarEvents();
      } catch (err) {
        console.error(err);
        alert("Impossible de charger les congés.");
      }
      this.loading = false;
    },

    async loadCongesByEmployee() {
      if (!this.employeId) return this.loadAllConges();

      this.loading = true;
      try {
        const res = await axios.get(`http://localhost:8085/api/rh/conges/${this.employeId}`);
        this.events = this.formatEvents(res.data);
        console.log("Événements filtrés:", this.events);
        
        await this.updateCalendarEvents();
      } catch (err) {
        console.error(err);
        alert("Erreur chargement des congés.");
      }
      this.loading = false;
    },

    formatEvents(conges) {
      if (!Array.isArray(conges)) return [];

      return conges
        .map(c => {
          if (!c.dateDebut) return null;

          const nom = c.employe?.nom
            ? `${c.employe.nom} ${c.employe.prenom}`
            : "Employé inconnu";

          const color = this.getColorFromSexe(c);

          const event = {
            title: `${c.typeConge?.libelle ?? "Congé"} - ${nom}`,
            start: c.dateDebut.split("T")[0],
            allDay: true,
            display: 'block',
            color: color, // ✅ Utiliser 'color' au lieu de backgroundColor/borderColor
            textColor: "#ffffff"
          };

          if (c.dateFin && c.dateFin !== c.dateDebut) {
            event.end = c.dateFin.split("T")[0];
          }

          return event;
        })
        .filter(e => e !== null);
    },

    getColorFromSexe(conge) {
      const sexe = conge?.employe?.sexe?.libelle?.toLowerCase() ?? "";

      if (sexe.includes("femme")) return "#ff8ac5";   // Rose
      if (sexe.includes("homme")) return "#4fa3ff";   // Bleu clair

      return "#999999"; // Neutre si inconnu
    }
  }
};
</script>

<style scoped>
.calendar-container {
  background: white;
  padding: 20px;
  border-radius: 12px;
  max-width: 1000px;
  margin: auto;
  margin-top: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.navigation-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  flex-wrap: wrap;
  gap: 15px;
}

/* Légende des couleurs */
.color-legend {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 15px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 6px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #555;
}

.color-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: inline-block;
  border: 2px solid white;
  box-shadow: 0 1px 3px rgba(0,0,0,0.2);
}

.year-navigation, .month-navigation, .quick-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.year-navigation {
  flex: 1;
  justify-content: flex-start;
}

.month-navigation {
  flex: 2;
  justify-content: center;
}

.quick-actions {
  flex: 1;
  justify-content: flex-end;
}

.year-display {
  font-size: 1.2em;
  font-weight: bold;
  color: #2c3e50;
  min-width: 80px;
  text-align: center;
}

select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: white;
  font-size: 14px;
  min-width: 120px;
}

button {
  padding: 8px 16px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

button:hover {
  background: #2980b9;
}

.reset { 
  background: #2ecc71; 
}

.reset:hover {
  background: #27ae60;
}

.today-btn {
  background: #e74c3c;
}

.today-btn:hover {
  background: #c0392b;
}

.view-toggle {
  background: #9b59b6;
}

.view-toggle:hover {
  background: #8e44ad;
}

.input-group {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

input {
  flex: 1;
  min-width: 150px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.loader { 
  margin: 20px 0; 
  color: #555; 
  text-align: center;
}

.empty { 
  color: #777; 
  font-style: italic; 
  text-align: center;
  margin: 20px 0;
}

.calendar-wrapper {
  margin: 20px 0;
  min-height: 600px;
}

.debug-info {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 6px;
  margin-top: 15px;
  font-size: 14px;
  color: #7f8c8d;
}

.debug-info p {
  margin: 5px 0;
}

/* Responsive */
@media (max-width: 768px) {
  .navigation-controls {
    flex-direction: column;
    gap: 15px;
  }
  
  .color-legend {
    flex-direction: column;
    gap: 10px;
    align-items: center;
  }
  
  .year-navigation, .month-navigation, .quick-actions {
    justify-content: center;
    width: 100%;
  }
  
  .input-group {
    flex-direction: column;
  }
}
</style>

<style>
/* Styles FullCalendar avec support des couleurs */
.fc {
  font-family: system-ui, -apple-system, sans-serif;
}

.fc .fc-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 1em;
}

.fc .fc-toolbar-title {
  font-size: 1.4em;
  font-weight: bold;
  color: #2c3e50;
}

.fc .fc-button {
  background: #3498db !important;
  border: none !important;
  color: white !important;
  padding: 8px 12px !important;
  border-radius: 4px !important;
  cursor: pointer !important;
}

.fc .fc-button:hover {
  background: #2980b9 !important;
}

.fc .fc-button-active {
  background: #21618c !important;
}

/* Styles pour les événements avec couleurs */
.fc-event {
  border: 2px solid !important;
  color: white !important;
  padding: 6px 8px !important;
  margin: 2px 0 !important;
  border-radius: 4px !important;
  font-size: 12px !important;
  font-weight: bold !important;
  cursor: pointer !important;
  opacity: 1 !important;
}

.fc-event:hover {
  opacity: 0.8 !important;
  transform: translateY(-1px) !important;
}

/* Assurer que les couleurs personnalisées s'appliquent */
.fc-event[style*="background-color: #ff8ac5"],
.fc-event[style*="color: #ff8ac5"] {
  background-color: #ff8ac5 !important;
  border-color: #e67bad !important;
}

.fc-event[style*="background-color: #4fa3ff"],
.fc-event[style*="color: #4fa3ff"] {
  background-color: #4fa3ff !important;
  border-color: #3a8de6 !important;
}

.fc-event[style*="background-color: #999999"],
.fc-event[style*="color: #999999"] {
  background-color: #999999 !important;
  border-color: #7a7a7a !important;
}
</style>