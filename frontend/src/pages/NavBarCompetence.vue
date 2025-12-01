<!-- Navbar.vue -->
<template>
  <nav class="navbar">
    <div class="navbar-container">
      <ul class="nav-list">
        <li class="nav-item">
          <router-link to="/competence" class="nav-link">
            <span class="nav-icon">📊</span>
            <span class="nav-text">Générer rapport</span>
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/rechercheCompetence" class="nav-link">
            <span class="nav-icon">🔍</span>
            <span class="nav-text">Rechercher compétence</span>
          </router-link>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup>
// Pas de logique nécessaire ici
</script>

<style scoped>
.navbar {
  width: 100%;
  background: linear-gradient(135deg, #2a7ae4 0%, #1a5bb8 100%);
  padding: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item {
  position: relative;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 10px;
  color: white;
  text-decoration: none;
  font-weight: 600;
  font-size: 15px;
  padding: 18px 24px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  background: transparent;
  border: none;
  cursor: pointer;
}

.nav-link::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s ease;
}

.nav-link:hover::before {
  left: 100%;
}

.nav-link:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-1px);
}

.nav-icon {
  font-size: 18px;
  filter: drop-shadow(0 2px 2px rgba(0, 0, 0, 0.2));
  transition: transform 0.3s ease;
}

.nav-link:hover .nav-icon {
  transform: scale(1.1);
}

.nav-text {
  position: relative;
  padding-bottom: 2px;
}

.nav-link.router-link-exact-active {
  background-color: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
}

.nav-link.router-link-exact-active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 24px;
  right: 24px;
  height: 3px;
  background: linear-gradient(90deg, #4CAF50, #8BC34A);
  border-radius: 3px 3px 0 0;
  animation: slideIn 0.3s ease-out;
}

.nav-link.router-link-exact-active .nav-icon {
  transform: scale(1.1);
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

@keyframes slideIn {
  from {
    transform: scaleX(0);
    opacity: 0;
  }
  to {
    transform: scaleX(1);
    opacity: 1;
  }
}

/* Responsive design */
@media (max-width: 768px) {
  .navbar-container {
    padding: 0 16px;
  }
  
  .nav-list {
    justify-content: center;
  }
  
  .nav-link {
    padding: 16px 20px;
    font-size: 14px;
  }
  
  .nav-text {
    display: none;
  }
  
  .nav-icon {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .nav-list {
    gap: 8px;
  }
  
  .nav-link {
    padding: 14px 16px;
  }
}
</style>