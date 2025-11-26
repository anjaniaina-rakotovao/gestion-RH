<script>
import axios from 'axios';

export default {
    data() {
        return {
            employe: null,
            loading: true,
            error: null,
        }
    },
    async created() {
        console.log('EmployeDetailsPage créée, route params:', this.$route.params);
        await this.chargerDetails();
        await this.chargerNombreDocuments();
    },
    methods: {
        async chargerDetails() {
            this.loading = true;
            this.error = null;

            try {
                const idEmploye = this.$route.params.id;
                const response = await axios.get(`/employes/${idEmploye}/details`);
                this.employe = response.data;
                console.log('Détails employé:', this.employe);
            } catch (error) {
                this.error = error.response?.data?.message || error.message || 'Erreur lors du chargement des détails';
                console.error('Erreur:', error);
            } finally {
                this.loading = false;
            }
        },
                async chargerNombreDocuments() {
            try {
                const idEmploye = this.$route.params.id;
                // Utiliser l'endpoint que nous avons créé
                const response = await axios.get(`/employes/employe/${idEmploye}/count`);
                this.nombreDocuments = response.data;
            } catch (error) {
                console.error('Erreur chargement nombre documents:', error);
                this.nombreDocuments = 0;
            }
        },

        voirDocuments() {
            const idEmploye = this.$route.params.id;
            // Navigation vers la page des documents
            this.$router.push(`/employes/${idEmploye}/documents`);
        },
        retourner() {
            this.$router.back();
        },
        formatDate(dateString) {
            if (!dateString) return '---';
            const date = new Date(dateString);
            return new Intl.DateTimeFormat('fr-FR', {
                year: 'numeric',
                month: 'long',
                day: 'numeric'
            }).format(date);
        }
    }
}
</script>

<template>
    <div class="details-container">
        <button @click="retourner" class="btn btn-back">← Retour</button>

        <div v-if="loading" style="text-align: center; padding: 40px;">
            🔄 Chargement des détails...
        </div>

        <div v-else-if="error" class="alert alert-error">
            ❌ {{ error }}
        </div>

        <div v-else-if="employe" class="details-content">
            <!-- EN-TÊTE AVEC PHOTO ET INFOS GÉNÉRALES -->
            <div class="details-header">
                <div class="photo-section">
                    <img v-if="employe.imagePath" :src="employe.imagePath" :alt="`${employe.prenom} ${employe.nom}`"
                        class="photo-employe">
                    <div v-else class="photo-placeholder">📷</div>
                </div>

                <div class="infos-generales">
                    <h1>{{ employe.prenom }} {{ employe.nom }}</h1>
                    <div class="documents-link">
                        <button @click="voirDocuments" class="btn btn-documents">
                            📁 Voir les documents
                            <span v-if="nombreDocuments > 0" class="documents-count">
                                ({{ nombreDocuments }})
                            </span>
                        </button>
                    </div>
                    <div class="info-row">
                        <span class="label">Sexe:</span>
                        <span class="value">{{ employe.sexe }}</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Date de naissance:</span>
                        <span class="value">{{ formatDate(employe.dateNaissance) }}</span>
                    </div>
                    <div class="info-row">
                        <span class="label">Email:</span>
                        <span class="value">
                            <a :href="`mailto:${employe.mail}`">{{ employe.mail }}</a>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="label">Contact:</span>
                        <span class="value">
                            <a :href="`tel:${employe.contact}`">{{ employe.contact }}</a>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="label">Adresse:</span>
                        <span class="value">{{ employe.adresse }}</span>
                    </div>
                </div>
            </div>

            <!-- SECTION POSTES -->
            <div class="details-section">
                <h2>📋 Historique des Postes</h2>
                <table v-if="employe.historiquesPostes && employe.historiquesPostes.length > 0" class="details-table">
                    <thead>
                        <tr>
                            <th>Poste</th>
                            <th>Domaine</th>
                            <th>Date Début</th>
                            <th>Date Fin</th>
                            <th>Statut</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="poste in employe.historiquesPostes" :key="poste.idEmployePoste">
                            <td><strong>{{ poste.posteLibelle }}</strong></td>
                            <td>{{ poste.domaineLibelle }}</td>
                            <td>{{ formatDate(poste.dateDebut) }}</td>
                            <td>{{ poste.dateFin ? formatDate(poste.dateFin) : '---' }}</td>
                            <td>
                                <span v-if="poste.actuel" class="badge badge-success">✓ Actuel</span>
                                <span v-else class="badge badge-inactive">Ancien</span>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div v-else class="no-data">Aucun poste enregistré.</div>
            </div>

            <!-- SECTION CONTRATS -->
            <div class="details-section">
                <h2>📝 Historique des Contrats</h2>
                <table v-if="employe.historiquesContrats && employe.historiquesContrats.length > 0"
                    class="details-table">
                    <thead>
                        <tr>
                            <th>Type de Contrat</th>
                            <th>Date Début</th>
                            <th>Date Fin</th>
                            <th>Durée (mois)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="contrat in employe.historiquesContrats" :key="contrat.idContrat">
                            <td><strong>{{ contrat.typeContrat }}</strong></td>
                            <td>{{ formatDate(contrat.dateDebut) }}</td>
                            <td>{{ contrat.dateFin ? formatDate(contrat.dateFin) : '---' }}</td>
                            <td>{{ contrat.duree ? contrat.duree : '---' }}</td>
                        </tr>
                    </tbody>
                </table>
                <div v-else class="no-data">Aucun contrat enregistré.</div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.details-container {
    background: white;
    border-radius: 8px;
    padding: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.btn-back {
    background-color: #95a5a6;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    margin-bottom: 20px;
    transition: all 0.3s;
}

.btn-back:hover {
    background-color: #7f8c8d;
}

/* ALERTE */
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

/* EN-TÊTE */
.details-header {
    display: flex;
    gap: 40px;
    margin-bottom: 40px;
    padding: 30px;
    background-color: #f8f9fa;
    border-radius: 8px;
    align-items: flex-start;
}

.photo-section {
    flex-shrink: 0;
}

.photo-employe {
    width: 150px;
    height: 150px;
    border-radius: 8px;
    object-fit: cover;
    border: 3px solid #3498db;
}

.photo-placeholder {
    width: 150px;
    height: 150px;
    border-radius: 8px;
    background-color: #ecf0f1;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 60px;
    border: 3px solid #3498db;
}

.infos-generales {
    flex: 1;
}

.infos-generales h1 {
    color: #2c3e50;
    margin: 0 0 20px 0;
    font-size: 28px;
}

.info-row {
    display: flex;
    margin-bottom: 12px;
    gap: 15px;
    align-items: flex-start;
}

.label {
    font-weight: 600;
    color: #34495e;
    min-width: 180px;
    font-size: 14px;
}

.value {
    color: #2c3e50;
    font-size: 14px;
    word-break: break-word;
}

.value a {
    color: #3498db;
    text-decoration: none;
}

.value a:hover {
    text-decoration: underline;
}

/* SECTIONS */
.details-section {
    margin-bottom: 40px;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
}

.details-section h2 {
    color: #34495e;
    font-size: 18px;
    margin-top: 0;
    margin-bottom: 20px;
    border-bottom: 2px solid #3498db;
    padding-bottom: 10px;
}

/* TABLEAU */
.details-table {
    width: 100%;
    border-collapse: collapse;
    background-color: white;
}

.details-table thead {
    background-color: #3498db;
    color: white;
}

.details-table th {
    padding: 12px;
    text-align: left;
    font-weight: 600;
    font-size: 14px;
}

.details-table td {
    padding: 12px;
    border-bottom: 1px solid #ecf0f1;
    font-size: 14px;
}

.details-table tbody tr:hover {
    background-color: #f5f5f5;
}

.details-table tbody tr:nth-child(even) {
    background-color: #fafafa;
}

/* BADGES */
.badge {
    display: inline-block;
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
}

.badge-success {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.badge-inactive {
    background-color: #e2e3e5;
    color: #383d41;
    border: 1px solid #d6d8db;
}

/* NO DATA */
.no-data {
    padding: 20px;
    text-align: center;
    color: #7f8c8d;
    font-style: italic;
}

/* RESPONSIVE */
@media (max-width: 768px) {
    .details-container {
        padding: 15px;
    }

    .details-header {
        flex-direction: column;
        gap: 20px;
        padding: 20px;
    }

    .photo-employe,
    .photo-placeholder {
        width: 100px;
        height: 100px;
    }

    .infos-generales h1 {
        font-size: 22px;
    }

    .info-row {
        flex-direction: column;
        gap: 5px;
    }

    .label {
        min-width: auto;
    }

    .details-table {
        font-size: 12px;
    }

    .details-table th,
    .details-table td {
        padding: 8px;
    }
}
.documents-link {
    margin-bottom: 20px;
}

.btn-documents {
    background-color: #3498db;
    color: white;
    padding: 12px 24px;
    border: none;
    border-radius: 6px;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s;
    display: inline-flex;
    align-items: center;
    gap: 8px;
}

.btn-documents:hover {
    background-color: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
}

.documents-count {
    background-color: #e74c3c;
    color: white;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 12px;
    font-weight: bold;
    margin-left: 5px;
}
</style>
