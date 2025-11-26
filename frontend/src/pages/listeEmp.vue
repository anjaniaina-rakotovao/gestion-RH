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
    <div>
        <h1>Liste des employés</h1>
        <div style="background: #fff3cd; padding: 10px; margin: 10px 0; border: 1px solid #ffeaa7;">
            <strong>État:</strong>
            <span v-if="loading">🔄 Chargement...</span>
            <span v-else-if="error">❌ Erreur: {{ error }}</span>
            <span v-else>✅ Données chargées ({{ domaines.length }} domaines)</span>
        </div>
        <table v-for="domaine in domaines" :key="domaine.idDomaine">
            <thead>
                {{ domaine.libelleDomaine }}
            </thead>
            <tbody>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Poste Actuel</th>
                </tr>
                <template v-if="domaine.employes.length > 0">
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
</template>