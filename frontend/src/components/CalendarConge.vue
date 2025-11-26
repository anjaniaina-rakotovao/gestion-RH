<template>
  <div>
    <h2>Calendrier minimal</h2>
    <FullCalendar
      v-if="calendarOptions.plugins.length > 0"
      :options="calendarOptions"
    />
  </div>
</template>

<script>
export default {
  name: 'CalendarConge',
  components: {
    FullCalendar: () => import('@fullcalendar/vue3').then(mod => mod.default)
  },
  data() {
    return {
      calendarOptions: {
        plugins: [],
        initialView: 'dayGridMonth',
        events: [
          { title: 'Test Congé', start: '2025-11-16', end: '2025-11-18' },
          { title: 'Congé John Doe', start: '2025-11-20', end: '2025-11-22' }
        ],
        height: 'auto'
      }
    };
  },
  async mounted() {
    try {
      // Charger les plugins dynamiquement
      const dayGridPlugin = await import('@fullcalendar/daygrid');
      this.calendarOptions.plugins = [dayGridPlugin.default];
    } catch (error) {
      console.error('Erreur chargement plugins:', error);
    }
  }
};
</script>