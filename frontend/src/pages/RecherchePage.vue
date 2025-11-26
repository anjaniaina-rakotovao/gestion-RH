<script>
import axios from 'axios';

export default {
    data() {
        return {
            criteresRecherche: {
                nom: '',
                prenom: '',
                poste: '',
                domaine: ''
            },
            resultats: [],
            loading: false,
            error: null,
            rechercheFaite: false,
        }
    },
    methods: {
        async rechercherEmployes() {
            // Valider qu'au moins un critère est rempli
            if (!this.criteresRecherche.nom && !this.criteresRecherche.prenom && 
                !this.criteresRecherche.poste && !this.criteresRecherche.domaine) {
                this.error = 'Veuillez entrer au moins un critère de recherche';
                return;
            }

            this.loading = true;
            this.error = null;
            this.rechercheFaite = true;

            try {
                const response = await axios.post('/employes/recherche', this.criteresRecherche);
                this.resultats = response.data;
                
                if (this.resultats.length === 0) {
                    this.error = 'Aucun employé trouvé avec ces critères';
                }
                console.log('Résultats recherche:', this.resultats);
            } catch (err) {
                this.error = err.response?.data?.message || err.message || 'Erreur lors de la recherche';
                console.error('Error searching:', err);
            } finally {
                this.loading = false;
            }
        },
        reinitialiserRecherche() {
            this.criteresRecherche = {
                nom: '',
                prenom: '',
                poste: '',
                domaine: ''
            };
            this.resultats = [];
            this.error = null;
            this.rechercheFaite = false;
        }
    }
}
</script>

<template>
    <div class="recherche-page">
        <h1>Recherche d'employés</h1>
        
        <div class="recherche-container">
            <div class="formulaire">
                <h2>Critères de recherche</h2>
                
                <div class="form-group">
                    <label for="nom">Nom:</label>
                    <input 
                        id="nom"
                        v-model="criteresRecherche.nom" 
                        type="text" 
                        placeholder="Entrez le nom"
                    >
                </div>

                <div class="form-group">
                    <label for="prenom">Prénom:</label>
                    <input 
                        id="prenom"
                        v-model="criteresRecherche.prenom" 
                        type="text" 
                        placeholder="Entrez le prénom"
                    >
                </div>

                <div class="form-group">
                    <label for="poste">Poste:</label>
                    <input 
                        id="poste"
                        v-model="criteresRecherche.poste" 
                        type="text" 
                        placeholder="Entrez le poste"
                    >
                </div>

                <div class="form-group">
                    <label for="domaine">Domaine:</label>
                    <input 
                        id="domaine"
                        v-model="criteresRecherche.domaine" 
                        type="text" 
                        placeholder="Entrez le domaine"
                    >
                </div>

                <div class="boutons">
                    <button @click="rechercherEmployes" class="btn btn-rechercher" :disabled="loading">
                        <span v-if="loading">🔄 Recherche en cours...</span>
                        <span v-else>🔍 Rechercher</span>
                    </button>
                    <button @click="reinitialiserRecherche" class="btn btn-reinitialiser">
                        🔄 Réinitialiser
                    </button>
                </div>
            </div>
        </div>

        <!-- Messages d'état -->
        <div v-if="error" class="alert alert-error">
            ❌ {{ error }}
        </div>

        <!-- Résultats -->
        <div v-if="rechercheFaite && resultats.length > 0" class="resultats">
            <h2>Résultats de recherche ({{ resultats.length }} employé(s))</h2>
            <table class="resultats-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Poste</th>
                        <th>Domaine</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="employe in resultats" :key="employe.id">
                        <td>{{ employe.id }}</td>
                        <td>{{ employe.nom }}</td>
                        <td>{{ employe.prenom }}</td>
                        <td>{{ employe.posteActuel }}</td>
                        <td>{{ employe.domaine?.libelleDomaine || '-' }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<style scoped>
.recherche-page {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

h1 {
    color: #333;
    margin-bottom: 30px;
    text-align: center;
}

h2 {
    color: #555;
    margin-bottom: 20px;
    font-size: 18px;
}

.recherche-container {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    margin-bottom: 30px;
}

.formulaire {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.form-group {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.form-group label {
    font-weight: 600;
    color: #333;
    font-size: 14px;
}

.form-group input {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    transition: border-color 0.3s;
}

.form-group input:focus {
    outline: none;
    border-color: #4CAF50;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
}

.boutons {
    display: flex;
    gap: 10px;
    margin-top: 10px;
}

.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    font-weight: 600;
    transition: all 0.3s;
}

.btn-rechercher {
    background-color: #4CAF50;
    color: white;
    flex: 1;
}

.btn-rechercher:hover:not(:disabled) {
    background-color: #45a049;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn-rechercher:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.btn-reinitialiser {
    background-color: #f0f0f0;
    color: #333;
    flex: 1;
}

.btn-reinitialiser:hover {
    background-color: #e0e0e0;
    transform: translateY(-2px);
}

.alert {
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    font-weight: 500;
}

.alert-error {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.resultats {
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.resultats-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
}

.resultats-table thead {
    background-color: #4CAF50;
    color: white;
}

.resultats-table thead th {
    padding: 12px;
    text-align: left;
    font-weight: 600;
}

.resultats-table tbody tr {
    border-bottom: 1px solid #ddd;
}

.resultats-table tbody tr:hover {
    background-color: #f5f5f5;
}

.resultats-table tbody td {
    padding: 12px;
}

.resultats-table tbody tr:nth-child(even) {
    background-color: #f9f9f9;
}
</style>
