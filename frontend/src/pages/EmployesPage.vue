<script>
import axios from 'axios';

export default {
    data() {
        return {
            // Affichage
            mode: 'domaines', // 'domaines' ou 'recherche'
            
            // Données par domaines
            domaines: [],
            domainesLoading: false,
            domainesError: null,
            
            // Filtres de recherche
            filtres: {
                nom: '',
                prenom: '',
                domaine: '',
                poste: '',
                ageMin: '',
                ageMax: '',
                sortBy: 'anciennete',
                sortDirection: 'DESC'
            },
            // Listes pour les dropdowns
            listedomainesFiltre: [],
            listepostes: [],
            
            // Résultats de recherche
            employes: [],
            loading: false,
            error: null,
        }
    },
    async created() {
        await this.chargerDomaines();
        await this.chargerPostes();
    },
    watch: {
        'filtres.nom'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.prenom'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.domaine'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.poste'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.ageMin'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.ageMax'(newVal) {
            if (newVal || this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.sortBy'(newVal) {
            if (this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        },
        'filtres.sortDirection'(newVal) {
            if (this.obtenirCriteresActifs().length > 0) {
                this.effectuerRecherche();
            }
        }
    },
    methods: {
        async chargerDomaines() {
            this.domainesLoading = true;
            this.domainesError = null;

            try {
                const response = await axios.get('/employes/domaines');
                this.domaines = response.data;
                // Extraire les libellés des domaines pour le filtre de recherche
                this.listedomainesFiltre = response.data.map(d => ({
                    id: d.idDomaine,
                    libelle: d.libelleDomaine
                }));
            } catch (error) {
                this.domainesError = error.message;
                console.error('Erreur lors du chargement des domaines:', error);
            } finally {
                this.domainesLoading = false;
            }
        },
        async chargerPostes() {
            try {
                const response = await axios.get('/employes/postes');
                console.log('Postes chargés:', response.data);
                this.listepostes = response.data;
            } catch (error) {
                console.warn('Erreur lors du chargement des postes:', error);
            }
        },
        obtenirCriteresActifs() {
            const criteres = [];
            if (this.filtres.nom) criteres.push({ cle: 'nom', label: 'Nom', valeur: this.filtres.nom });
            if (this.filtres.prenom) criteres.push({ cle: 'prenom', label: 'Prénom', valeur: this.filtres.prenom });
            if (this.filtres.domaine) criteres.push({ cle: 'domaine', label: 'Domaine', valeur: this.filtres.domaine });
            if (this.filtres.poste) criteres.push({ cle: 'poste', label: 'Poste', valeur: this.filtres.poste });
            if (this.filtres.ageMin) criteres.push({ cle: 'ageMin', label: 'Âge min', valeur: this.filtres.ageMin });
            if (this.filtres.ageMax) criteres.push({ cle: 'ageMax', label: 'Âge max', valeur: this.filtres.ageMax });
            return criteres;
        },
        supprimerCritere(cle) {
            this.filtres[cle] = '';
            // La recherche se fera automatiquement via le watch
            // Si tous les critères sont supprimés, revenir à l'affichage par domaines
            if (this.obtenirCriteresActifs().length === 0) {
                this.mode = 'domaines';
                this.employes = [];
            }
        },
        async effectuerRecherche() {
            this.loading = true;
            this.error = null;
            this.mode = 'recherche'; // Changer le mode pour afficher les résultats

            try {
                // Construire l'objet criteria en supprimant les valeurs vides
                const criteria = {};
                if (this.filtres.nom) criteria.nom = this.filtres.nom;
                if (this.filtres.prenom) criteria.prenom = this.filtres.prenom;
                if (this.filtres.domaine) criteria.domaine = this.filtres.domaine;
                if (this.filtres.poste) criteria.poste = this.filtres.poste;
                if (this.filtres.ageMin) criteria.ageMin = parseInt(this.filtres.ageMin);
                if (this.filtres.ageMax) criteria.ageMax = parseInt(this.filtres.ageMax);
                if (this.filtres.sortBy) criteria.sortBy = this.filtres.sortBy;
                if (this.filtres.sortDirection) criteria.sortDirection = this.filtres.sortDirection;

                console.log('Critères envoyés:', criteria);
                const response = await axios.post('/employes/recherche', criteria);
                this.employes = response.data;

                if (this.employes.length === 0) {
                    this.error = 'Aucun employé ne correspond à votre recherche.';
                }
            } catch (error) {
                this.error = error.response?.data?.message || error.message || 'Erreur lors de la recherche';
                console.error('Erreur:', error);
            } finally {
                this.loading = false;
            }
        },
        reinitialiserFiltres() {
            this.filtres = {
                nom: '',
                prenom: '',
                domaine: '',
                poste: '',
                ageMin: '',
                ageMax: '',
                sortBy: 'anciennete',
                sortDirection: 'DESC'
            };
            this.employes = [];
            this.error = null;
            this.mode = 'domaines'; // Revenir à l'affichage par domaines
        },
        allerAuxDetails(idEmploye) {
            console.log('Navigation vers détails:', idEmploye);
            this.$router.push({ name: 'EmployeDetails', params: { id: idEmploye } });
        }
    }
}
</script>

<template>
    <div class="employes-container">
        <h1>Gestion des employés</h1>

        <!-- FILTRES EN HAUT (TOUJOURS VISIBLES) -->
        <div class="filtres-section">
            <h2>Recherche et Filtres</h2>
            <form @submit.prevent="effectuerRecherche" class="filtres-form">
                <div class="filtres-grid">
                    <!-- Nom -->
                    <div class="form-group">
                        <label for="nom">Nom</label>
                        <input
                            v-model="filtres.nom"
                            type="text"
                            id="nom"
                            placeholder="Ex: Dupont"
                        />
                    </div>

                    <!-- Prénom -->
                    <div class="form-group">
                        <label for="prenom">Prénom</label>
                        <input
                            v-model="filtres.prenom"
                            type="text"
                            id="prenom"
                            placeholder="Ex: Jean"
                        />
                    </div>

                    <!-- Domaine (dropdown) -->
                    <div class="form-group">
                        <label for="domaine">Domaine</label>
                        <select v-model="filtres.domaine" id="domaine">
                            <option value="">-- Tous les domaines --</option>
                            <option v-for="domaine in listedomainesFiltre" :key="domaine.id" :value="domaine.libelle">
                                {{ domaine.libelle }}
                            </option>
                        </select>
                    </div>

                    <!-- Poste (dropdown) -->
                    <div class="form-group">
                        <label for="poste">Poste</label>
                        <select v-model="filtres.poste" id="poste">
                            <option value="">-- Tous les postes --</option>
                            <option v-for="poste in listepostes" :key="poste.id" :value="poste.libelle">
                                {{ poste.libelle }}
                            </option>
                        </select>
                    </div>

                    <!-- Âge min -->
                    <div class="form-group">
                        <label for="ageMin">Âge minimum</label>
                        <input
                            v-model="filtres.ageMin"
                            type="number"
                            id="ageMin"
                            min="0"
                            max="100"
                            placeholder="Ex: 25"
                        />
                    </div>

                    <!-- Âge max -->
                    <div class="form-group">
                        <label for="ageMax">Âge maximum</label>
                        <input
                            v-model="filtres.ageMax"
                            type="number"
                            id="ageMax"
                            min="0"
                            max="100"
                            placeholder="Ex: 50"
                        />
                    </div>

                    <!-- Tri -->
                    <div class="form-group form-group-small">
                        <label for="sortBy">Trier par</label>
                        <select v-model="filtres.sortBy" id="sortBy">
                            <option value="anciennete">Ancienneté</option>
                            <option value="age">Âge</option>
                        </select>
                    </div>

                    <!-- Direction du tri -->
                    <div class="form-group form-group-small">
                        <label for="sortDirection">Ordre</label>
                        <select v-model="filtres.sortDirection" id="sortDirection">
                            <option value="ASC">Croissant</option>
                            <option value="DESC">Décroissant</option>
                        </select>
                    </div>
                </div>

                <!-- Boutons -->
                <div class="filtres-buttons">
                    <button type="submit" class="btn btn-primary" :disabled="loading" v-show="false">
                        <span v-if="loading">⏳ Recherche en cours...</span>
                        <span v-else>🔍 Rechercher</span>
                    </button>
                    <button v-if="mode === 'recherche'" type="button" @click="reinitialiserFiltres" class="btn btn-secondary">
                        ↻ Réinitialiser tous les critères
                    </button>
                </div>
            </form>

            <!-- CRITÈRES ACTIFS -->
            <div v-if="obtenirCriteresActifs().length > 0" class="criteres-actifs">
                <strong>Critères appliqués:</strong>
                <div class="criteres-list">
                    <div v-for="critere in obtenirCriteresActifs()" :key="critere.cle" class="critere-tag">
                        <span>{{ critere.label }}: <strong>{{ critere.valeur }}</strong></span>
                        <button type="button" @click="supprimerCritere(critere.cle)" class="btn-remove" title="Supprimer ce critère">
                            ✕
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- AFFICHAGE PAR DOMAINES (PAR DÉFAUT) -->
        <div v-if="mode === 'domaines'" class="contenu-section">
            <h2>📋 Liste des employés par domaines</h2>
            
            <div v-if="domainesLoading" style="text-align: center; padding: 20px;">
                🔄 Chargement des domaines...
            </div>
            
            <div v-else-if="domainesError" class="alert alert-error">
                ❌ Erreur: {{ domainesError }}
            </div>

            <div v-else class="domaines-container">
                <table v-for="domaine in domaines" :key="domaine.idDomaine" class="domaine-table">
                    <thead>
                        <tr>
                            <th colspan="4">{{ domaine.libelleDomaine }}</th>
                        </tr>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Poste Actuel</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template v-if="domaine.employes && domaine.employes.length > 0">
                            <tr v-for="employe in domaine.employes" :key="employe.idEmploye">
                                <td>{{ employe.nom }}</td>
                                <td>{{ employe.prenom }}</td>
                                <td>{{ employe.posteActuel }}</td>
                                <td>
                                    <button @click="allerAuxDetails(employe.idEmploye)" class="btn btn-small btn-info">👁️ Voir détails</button>
                                </td>
                            </tr>
                        </template>
                        <template v-else>
                            <tr>
                                <td colspan="4" style="text-align: center; color: #7f8c8d;">Aucun employé dans ce domaine.</td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- RÉSULTATS DE RECHERCHE -->
        <div v-else-if="mode === 'recherche'" class="resultats-section">
            <h2>📊 Résultats de recherche</h2>
            
            <!-- Messages -->
            <div v-if="error" class="alert alert-error">
                ❌ {{ error }}
            </div>

            <div v-if="employes.length > 0" class="alert alert-success">
                ✅ {{ employes.length }} employé(s) trouvé(s)
            </div>

            <!-- Tableau des résultats -->
            <div v-if="employes.length > 0" class="resultats-table">
                <table class="domaine-table">
                    <thead>
                        <tr>
                            <th colspan="6">Résultats</th>
                        </tr>
                        <tr>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Domaine</th>
                            <th>Poste</th>
                            <th>Âge</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="employe in employes" :key="employe.id">
                            <td>{{ employe.nom }}</td>
                            <td>{{ employe.prenom }}</td>
                            <td>{{ employe.domaine }}</td>
                            <td>{{ employe.posteActuel }}</td>
                            <td>{{ employe.age }}</td>
                            <td>
                                <button @click="allerAuxDetails(employe.id)" class="btn btn-small btn-info">👁️ Voir détails</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<style scoped>
.employes-container {
    background: white;
    border-radius: 8px;
    padding: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h1 {
    color: #2c3e50;
    margin-bottom: 30px;
    text-align: center;
}

h2 {
    color: #34495e;
    font-size: 18px;
    margin-bottom: 20px;
    border-bottom: 2px solid #4CAF50;
    padding-bottom: 10px;
}

/* FILTRES */
.filtres-section {
    margin-bottom: 40px;
    background-color: #ecf0f1;
    padding: 20px;
    border-radius: 8px;
}

.filtres-form {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.filtres-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.form-group {
    display: flex;
    flex-direction: column;
}

.form-group-small {
    grid-column: span 1;
}

.form-group label {
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 5px;
    font-size: 14px;
}

.form-group-small label {
    font-size: 12px;
}

.form-group input,
.form-group select {
    padding: 10px 12px;
    border: 1px solid #bdc3c7;
    border-radius: 4px;
    font-size: 14px;
    font-family: inherit;
    transition: border-color 0.3s;
    background-color: white;
}

.form-group-small input,
.form-group-small select {
    padding: 8px 10px;
    font-size: 12px;
}

.form-group input:focus,
.form-group select:focus {
    outline: none;
    border-color: #4CAF50;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
}

.form-group input::placeholder {
    color: #95a5a6;
}

.filtres-buttons {
    display: flex;
    gap: 10px;
    justify-content: flex-start;
    flex-wrap: wrap;
}

/* CRITÈRES ACTIFS */
.criteres-actifs {
    margin-top: 20px;
    padding: 15px;
    background-color: #d4edda;
    border: 1px solid #c3e6cb;
    border-radius: 4px;
}

.criteres-actifs strong {
    display: block;
    margin-bottom: 10px;
    color: #155724;
}

.criteres-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

.critere-tag {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    background-color: white;
    border: 1px solid #c3e6cb;
    border-radius: 20px;
    padding: 6px 12px;
    color: #155724;
    font-size: 13px;
}

.critere-tag span {
    display: flex;
    align-items: center;
    gap: 4px;
}

.btn-remove {
    background: none;
    border: none;
    color: #c82333;
    font-size: 16px;
    cursor: pointer;
    padding: 0;
    margin: 0;
    transition: color 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 20px;
    height: 20px;
}

.btn-remove:hover {
    color: #a71d2a;
}

/* BOUTONS */
.btn {
    padding: 12px 20px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s;
    display: inline-flex;
    align-items: center;
    gap: 5px;
}

.btn:disabled {
    opacity: 0.6;
    cursor: not-allowed;
}

.btn-primary {
    background-color: #4CAF50;
    color: white;
}

.btn-primary:hover:not(:disabled) {
    background-color: #45a049;
    box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.btn-secondary {
    background-color: #95a5a6;
    color: white;
}

.btn-secondary:hover {
    background-color: #7f8c8d;
}

.btn-small {
    padding: 6px 12px;
    font-size: 12px;
}

.btn-info {
    background-color: #3498db;
    color: white;
    padding: 8px 12px;
    font-size: 12px;
}

.btn-info:hover {
    background-color: #2980b9;
}

/* ALERTES */
.alert {
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    border-left: 4px solid;
}

.alert-error {
    background-color: #f8d7da;
    color: #721c24;
    border-color: #f5c6cb;
}

.alert-success {
    background-color: #d4edda;
    color: #155724;
    border-color: #c3e6cb;
}

/* CONTENU PAR DOMAINES */
.contenu-section {
    margin-top: 30px;
    padding-top: 30px;
    border-top: 2px solid #ecf0f1;
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
    background-color: #3498db;
    color: white;
}

.domaine-table thead tr:first-child th {
    padding: 15px;
    font-size: 16px;
    font-weight: bold;
}

.domaine-table thead tr:last-child th {
    padding: 10px;
    border-bottom: 2px solid #2980b9;
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

/* RÉSULTATS DE RECHERCHE */
.resultats-section {
    margin-top: 30px;
    padding-top: 30px;
    border-top: 2px solid #ecf0f1;
}

.resultats-table {
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
}

thead {
    background-color: #34495e;
    color: white;
}

th {
    padding: 12px;
    text-align: left;
    font-weight: 600;
    font-size: 14px;
}

td {
    padding: 12px;
    border-bottom: 1px solid #ecf0f1;
    font-size: 14px;
}

tbody tr:hover {
    background-color: #f5f5f5;
}

tbody tr:nth-child(even) {
    background-color: #fafafa;
}

/* RESPONSIVE */
@media (max-width: 768px) {
    .employes-container {
        padding: 15px;
    }

    .filtres-grid {
        grid-template-columns: 1fr;
    }

    .filtres-buttons {
        flex-direction: column;
    }

    .btn {
        width: 100%;
        justify-content: center;
    }

    table {
        font-size: 12px;
    }

    th, td {
        padding: 8px;
    }
}
</style>
