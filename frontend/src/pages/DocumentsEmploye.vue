<template>
    <div class="documents-container">
        <div class="documents-header">
            <button @click="retourner" class="btn btn-back">← Retour aux détails</button>
            <h1>📁 Documents de {{ employeNom }}</h1>
        </div>


        <div v-if="loading" class="loading">
            🔄 Chargement des documents...
        </div>

        <div v-else-if="error" class="alert alert-error">
            ❌ {{ error }}
        </div>

        <div v-else-if="documents.length === 0" class="no-documents">
            <div class="no-documents-icon">📄</div>
            <h3>Aucun document</h3>
            <p>L'API a retourné un tableau vide.</p>
            <p class="debug-hint">Vérifiez la console pour les détails du réseau.</p>
        </div>

        <div v-else class="documents-section">
            <div class="documents-grid">
                <div v-for="document in documents" :key="document.idDocEmploye" class="document-card">
                    <div class="document-icon">
                        <span v-if="document.typeDocument === 'CV'">📄</span>
                        <span v-else-if="document.typeDocument === 'Contrat'">📝</span>
                        <span v-else-if="document.typeDocument === 'Diplôme'">🎓</span>
                        <span v-else-if="document.typeDocument === 'Pièce d\'identité'">🆔</span>
                        <span v-else-if="document.typeDocument === 'Photo'">📷</span>
                        <span v-else-if="document.typeDocument === 'Bulletin de salaire'">💰</span>
                        <span v-else>📎</span>
                    </div>
                    
                    <div class="document-info">
                        <h3>{{ document.typeDocument || 'Document' }}</h3>
                        <p class="document-filename">{{ document.nomFichier || 'Sans nom' }}</p>
                        <p class="document-id">ID: {{ document.idDocEmploye }}</p>
                    </div>

                    <div class="document-actions">
                        <button @click="voirDocument(document)" class="btn btn-view">
                            👁️ Voir
                        </button>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'DocumentsEmploye',
    data() {
        return {
            employeNom: '',
            documents: [],
            loading: true,
            error: null,
            apiUrl: '',
            apiResponse: 'Non chargé',
            httpStatus: 'Non connu'
        }
    },
    async created() {
        console.log('📄 Composant DocumentsEmploye créé');
        console.log('📍 URL actuelle:', window.location.href);
        await this.chargerDocuments();
    },
    methods: {
        async chargerDocuments() {
            this.loading = true;
            this.error = null;
            this.apiResponse = 'Chargement...';

            try {
                const idEmploye = this.$route.params.id;
                this.apiUrl = `/employes/employe/${idEmploye}`;
                
                console.log('🔄 Début du chargement');
                console.log('🎯 ID Employé:', idEmploye);
                console.log('🔗 URL API:', this.apiUrl);
                console.log('🌐 URL complète:', window.location.origin + this.apiUrl);
                
                // 1. Charger le nom de l'employé
                console.group('📡 Chargement nom employé');
                const responseEmploye = await axios.get(`/employes/${idEmploye}/details`);
                this.employeNom = `${responseEmploye.data.prenom} ${responseEmploye.data.nom}`;
                console.log('✅ Nom chargé:', this.employeNom);
                console.groupEnd();

                // 2. Charger les documents avec intercepteur de debug
                console.group('📡 Chargement documents');
                
                // Créer une instance axios avec intercepteur pour le debug
                const axiosInstance = axios.create();
                
                // Intercepteur de requête
                axiosInstance.interceptors.request.use(config => {
                    console.log('🚀 Requête envoyée:', {
                        url: config.url,
                        method: config.method,
                        headers: config.headers
                    });
                    return config;
                });
                
                // Intercepteur de réponse
                axiosInstance.interceptors.response.use(response => {
                    console.log('✅ Réponse reçue:', {
                        status: response.status,
                        statusText: response.statusText,
                        headers: response.headers,
                        data: response.data
                    });
                    return response;
                }, error => {
                    console.error('❌ Erreur réponse:', error);
                    return Promise.reject(error);
                });

                const startTime = Date.now();
                const responseDocs = await axiosInstance.get(this.apiUrl);
                const endTime = Date.now();
                
                console.log('⏱️ Temps de réponse:', (endTime - startTime) + 'ms');
                console.log('📊 Données reçues:', responseDocs.data);
                
                this.httpStatus = `${responseDocs.status} ${responseDocs.statusText}`;
                this.apiResponse = `Succès: ${responseDocs.status}`;
                
                // Vérification détaillée des données
                if (Array.isArray(responseDocs.data)) {
                    console.log('✅ Données sont un tableau');
                    console.log('📦 Nombre d éléments:', responseDocs.data.length);
                    console.log('🔍 Premier élément:', responseDocs.data[0]);
                    
                    this.documents = responseDocs.data;
                    console.log('🎉 Documents assignés:', this.documents);
                } else {
                    console.warn('⚠️ Données ne sont pas un tableau:', typeof responseDocs.data);
                    this.documents = [];
                }
                
                console.groupEnd();
                
            } catch (error) {
                console.group('❌ Erreur complète');
                console.error('Message:', error.message);
                console.error('Code:', error.code);
                console.error('URL:', error.config?.url);
                
                if (error.response) {
                    // Erreur HTTP
                    console.error('Status:', error.response.status);
                    console.error('Headers:', error.response.headers);
                    console.error('Data:', error.response.data);
                    
                    this.httpStatus = `${error.response.status} ${error.response.statusText}`;
                    this.apiResponse = `Erreur ${error.response.status}: ${JSON.stringify(error.response.data)}`;
                    
                    if (error.response.status === 404) {
                        this.error = 'Endpoint non trouvé. Vérifiez que le contrôleur est correctement configuré.';
                    } else if (error.response.status === 500) {
                        this.error = 'Erreur serveur interne. Vérifiez les logs du backend.';
                    } else {
                        this.error = `Erreur HTTP ${error.response.status}: ${error.response.statusText}`;
                    }
                } else if (error.request) {
                    // Pas de réponse
                    console.error('Aucune réponse reçue');
                    console.error('Request:', error.request);
                    
                    this.httpStatus = 'Aucune réponse';
                    this.apiResponse = 'Timeout ou erreur réseau';
                    this.error = 'Impossible de contacter le serveur. Vérifiez que le backend est démarré.';
                } else {
                    // Erreur de configuration
                    console.error('Erreur de configuration:', error.message);
                    this.httpStatus = 'Erreur configuration';
                    this.apiResponse = error.message;
                    this.error = `Erreur: ${error.message}`;
                }
                console.groupEnd();
                
                this.documents = [];
            } finally {
                this.loading = false;
                console.log('🏁 Chargement terminé');
                console.log('📋 État final:', {
                    loading: this.loading,
                    error: this.error,
                    documentsCount: this.documents.length,
                    documents: this.documents
                });
            }
        },

        async testerApiManuellement() {
            console.group('🧪 Test manuel API');
            try {
                const idEmploye = this.$route.params.id;
                const testUrl = `/employes/employe/${idEmploye}`;
                
                console.log('🔗 Test URL:', testUrl);
                console.log('🌐 URL complète:', window.location.origin + testUrl);
                
                // Test avec fetch directement
                const response = await fetch(testUrl);
                console.log('📡 Réponse fetch:', {
                    status: response.status,
                    statusText: response.statusText,
                    ok: response.ok,
                    headers: Object.fromEntries(response.headers.entries())
                });
                
                if (response.ok) {
                    const data = await response.json();
                    console.log('✅ Données fetch:', data);
                    alert(`API fonctionne! ${data.length} documents trouvés.`);
                } else {
                    console.error('❌ Erreur fetch:', response.status);
                    alert(`Erreur API: ${response.status} ${response.statusText}`);
                }
            } catch (error) {
                console.error('❌ Erreur test manuel:', error);
                alert('Erreur test: ' + error.message);
            }
            console.groupEnd();
        },

        voirDocument(document) {
            console.log('👁️ Voir document:', document);
            if (!document || !document.imagePath) {
                alert('Document non disponible');
                return;
            }
            
            let documentUrl = document.imagePath;
            if (!documentUrl.startsWith('http') && !documentUrl.startsWith('/')) {
                documentUrl = '/' + documentUrl;
            }
            
            window.open(documentUrl, '_blank');
        },

        async telechargerDocument(document) {
            console.log('⬇️ Téléchargement:', document);
            if (!document || !document.imagePath) {
                alert('Document non disponible');
                return;
            }

            try {
                let documentUrl = document.imagePath;
                if (!documentUrl.startsWith('http') && !documentUrl.startsWith('/')) {
                    documentUrl = '/' + documentUrl;
                }

                const response = await fetch(documentUrl);
                if (!response.ok) throw new Error(`HTTP: ${response.status}`);
                
                const blob = await response.blob();
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.style.display = 'none';
                a.href = url;
                a.download = document.nomFichier || 'document';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
                
            } catch (error) {
                console.error('❌ Erreur téléchargement:', error);
                alert('Erreur: ' + error.message);
            }
        },

        retourner() {
            this.$router.back();
        }
    }
}
</script>

<style scoped>
.documents-container {
    background: white;
    border-radius: 8px;
    padding: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    max-width: 1200px;
    margin: 0 auto;
}

.documents-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 2px solid #ecf0f1;
}

.documents-header h1 {
    color: #2c3e50;
    margin: 0;
    font-size: 24px;
}

.debug-section {
    background: #f8f9fa;
    border: 2px solid #3498db;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 30px;
}

.debug-section h3 {
    color: #2c3e50;
    margin-top: 0;
    margin-bottom: 15px;
}

.debug-info {
    background: white;
    padding: 15px;
    border-radius: 6px;
    border: 1px solid #e1e8ed;
    font-family: monospace;
    font-size: 12px;
    overflow-x: auto;
}

.debug-info p {
    margin: 5px 0;
    word-break: break-all;
}

.btn-test {
    background: #f39c12;
    color: white;
    border: none;
    padding: 10px 15px;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
    margin-top: 10px;
}

.btn-test:hover {
    background: #e67e22;
}

.documents-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 20px;
}

.document-card {
    background: #f8f9fa;
    border: 1px solid #e1e8ed;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    gap: 15px;
    align-items: flex-start;
    transition: all 0.3s;
}

.document-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-color: #3498db;
}

.document-icon {
    font-size: 2em;
    flex-shrink: 0;
}

.document-info {
    flex: 1;
    min-width: 0;
}

.document-info h3 {
    margin: 0 0 8px 0;
    color: #2c3e50;
    font-size: 16px;
}

.document-filename {
    margin: 0 0 5px 0;
    color: #7f8c8d;
    font-size: 14px;
    word-break: break-all;
}

.document-id {
    margin: 0;
    color: #95a5a6;
    font-size: 11px;
    font-family: monospace;
}

.document-actions {
    display: flex;
    flex-direction: column;
    gap: 8px;
    flex-shrink: 0;
}

.btn-view, .btn-download {
    padding: 8px 12px;
    border: none;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s;
    white-space: nowrap;
}

.btn-view {
    background-color: #3498db;
    color: white;
}

.btn-view:hover {
    background-color: #2980b9;
}

.btn-download {
    background-color: #2ecc71;
    color: white;
}

.btn-download:hover {
    background-color: #27ae60;
}

.no-documents {
    text-align: center;
    padding: 60px 20px;
    color: #7f8c8d;
}

.no-documents-icon {
    font-size: 64px;
    margin-bottom: 20px;
    opacity: 0.5;
}

.debug-hint {
    font-size: 12px;
    color: #e74c3c;
    margin-top: 10px;
}

.loading {
    text-align: center;
    padding: 40px;
    color: #7f8c8d;
    font-size: 16px;
}

.alert-error {
    background-color: #f8d7da;
    color: #721c24;
    padding: 15px;
    border-radius: 4px;
    border-left: 4px solid #f5c6cb;
    margin-bottom: 20px;
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
    transition: all 0.3s;
}

.btn-back:hover {
    background-color: #7f8c8d;
}

@media (max-width: 768px) {
    .documents-container {
        padding: 15px;
    }
    
    .documents-header {
        flex-direction: column;
        gap: 15px;
    }
    
    .documents-grid {
        grid-template-columns: 1fr;
    }
    
    .document-card {
        flex-direction: column;
    }
    
    .document-actions {
        flex-direction: row;
        justify-content: center;
        width: 100%;
    }
}
</style>