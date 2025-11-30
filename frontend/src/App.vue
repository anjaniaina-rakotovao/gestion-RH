<script>
export default {
  data() {
    return {
      menuOpen: false,
      sidebarCollapsed: false
    }
  },
  methods: {
    closeMenu() {
      this.menuOpen = false;
    },
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed;
    }
  }
}
</script>

<template>
  <div id="app" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
    <!-- Sidebar Navigation -->
    <nav class="sidebar">
      <div class="sidebar-header">
        <div class="nav-brand">
          <h1 v-if="!sidebarCollapsed">Gestion RH</h1>
          <h1 v-else>RH</h1>
        </div>
        <button class="sidebar-toggle" @click="toggleSidebar"
          :title="sidebarCollapsed ? 'Agrandir le menu' : 'Réduire le menu'">
          {{ sidebarCollapsed ? '→' : '←' }}
        </button>
      </div>

      <div class="nav-menu">
        <router-link to="/" class="nav-link" :class="{ active: $route.path === '/' }"
          :title="sidebarCollapsed ? 'Accueil' : ''">
          <span class="nav-icon">📋</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Accueil</span>
        </router-link>

        <router-link to="/" class="nav-link" :class="{ active: $route.path === '/recherche' }"
          :title="sidebarCollapsed ? 'Recherche' : ''">
          <span class="nav-icon">🔍</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Recherche</span>
        </router-link>

        <router-link to="/statistiques/demographiques" class="nav-link"
          :class="{ active: $route.path === '/statistiques/demographiques' }"
          :title="sidebarCollapsed ? 'Statistiques' : ''">
          <span class="nav-icon">📊</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Statistiques</span>
        </router-link>

        <router-link to="/statistiques/turnover-absenteisme" class="nav-link"
          :class="{ active: $route.path === '/statistiques/turnover-absenteisme' }"
          :title="sidebarCollapsed ? 'Turnover & Absentéisme' : ''">
          <span class="nav-icon">📈</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Turnover & Absentéisme</span>
        </router-link>

        <router-link to="/statistiques/contrat" class="nav-link"
          :class="{ active: $route.path === '/statistiques/contrat' }"
          :title="sidebarCollapsed ? 'Statistiques Contrat' : ''">
          <span class="nav-icon">📑</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Statistiques Contrat</span>
        </router-link>

        <router-link to="/alertes-notifications" class="nav-link"
          :class="{ active: $route.path === '/alertes-notifications' }" :title="sidebarCollapsed ? 'Alertes' : ''">
          <span class="nav-icon">🚨</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Alertes & Notifications</span>
        </router-link>

        <router-link to="/fiche-paie" class="nav-link" :class="{ active: $route.path === '/fiche-paie' }"
          :title="sidebarCollapsed ? 'Fiches de Paie' : ''">
          <span class="nav-icon">💰</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Fiches de Paie</span>
        </router-link>

        <router-link to="/dashboard" class="nav-link"
          :class="{ active: $route.path === '/dashboard' }"> 
          <span class="nav-icon">📅</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Planning des congés</span>
        </router-link>
          
        <router-link to="/presences" class="nav-link"
          :class="{ active: $route.path === '/presences' }">
          <span class="nav-icon">🕒</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Présences</span>
        </router-link>

        <router-link to="/conges" class="nav-link" :class="{ active: $route.path === '/conges' }">
          <span class="nav-icon">📝</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Demander un congé</span>
        </router-link>

        <router-link to="/conges/attente" class="nav-link" :class="{ active: $route.path === '/conges/attente' }">
          <span class="nav-icon">⏲️</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Liste congés en attente</span>
        </router-link>

        <router-link to="/scoringPage" class="nav-link" :class="{ active: $route.path === '/scoringPage' }">
          <span class="nav-icon">⏲️</span>
          <span class="nav-text" v-if="!sidebarCollapsed">Gestion des performances</span>
        </router-link>
      </div>

      <div class="sidebar-footer" v-if="!sidebarCollapsed">
        <p>&copy; 2025 Gestion RH</p>
      </div>
    </nav>

    <!-- Main Content Area -->
    <div class="main-container">
      <!-- Mobile Header -->
      <header class="mobile-header">
        <button class="mobile-menu-toggle" @click="menuOpen = !menuOpen">☰</button>
        <div class="mobile-brand">
          <h1>Gestion RH</h1>
        </div>
      </header>

      <!-- Mobile Menu -->
      <div class="mobile-menu" :class="{ active: menuOpen }" @click="closeMenu">
        <router-link to="/" class="mobile-nav-link" :class="{ active: $route.path === '/' }">
          📋 Accueil
        </router-link>
        <router-link to="/" class="mobile-nav-link" :class="{ active: $route.path === '/recherche' }">
          🔍 Recherche
        </router-link>
        <router-link to="/statistiques/demographiques" class="mobile-nav-link"
          :class="{ active: $route.path === '/statistiques/demographiques' }">
          📊 Statistiques
        </router-link>
        <router-link to="/statistiques/turnover-absenteisme" class="mobile-nav-link"
          :class="{ active: $route.path === '/statistiques/turnover-absenteisme' }">
          📊 Turnover & Absentéisme
        </router-link>
        <router-link to="/statistiques/contrat" class="mobile-nav-link"
          :class="{ active: $route.path === '/statistiques/contrat' }">
          📊 Statistiques Contrat
        </router-link>
        <router-link to="/alertes-notifications" class="mobile-nav-link"
          :class="{ active: $route.path === '/alertes-notifications' }">
          🚨 Alertes & Notifications
        </router-link>
        <router-link to="/fiche-paie" class="mobile-nav-link" :class="{ active: $route.path === '/fiche-paie' }">
          💰 Fiches de Paie
        </router-link>
        <router-link to="/home" class="mobile-nav-link"
          :class="{ active: $route.path === '/home' }">Accueil</router-link>
        <router-link to="/dashboard" class="mobile-nav-link"
          :class="{ active: $route.path === '/dashboard' }">Dashboard</router-link>
        <router-link to="/presences" class="mobile-nav-link"
          :class="{ active: $route.path === '/presences' }">Présences</router-link>
        <router-link to="/conges" class="mobile-nav-link" :class="{ active: $route.path === '/conges' }">Demander un
          congé</router-link>
      </div>

      <!-- Main Content -->
      <main class="main-content">
        <router-view />
      </main>

      <!-- Footer -->
      <footer class="footer">
        <p>&copy; 2025 Gestion RH. Tous droits réservés.</p>
      </footer>
    </div>
  </div>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* Sidebar Styles */
.sidebar {
  width: 280px;
  background: linear-gradient(180deg, #2c3e50 0%, #34495e 100%);
  color: white;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  position: fixed;
  height: 100vh;
  z-index: 1000;
  overflow-y: auto;
}

#app.sidebar-collapsed .sidebar {
  width: 70px;
}

.sidebar-header {
  padding: 1.5rem 1.25rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(0, 0, 0, 0.1);
}

.nav-brand h1 {
  font-size: 1.5rem;
  font-weight: 700;
  color: #fff;
  white-space: nowrap;
  overflow: hidden;
}

.sidebar-toggle {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  width: 32px;
  height: 32px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.sidebar-toggle:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* Navigation Menu */
.nav-menu {
  flex: 1;
  padding: 1rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.nav-link {
  color: #ecf0f1;
  text-decoration: none;
  padding: 1rem 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
  margin: 0 0.5rem;
  border-radius: 8px;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border-left-color: #3498db;
}

.nav-link.active {
  background: rgba(52, 152, 219, 0.2);
  color: white;
  border-left-color: #4CAF50;
  font-weight: 600;
}

.nav-icon {
  font-size: 1.25rem;
  min-width: 24px;
  text-align: center;
}

.nav-text {
  white-space: nowrap;
  overflow: hidden;
  transition: opacity 0.3s ease;
}

#app.sidebar-collapsed .nav-text {
  opacity: 0;
  width: 0;
}

.sidebar-footer {
  padding: 1rem 1.25rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
  font-size: 0.875rem;
  color: rgba(255, 255, 255, 0.7);
  background: rgba(0, 0, 0, 0.1);
}

/* Main Container */
.main-container {
  flex: 1;
  margin-left: 280px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  transition: margin-left 0.3s ease;
}

#app.sidebar-collapsed .main-container {
  margin-left: 70px;
}

/* Mobile Header */
.mobile-header {
  display: none;
  background-color: #2c3e50;
  color: white;
  padding: 1rem;
  position: sticky;
  top: 0;
  z-index: 999;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.mobile-menu-toggle {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0.25rem;
}

.mobile-brand h1 {
  font-size: 1.25rem;
  font-weight: 600;
}

/* Mobile Menu */
.mobile-menu {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(44, 62, 80, 0.95);
  z-index: 1001;
  flex-direction: column;
  padding: 2rem;
  transform: translateX(-100%);
  transition: transform 0.3s ease;
}

.mobile-menu.active {
  transform: translateX(0);
}

.mobile-nav-link {
  color: #ecf0f1;
  text-decoration: none;
  padding: 1.25rem 1rem;
  font-size: 1.1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.mobile-nav-link:hover,
.mobile-nav-link.active {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  padding-left: 1.5rem;
}

/* Main Content */
.main-content {
  flex: 1;
  padding: 2rem;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
}

/* Footer */
.footer {
  background-color: #2c3e50;
  color: white;
  text-align: center;
  padding: 1.5rem;
  margin-top: auto;
}

/* Responsive Design */
@media (max-width: 1024px) {
  .sidebar {
    width: 240px;
  }

  .main-container {
    margin-left: 240px;
  }

  #app.sidebar-collapsed .sidebar {
    width: 70px;
  }

  #app.sidebar-collapsed .main-container {
    margin-left: 70px;
  }
}

@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
  }

  .main-container {
    margin-left: 0 !important;
  }

  .mobile-header {
    display: flex;
    align-items: center;
    gap: 1rem;
  }

  .mobile-menu {
    display: flex;
  }

  #app.sidebar-collapsed .sidebar {
    transform: translateX(-100%);
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 1rem;
  }

  .mobile-menu {
    padding: 1rem;
  }

  .mobile-nav-link {
    padding: 1rem 0.75rem;
    font-size: 1rem;
  }
}

/* Scrollbar for sidebar */
.sidebar::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
}

.sidebar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.5);
}
</style>