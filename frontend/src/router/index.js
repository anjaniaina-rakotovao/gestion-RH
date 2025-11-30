import { createRouter, createWebHistory } from 'vue-router'
import EmployesPage from '../pages/EmployesPage.vue'
import EmployeDetailsPage from '../pages/EmployeDetailsPage.vue'
import StatistiquesDemographiquesPage from '../pages/StatistiquesDemographiquesPage.vue'
import TauxTurnAbs from '../pages/TauxTurnAbs.vue'
import StatistiquesContrat from '../pages/StatistiquesContrat.vue'
import AlerteNotifs from '../pages/AlerteNotifs.vue'
import DocumentsEmploye from '../pages/DocumentsEmploye.vue'
import FichePaieSearch from '../components/EmployeForm.vue'
import ResultPage from '../components/ResultPage.vue'
import Home from "../views/Home.vue";
import Dashboard from "../views/Dashboard.vue";
import PresenceList from "../views/PresenceList.vue";
import DemandeConge from "../views/DemandeConge.vue";
import PendingLeaves from "../views/PendingLeaves.vue";
import ScoringPage from '@/pages/ScoringPage.vue';
import GenererRapport from '@/pages/GenererRapport.vue';
import Competence from '@/pages/Competence.vue';

const routes = [
    {
        path: '/',
        name: 'Accueil',
        component: EmployesPage
    },
    {
        path: '/recherche',
        name: 'Recherche',
        component: EmployesPage
    },
    {
        path: '/employe/:id',
        name: 'EmployeDetails',
        component: EmployeDetailsPage
    },
    {
        path: '/statistiques/demographiques',
        name: 'StatistiquesDemographiques',
        component: StatistiquesDemographiquesPage
    },
    {
        path: '/statistiques/turnover-absenteisme',
        name: 'TauxTurnAbs',
        component: TauxTurnAbs
    },
    {
        path: '/statistiques/contrat',
        name: 'StatistiquesContrat',
        component: StatistiquesContrat
    },
    {
        path: '/alertes-notifications',
        name: 'AlerteNotifs',
        component: AlerteNotifs
    },
    // Dans votre router/index.js
    {
        path: '/employes/:id/documents',
        name: 'DocumentsEmploye',
        component: DocumentsEmploye
    },
    // Nouveaux chemins pour la fiche de paie
    {
        path: '/fiche-paie',
        name: 'EmployeForm',
        component: FichePaieSearch
    },
    {
        path: '/fiche-paie/resultat/:idEmploye/:mois/:annee',
        name: 'ResultPage',
        component: ResultPage,
        props: true
    },
     {
        path: '/scoringPage',
        name: 'ScoringPage',
        component: ScoringPage,
    },

    {
        path: '/genererRapport',
        name: 'GenererRapport',
        component: GenererRapport,
    },
       {
        path: '/competence',
        name: 'Competence',
        component: Competence,
    },
    { path: "/home", name: "home", component: Home },
    { path: "/dashboard", name: "dashboard", component: Dashboard },
    { path: "/presences", name: "presences", component: PresenceList },
    { path: "/conges", name: "conges", component: DemandeConge },
    {path: '/conges/attente', name: "conges attente", component: PendingLeaves }

]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router
