<template>
  <div class="container">
    <h2>Scoring de la période {{ periode }}</h2>

    <div v-if="loading">Chargement...</div>

    <div v-else>
      <table class="scoring-table">
        <thead>
          <tr>
            <th>Critère</th>
            <th>Score</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in scoring.listeScoringPeriode" :key="item.nom">
            <td>{{ item.nom }}</td>
            <td>{{ item.score }}</td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ScoringPeriode",

  props: {
    month: {
      type: Number,
      required: true
    },
    year: {
      type: Number,
      required: true
    }
  },

  data() {
    return {
      scoring: null,
      loading: true,
    };
  },

  computed: {
    periode() {
      return `${this.month}-${this.year}`;
    }
  },

  mounted() {
    this.fetchScoring();
  },

  methods: {
    async fetchScoring() {
      try {
        const res = await axios.get(`/api/scoring/${this.month}/${this.year}`);
        this.scoring = res.data;
      } catch (e) {
        console.error("Erreur API:", e);
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.container {
  margin: 20px;
  padding: 20px;
  border-radius: 8px;
  background: #fafafa;
  width: 450px;
}

.scoring-table {
  width: 100%;
  border-collapse: collapse;
}

.scoring-table th,
.scoring-table td {
  padding: 10px;
  border: 1px solid #ddd;
}

.scoring-table th {
  background: #f0f0f0;
  font-weight: bold;
}
</style>
