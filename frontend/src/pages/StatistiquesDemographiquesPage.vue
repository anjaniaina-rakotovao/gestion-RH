<script>
import axios from 'axios';

export default {
    data() {
        return {
            // Filtres
            anneeSelectionnee: new Date().getFullYear(),
            moisSelectionne: 12, // Par défaut Décembre pour l'année globale
            modeAffichage: 'annee', // 'annee' ou 'mois'

            // Données
            statistiquesAnnee: null,
            statistiquesMois: [],
            toutesStatistiques: [], // Toutes les stats de tous les mois
            comparaisonAnnees: [], // Pour la comparaison multi-année
            loading: false,
            error: null,

            // Années disponibles
            anneesDisponibles: [],
            moisDisponibles: [
                { num: 1, label: 'Janvier' },
                { num: 2, label: 'Février' },
                { num: 3, label: 'Mars' },
                { num: 4, label: 'Avril' },
                { num: 5, label: 'Mai' },
                { num: 6, label: 'Juin' },
                { num: 7, label: 'Juillet' },
                { num: 8, label: 'Août' },
                { num: 9, label: 'Septembre' },
                { num: 10, label: 'Octobre' },
                { num: 11, label: 'Novembre' },
                { num: 12, label: 'Décembre' }
            ]
        }
    },
    async created() {
        // Initialiser les années disponibles (5 dernières années)
        const anneeActuelle = new Date().getFullYear();
        for (let i = 0; i < 5; i++) {
            this.anneesDisponibles.push(anneeActuelle - i);
        }

        await this.chargerStatistiques();
    },
    watch: {
        anneeSelectionnee() {
            this.chargerStatistiques();
        },
        moisSelectionne() {
            if (this.moisSelectionne !== null) {
                this.chargerStatistiques();
            }
        }
    },
    methods: {
        async chargerStatistiques() {
            this.loading = true;
            this.error = null;

            try {
                // Charger les statistiques du mois sélectionné (décembre par défaut)
                const response = await axios.get(
                    `/employes/statistiques/demographique/${this.anneeSelectionnee}/${this.moisSelectionne || 12}`
                );
                this.statistiquesAnnee = response.data;
                this.modeAffichage = 'mois';

                // Charger aussi tous les mois de l'année pour le graphique en barres
                await this.chargerTousLesMois();

                console.log('Statistiques chargées:', this.statistiquesAnnee);
            } catch (error) {
                this.error = error.response?.data?.message || error.message || 'Erreur lors du chargement';
                console.error('Erreur:', error);
            } finally {
                this.loading = false;
            }
        },
        async chargerTousLesMois() {
            try {
                // Charger toutes les statistiques (tous les mois de toutes les années)
                const response = await axios.get('/employes/statistiques/demographique/tous');
                const tousLesDonnees = response.data;

                // Filtrer pour l'année sélectionnée
                // La data a "periode": "YYYY-MM", on doit extraire l'année
                this.toutesStatistiques = tousLesDonnees
                    .filter(stat => {
                        const annee = parseInt(stat.periode.substring(0, 4));
                        return annee === this.anneeSelectionnee;
                    })
                    .map(stat => {
                        // Ajouter les propriétés annee et mois pour utilisation ultérieure
                        const [annee, mois] = stat.periode.split('-');
                        return {
                            ...stat,
                            annee: parseInt(annee),
                            mois: parseInt(mois)
                        };
                    });

                // Trier par mois
                this.toutesStatistiques.sort((a, b) => a.mois - b.mois);

                console.log('Tous les mois chargés:', this.toutesStatistiques);
            } catch (error) {
                console.error('Erreur lors du chargement des mois:', error);
            }
        },
        async chargerComparaisonAnnees() {
            try {
                // Charger les statistiques pour chaque année (pour la barre de comparaison)
                const promesses = this.anneesDisponibles.map(annee =>
                    axios.get(`/employes/statistiques/demographique/${annee}/12`)
                        .then(res => ({ annee, data: res.data }))
                        .catch(() => ({ annee, data: null }))
                );

                const resultats = await Promise.all(promesses);
                this.comparaisonAnnees = resultats.filter(r => r.data !== null);
                console.log('Comparaison années chargées:', this.comparaisonAnnees);
            } catch (error) {
                console.error('Erreur lors de la comparaison:', error);
            }
        },
        reinitialiser() {
            this.moisSelectionne = 12; // Réinitialiser à Décembre
            this.chargerStatistiques();
        },
        obtenirColorAges(groupe) {
            switch (groupe) {
                case 'age18_20': return '#FF6B6B'; // Rouge
                case 'age20_40': return '#4ECDC4'; // Turquoise
                case 'age40_60': return '#FFD93D'; // Jaune
                case 'age60Plus': return '#95E1D3'; // Vert clair
                default: return '#ccc';
            }
        },
        obtenirPercentageSexe(data, sexe) {
            if (!data || data.totalEmployes === 0) return 0;
            const count = sexe === 'femme' ? data.nombreFemmes : data.nombreHommes;
            return (count / data.totalEmployes) * 100;
        },
        obtenirNomMois(mois) {
            const moisList = ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'];
            return moisList[mois - 1] || '';
        }
    }
}
</script>

<template>
    <div class="stats-container">
        <h1>📊 Statistiques Démographiques</h1>

        <!-- FILTRES -->
        <div class="filtres-section">
            <div class="filtre-group">
                <label for="annee">Année:</label>
                <select v-model.number="anneeSelectionnee" id="annee" class="select-filtre">
                    <option v-for="annee in anneesDisponibles" :key="annee" :value="annee">
                        {{ annee }}
                    </option>
                </select>
            </div>

            <div class="filtre-group">
                <label for="mois">Mois (optionnel):</label>
                <select v-model.number="moisSelectionne" id="mois" class="select-filtre">
                    <option :value="null">-- Toute l'année --</option>
                    <option v-for="mois in moisDisponibles" :key="mois.num" :value="mois.num">
                        {{ mois.label }}
                    </option>
                </select>
            </div>

            <button v-if="moisSelectionne !== null" @click="reinitialiser" class="btn btn-secondary">
                Réinitialiser
            </button>
        </div>

        <!-- CHARGEMENT / ERREUR -->
        <div v-if="loading" style="text-align: center; padding: 40px;">
            🔄 Chargement des statistiques...
        </div>

        <div v-else-if="error" class="alert alert-error">
            ❌ {{ error }}
        </div>

        <!-- CONTENU STATISTIQUES -->
        <div v-else-if="statistiquesAnnee" class="stats-content">
            <!-- INFO GLOBALE -->
            <div class="info-globale">
                <div class="info-card">
                    <h3>Période</h3>
                    <p class="big-number">{{ statistiquesAnnee.periode }}</p>
                </div>
                <div class="info-card">
                    <h3>Total Employés</h3>
                    <p class="big-number">{{ statistiquesAnnee.totalEmployes }}</p>
                </div>
            </div>

            <!-- STATISTIQUES SEXE -->
            <div class="stats-section">
                <h2>👥 Répartition par Sexe</h2>
                <div class="sexe-container">
                    <!-- GRAPHIQUE CAMEMBERT SIMPLIFIÉ -->
                    <div class="pie-chart-container">
                        <svg viewBox="0 0 200 200" class="pie-chart">
                            <!-- Femmes -->
                            <circle v-if="statistiquesAnnee.pourcentageFemmes > 0" cx="100" cy="100" r="80"
                                :style="{ strokeDasharray: `${statistiquesAnnee.pourcentageFemmes * 5.02} 502`, stroke: '#FF69B4', strokeWidth: '30' }"
                                fill="none" class="pie-segment" />
                            <!-- Hommes -->
                            <circle v-if="statistiquesAnnee.pourcentageHommes > 0" cx="100" cy="100" r="80"
                                :style="{ strokeDasharray: `${statistiquesAnnee.pourcentageHommes * 5.02} 502`, strokeDashoffset: `${-statistiquesAnnee.pourcentageFemmes * 5.02}`, stroke: '#3498DB', strokeWidth: '30' }"
                                fill="none" class="pie-segment" />
                        </svg>
                    </div>

                    <!-- LÉGENDE SEXE -->
                    <div class="sexe-legende">
                        <div class="sexe-item">
                            <div class="sexe-color" style="background-color: #FF69B4;"></div>
                            <div>
                                <p class="sexe-label">Femmes</p>
                                <p class="sexe-nombre">{{ statistiquesAnnee.nombreFemmes }} ({{
                                    statistiquesAnnee.pourcentageFemmes.toFixed(1) }}%)</p>
                            </div>
                        </div>
                        <div class="sexe-item">
                            <div class="sexe-color" style="background-color: #3498DB;"></div>
                            <div>
                                <p class="sexe-label">Hommes</p>
                                <p class="sexe-nombre">{{ statistiquesAnnee.nombreHommes }} ({{
                                    statistiquesAnnee.pourcentageHommes.toFixed(1) }}%)</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- STATISTIQUES AGES -->
            <div class="stats-section">
                <h2>🎂 Répartition par Âge</h2>
                <div class="ages-bar-chart">
                    <div class="age-group">
                        <div class="age-label">18-20 ans</div>
                        <div class="bar-container">
                            <div class="bar" :style="{
                                width: `${(statistiquesAnnee.age18_20 / statistiquesAnnee.totalEmployes) * 100 || 0}%`,
                                backgroundColor: '#FF6B6B'
                            }">
                                <span v-if="statistiquesAnnee.age18_20 > 0" class="bar-value">{{
                                    statistiquesAnnee.age18_20 }}</span>
                            </div>
                            <span class="bar-total">{{ statistiquesAnnee.age18_20 }}</span>
                        </div>
                    </div>

                    <div class="age-group">
                        <div class="age-label">20-40 ans</div>
                        <div class="bar-container">
                            <div class="bar" :style="{
                                width: `${(statistiquesAnnee.age20_40 / statistiquesAnnee.totalEmployes) * 100 || 0}%`,
                                backgroundColor: '#4ECDC4'
                            }">
                                <span v-if="statistiquesAnnee.age20_40 > 0" class="bar-value">{{
                                    statistiquesAnnee.age20_40 }}</span>
                            </div>
                            <span class="bar-total">{{ statistiquesAnnee.age20_40 }}</span>
                        </div>
                    </div>

                    <div class="age-group">
                        <div class="age-label">40-60 ans</div>
                        <div class="bar-container">
                            <div class="bar" :style="{
                                width: `${(statistiquesAnnee.age40_60 / statistiquesAnnee.totalEmployes) * 100 || 0}%`,
                                backgroundColor: '#FFD93D'
                            }">
                                <span v-if="statistiquesAnnee.age40_60 > 0" class="bar-value">{{
                                    statistiquesAnnee.age40_60 }}</span>
                            </div>
                            <span class="bar-total">{{ statistiquesAnnee.age40_60 }}</span>
                        </div>
                    </div>

                    <div class="age-group">
                        <div class="age-label">60+ ans</div>
                        <div class="bar-container">
                            <div class="bar" :style="{
                                width: `${(statistiquesAnnee.age60Plus / statistiquesAnnee.totalEmployes) * 100 || 0}%`,
                                backgroundColor: '#95E1D3'
                            }">
                                <span v-if="statistiquesAnnee.age60Plus > 0" class="bar-value">{{
                                    statistiquesAnnee.age60Plus }}</span>
                            </div>
                            <span class="bar-total">{{ statistiquesAnnee.age60Plus }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- COMPARAISON MULTI-MOIS DE L'ANNÉE -->
            <div class="stats-section" v-if="toutesStatistiques.length > 0">
                <h2>📈 Répartition Hommes/Femmes par Mois ({{ anneeSelectionnee }})</h2>
                <div class="bar-chart-vertical-container">
                    <div v-for="moisData in toutesStatistiques" :key="moisData.mois" class="bar-chart-month">
                        <div class="month-title">{{ obtenirNomMois(moisData.mois) }}</div>
                        <div class="bars-container">
                            <!-- Barre Femmes -->
                            <div class="bar-vertical-group">
                                <div class="bar-vertical-femmes bar-vertical"
                                    :style="{ height: `${obtenirPercentageSexe(moisData, 'femme')}%` }"
                                    :title="`Femmes: ${moisData.nombreFemmes} (${obtenirPercentageSexe(moisData, 'femme').toFixed(1)}%)`">
                                    <span v-if="obtenirPercentageSexe(moisData, 'femme') > 5" class="bar-value">
                                        {{ moisData.nombreFemmes }}
                                    </span>
                                </div>
                                <div class="bar-label">👩</div>
                            </div>

                            <!-- Barre Hommes -->
                            <div class="bar-vertical-group">
                                <div class="bar-vertical-hommes bar-vertical"
                                    :style="{ height: `${obtenirPercentageSexe(moisData, 'homme')}%` }"
                                    :title="`Hommes: ${moisData.nombreHommes} (${obtenirPercentageSexe(moisData, 'homme').toFixed(1)}%)`">
                                    <span v-if="obtenirPercentageSexe(moisData, 'homme') > 5" class="bar-value">
                                        {{ moisData.nombreHommes }}
                                    </span>
                                </div>
                                <div class="bar-label">👨</div>
                            </div>
                        </div>
                        <div class="month-total">
                            Total: {{ moisData.totalEmployes }}
                        </div>
                    </div>
                </div>
            </div>
            <div v-else-if="!loading" class="stats-section">
                <p style="text-align: center; color: #95a5a6;">Aucune donnée disponible pour cette année</p>
                <details style="text-align: center; color: #7f8c8d; margin-top: 10px;">
                    <summary>Debug</summary>
                    <p>toutesStatistiques: {{ toutesStatistiques.length }} items</p>
                    <p>Données brutes: {{ toutesStatistiques }}</p>
                </details>
            </div>
        </div>
    </div>
</template>

<style scoped>
.stats-container {
    background: white;
    border-radius: 8px;
    padding: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h1 {
    color: #2c3e50;
    text-align: center;
    margin-bottom: 30px;
}

h2 {
    color: #34495e;
    font-size: 18px;
    margin-bottom: 20px;
    border-bottom: 2px solid #3498db;
    padding-bottom: 10px;
}

/* FILTRES */
.filtres-section {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
    padding: 20px;
    background-color: #ecf0f1;
    border-radius: 8px;
    align-items: flex-end;
    flex-wrap: wrap;
}

.filtre-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
}

.filtre-group label {
    font-weight: 600;
    color: #2c3e50;
    font-size: 14px;
}

.select-filtre {
    padding: 10px 12px;
    border: 1px solid #bdc3c7;
    border-radius: 4px;
    font-size: 14px;
    font-family: inherit;
    background-color: white;
    cursor: pointer;
    min-width: 180px;
}

.select-filtre:focus {
    outline: none;
    border-color: #3498db;
    box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
}

.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s;
}

.btn-secondary {
    background-color: #95a5a6;
    color: white;
}

.btn-secondary:hover {
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

/* CONTENU */
.stats-content {
    display: flex;
    flex-direction: column;
    gap: 30px;
}

/* INFO GLOBALE */
.info-globale {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
}

.info-card {
    background-color: #ecf0f1;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
    border-left: 4px solid #3498db;
}

.info-card h3 {
    color: #34495e;
    margin: 0 0 10px 0;
    font-size: 14px;
}

.big-number {
    color: #3498db;
    font-size: 32px;
    font-weight: bold;
    margin: 0;
}

/* SECTION STATISTIQUES */
.stats-section {
    background-color: #f8f9fa;
    padding: 25px;
    border-radius: 8px;
}

/* GRAPHIQUE SEXE */
.sexe-container {
    display: flex;
    gap: 40px;
    align-items: center;
    justify-content: center;
    flex-wrap: wrap;
}

.pie-chart-container {
    width: 200px;
    height: 200px;
}

.pie-chart {
    width: 100%;
    height: 100%;
    transform: rotate(-90deg);
}

.pie-segment {
    transition: all 0.3s;
}

.pie-segment:hover {
    filter: brightness(1.1);
}

.sexe-legende {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.sexe-item {
    display: flex;
    gap: 15px;
    align-items: center;
}

.sexe-color {
    width: 40px;
    height: 40px;
    border-radius: 8px;
}

.sexe-label {
    color: #34495e;
    font-weight: 600;
    margin: 0 0 5px 0;
    font-size: 14px;
}

.sexe-nombre {
    color: #7f8c8d;
    margin: 0;
    font-size: 13px;
}

/* GRAPHIQUE AGES */
.ages-bar-chart {
    display: flex;
    flex-direction: column;
    gap: 20px;
}

.age-group {
    display: flex;
    align-items: center;
    gap: 15px;
}

.age-label {
    min-width: 80px;
    font-weight: 600;
    color: #34495e;
    font-size: 14px;
}

.bar-container {
    flex: 1;
    display: flex;
    align-items: center;
    gap: 10px;
}

.bar {
    height: 35px;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding-right: 10px;
    min-width: 40px;
    transition: all 0.3s;
    position: relative;
}

.bar:hover {
    filter: brightness(0.9);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.bar-value {
    color: white;
    font-weight: 600;
    font-size: 13px;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
}

.bar-total {
    min-width: 30px;
    text-align: right;
    font-weight: 600;
    color: #34495e;
    font-size: 14px;
}

/* COMPARAISON MULTI-MOIS */
.mois-comparaison-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
}

.mois-comparaison {
    background-color: white;
    padding: 18px;
    border-radius: 8px;
    border-left: 4px solid #3498db;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    transition: all 0.3s;
}

.mois-comparaison:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.mois-titre {
    font-size: 16px;
    font-weight: bold;
    color: #2c3e50;
    margin-bottom: 12px;
    text-align: center;
}

.barre-sexe-horizontal {
    display: flex;
    height: 50px;
    border-radius: 4px;
    overflow: hidden;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    margin-bottom: 12px;
}

.sexe-barre {
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s;
    position: relative;
}

.sexe-barre:hover {
    filter: brightness(0.9);
}

.barre-text {
    color: white;
    font-weight: bold;
    font-size: 13px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.mois-stats {
    display: flex;
    gap: 10px;
    justify-content: center;
    padding-top: 8px;
    font-size: 12px;
}

.stat-femmes {
    color: #FF69B4;
    font-weight: 600;
}

.stat-hommes {
    color: #3498DB;
    font-weight: 600;
}

.stat-total {
    color: #95a5a6;
    font-weight: 600;
}

/* DIAGRAMME EN BARRES VERTICALES */
.bar-chart-vertical-container {
    display: flex;
    justify-content: space-around;
    align-items: flex-end;
    gap: 15px;
    height: 300px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    border: 1px solid #e1e8ed;
}

.bar-chart-month {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    flex: 1;
    max-width: 80px;
}

.month-title {
    font-size: 12px;
    font-weight: 600;
    color: #2c3e50;
    text-align: center;
    margin-bottom: 5px;
}

.bars-container {
    display: flex;
    gap: 8px;
    align-items: flex-end;
    height: 200px;
    width: 100%;
}

.bar-vertical-group {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 5px;
    flex: 1;
}

.bar-vertical {
    width: 25px;
    min-height: 3px;
    border-radius: 4px 4px 0 0;
    display: flex;
    align-items: flex-end;
    justify-content: center;
    transition: all 0.3s ease;
    position: relative;
    cursor: pointer;
}

.bar-vertical:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.bar-vertical-femmes {
    background: linear-gradient(to top, #FF69B4, #FF8EC7);
    border: 1px solid #FF1493;
}

.bar-vertical-hommes {
    background: linear-gradient(to top, #3498DB, #5DADE2);
    border: 1px solid #2980B9;
}

.bar-value {
    color: white;
    font-weight: bold;
    font-size: 10px;
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.5);
    margin-bottom: 2px;
}

.bar-label {
    font-size: 12px;
    color: #7f8c8d;
}

.month-total {
    font-size: 10px;
    color: #95a5a6;
    font-weight: 600;
    text-align: center;
    margin-top: 5px;
}

/* LÉGENDE */
.chart-legend {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 20px;
    padding: 15px;
    background: #f8f9fa;
    border-radius: 8px;
}

.legend-item {
    display: flex;
    align-items: center;
    gap: 8px;
}

.legend-color {
    width: 20px;
    height: 20px;
    border-radius: 4px;
}

.legend-color.femmes {
    background: linear-gradient(to top, #FF69B4, #FF8EC7);
}

.legend-color.hommes {
    background: linear-gradient(to top, #3498DB, #5DADE2);
}

.legend-text {
    font-size: 14px;
    color: #2c3e50;
    font-weight: 500;
}

/* RESPONSIVE */
@media (max-width: 768px) {
    .stats-container {
        padding: 15px;
    }

    .filtres-section {
        flex-direction: column;
        align-items: stretch;
    }

    .filtre-group {
        flex: 1;
    }

    .select-filtre {
        min-width: auto;
    }

    .sexe-container {
        flex-direction: column;
        gap: 20px;
    }

    .age-group {
        flex-direction: column;
        gap: 8px;
    }

    .bar-container {
        width: 100%;
    }

    .bar {
        height: 40px;
    }
    
}
</style>
