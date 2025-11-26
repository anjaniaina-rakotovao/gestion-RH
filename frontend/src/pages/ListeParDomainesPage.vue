<script>
import axios from 'axios';

export default {
    data() {
        return {
            domaines: null,
            loading: false,
            error: null,
        }
    },
    async created() {
        await this.fetchDomaines();
    },
    methods: {
        async fetchDomaines() {
            this.loading = true;
            this.error = null;

            try {
                const response = await axios.get('/employes/domaines');
                if (response.status !== 200) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                this.domaines = response.data;
                console.log('Data received:', this.domaines);
            } catch (error) {
                this.error = error.message;
                console.error('Error fetching data:', error);
            } finally {
                this.loading = false;
            }
        }
    }
}
</script>

<template>
    <div class="liste-domaines">
        <h1>Liste des employés par domaines</h1>
        <div style="background: #fff3cd; padding: 10px; margin: 10px 0; border: 1px solid #ffeaa7;">
            <strong>État:</strong>
            <span v-if="loading">🔄 Chargement...</span>
            <span v-else-if="error">❌ Erreur: {{ error }}</span>
            <span v-else>✅ Données chargées ({{ domaines?.length || 0 }} domaines)</span>
        </div>
        <div v-if="domaines" class="domaines-container">
            <table v-for="domaine in domaines" :key="domaine.idDomaine" class="domaine-table">
                <thead>
                    <tr>
                        <th colspan="3">{{ domaine.libelleDomaine }}</th>
                    </tr>
                    <tr>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Poste Actuel</th>
                    </tr>
                </thead>
                <tbody>
                    <template v-if="domaine.employes && domaine.employes.length > 0">
                        <tr v-for="employe in domaine.employes" :key="employe.id">
                            <td>{{ employe.nom }}</td>
                            <td>{{ employe.prenom }}</td>
                            <td>{{ employe.posteActuel }}</td>
                        </tr>
                    </template>
                    <template v-else>
                        <tr>
                            <td colspan="3">Aucun employé dans ce domaine.</td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
    </div>
</template>

<style scoped>
.liste-domaines {
    padding: 20px;
}

.domaines-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.domaine-table {
    width: 100%;
    border-collapse: collapse;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin-bottom: 10px;
}

.domaine-table thead {
    background-color: #4CAF50;
    color: white;
}

.domaine-table thead tr:first-child th {
    padding: 15px;
    font-size: 16px;
    font-weight: bold;
}

.domaine-table thead tr:last-child th {
    padding: 10px;
    border-bottom: 2px solid #45a049;
}

.domaine-table tbody tr {
    border-bottom: 1px solid #ddd;
}

.domaine-table tbody tr:hover {
    background-color: #f5f5f5;
}

.domaine-table td {
    padding: 12px;
}

.domaine-table tbody tr:nth-child(even) {
    background-color: #f9f9f9;
}
</style>
