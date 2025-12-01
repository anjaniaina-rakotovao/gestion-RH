CREATE TABLE ticket (
    id_ticket VARCHAR(50) PRIMARY KEY,
    titre VARCHAR(50),
    libelle VARCHAR(50),
    duree_estimee DECIMAL(10, 2),
    id_employe VARCHAR(50), 
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe)
);

CREATE TABLE ticket_histo(
    id_ticket_histo VARCHAR(50) PRIMARY KEY,
    statut INTEGER,
    date_statut DATE,
    duree DECIMAL(10, 2),
    id_ticket VARCHAR(50),
    FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket)
);

INSERT INTO ticket (id_ticket, titre, libelle, duree_estimee, id_employe) VALUES
('TICK001', 'Correction Bug Auth', 'Le système d''authentification par token expire trop vite.', 3.00, 'EMP001'),
('TICK002', 'Feature Export CSV', 'Ajout de la fonctionnalité d''exportation des données de commande.', 12.00, 'EMP002'),
('TICK003', 'Optimisation Cache Redis', 'Mise en place d''un système de cache plus performant sur la base de données.', 7.50, 'EMP003'),
('TICK004', 'Mise à jour Dépendances Sécurité', 'Mettre à jour les dépendances critiques du projet Java.', 5.00, 'EMP004'),
('TICK005', 'Nouveau Système Logging', 'Intégration de Stackdriver pour la gestion centralisée des logs.', 18.00, 'EMP005'),
('TICK006', 'Bug Affichage Responsive', 'Correction des chevauchements de colonnes sur les tablettes.', 2.50, 'EMP006'),
('TICK007', 'Refonte Page de Connexion', 'Modernisation du design et de l''ergonomie de la page de login.', 9.50, 'EMP007'),
('TICK008', 'Configuration Monitoring Prod', 'Finaliser la configuration des alertes pour l''environnement de production.', 6.00, 'EMP008'),
('TICK009', 'Ajout Champ Téléphone Client', 'Ajouter le champ téléphone dans le formulaire d''édition des profils clients.', 1.00, 'EMP009'),
('TICK010', 'Performance API lente', 'Détecter et optimiser l''API /reports qui est trop lente (plus de 5s).', 15.00, 'EMP010'),
('TICK011', 'Documentation Technique Front', 'Rédiger la documentation du framework Frontend (Vue.js/React).', 10.00, 'EMP011'),
('TICK012', 'Erreur de Validation Email', 'Le système rejette certains emails valides (.co.uk).', 4.00, 'EMP012'),
('TICK013', 'Migration Cloud GCP', 'Planification et début de la migration de l''infrastructure vers Google Cloud Platform.', 35.00, 'EMP013'),
('TICK014', 'Support Urgent Base de Données', 'Incident critique : Base de données en lecture seule.', 0.50, 'EMP014'),
('TICK015', 'Développement Widget Météo', 'Création d''un widget affichant la météo locale sur le tableau de bord.', 6.50, 'EMP015'),
('TICK016', 'Bug Barre de Recherche', 'La barre de recherche n''affiche pas les résultats instantanément.', 3.50, 'EMP016'),
('TICK017', 'Déploiement Hotfix', 'Déploiement en urgence d''un correctif de sécurité pour la version 1.2.3.', 1.50, 'EMP017'),
('TICK018', 'Analyse Log Erreur 500', 'Analyser les logs d''une vague d''erreurs 500 sur le serveur web.', 8.00, 'EMP018'),
('TICK019', 'Intégration Système de Facturation', 'Connecter l''application à l''outil de facturation tiers.', 20.00, 'EMP019'),
('TICK020', 'Nettoyage Code Legacy', 'Refactorisation et suppression de code ancien non utilisé dans le module de panier.', 11.00, 'EMP020'),
('TICK021', 'Ajustement CSS Boutons', 'Harmonisation du style des boutons (primaire/secondaire) sur tout le site.', 2.00, 'EMP021'),
('TICK022', 'Création Clé API Partenaire', 'Générer une clé d''API pour l''intégration avec le nouveau partenaire marketing.', 1.25, 'EMP022'),
('TICK023', 'Rédaction Tests Intégration', 'Écrire de nouveaux tests d''intégration pour le workflow de commande.', 13.00, 'EMP023'),
('TICK024', 'Modification Footer', 'Changement du copyright et ajout d''un lien de politique de confidentialité.', 0.75, 'EMP024'),
('TICK025', 'Upgrade OS Server Test', 'Mettre à jour l''OS du serveur de test vers Debian 12.', 4.50, 'EMP025'),
('TICK026', 'Bug Envoi Notification', 'Les notifications push ne sont pas envoyées à tous les utilisateurs Android.', 5.50, 'EMP026'),
('TICK027', 'Ajout Gestion des Rôles', 'Développement de l''interface d''administration pour la gestion des rôles utilisateur (RBAC).', 25.00, 'EMP027'),
('TICK028', 'Rapport Hebdomadaire Bugs', 'Compilation des bugs pour la réunion d''équipe.', 4.00, 'EMP028'),
('TICK029', 'Automatisation Tests E2E', 'Mettre en place Cypress/Selenium pour les tests de bout en bout.', 17.00, 'EMP029'),
('TICK030', 'Correction Fichier Config', 'Une variable d''environnement est mal configurée sur le serveur staging.', 1.00, 'EMP030');


INSERT INTO ticket_histo (id_ticket_histo, statut, date_statut, duree, id_ticket) VALUES
-- TICKET001 (Fermé - 40) - Durée réelle: 8.50 (Est. 8.00)
('HISTO001', 10, '2023-01-10', NULL, 'TICK001'),
('HISTO002', 20, '2023-01-11', NULL, 'TICK001'),
('HISTO003', 30, '2023-01-13', 7.50, 'TICK001'),
('HISTO004', 40, '2023-01-13', 1.00, 'TICK001'),

-- TICKET002 (Fermé - 40) - Durée réelle: 13.00 (Est. 12.50)
('HISTO005', 10, '2023-02-01', NULL, 'TICK002'),
('HISTO006', 20, '2023-02-05', NULL, 'TICK002'),
('HISTO007', 30, '2023-02-14', 12.00, 'TICK002'),
('HISTO008', 40, '2023-02-15', 1.00, 'TICK002'),

-- TICKET003 (Fermé - 40) - Durée réelle: 5.75 (Est. 6.00)
('HISTO009', 10, '2023-03-01', NULL, 'TICK003'),
('HISTO010', 20, '2023-03-02', NULL, 'TICK003'),
('HISTO011', 30, '2023-03-03', 5.50, 'TICK003'),
('HISTO012', 40, '2023-03-03', 0.25, 'TICK003'),

-- TICKET004 (Fermé - 40) - Durée réelle: 1.60 (Est. 1.50)
('HISTO013', 10, '2023-04-15', NULL, 'TICK004'),
('HISTO014', 20, '2023-04-15', NULL, 'TICK004'),
('HISTO015', 30, '2023-04-15', 1.50, 'TICK004'),
('HISTO016', 40, '2023-04-16', 0.10, 'TICK004'),

-- TICKET005 (Fermé - 40) - Durée réelle: 17.00 (Est. 16.00)
('HISTO017', 10, '2023-05-20', NULL, 'TICK005'),
('HISTO018', 20, '2023-05-25', NULL, 'TICK005'),
('HISTO019', 30, '2023-06-05', 15.00, 'TICK005'),
('HISTO020', 40, '2023-06-07', 2.00, 'TICK005'),

-- TICKET006 (Terminé - 30) - Durée réelle: 4.80 (Est. 4.50)
('HISTO021', 10, '2023-07-01', NULL, 'TICK006'),
('HISTO022', 20, '2023-07-03', NULL, 'TICK006'),
('HISTO023', 30, '2023-07-05', 4.80, 'TICK006'),

-- TICKET007 (Terminé - 30) - Durée réelle: 19.50 (Est. 18.00)
('HISTO024', 10, '2023-08-10', NULL, 'TICK007'),
('HISTO025', 20, '2023-08-15', NULL, 'TICK007'),
('HISTO026', 30, '2023-09-01', 19.50, 'TICK007'),

-- TICKET008 (Terminé - 30) - Durée réelle: 21.50 (Est. 22.00)
('HISTO027', 10, '2023-09-05', NULL, 'TICK008'),
('HISTO028', 20, '2023-09-15', NULL, 'TICK008'),
('HISTO029', 30, '2023-10-05', 21.50, 'TICK008'),

-- TICKET009 (Terminé - 30) - Durée réelle: 3.25 (Est. 3.50)
('HISTO030', 10, '2023-10-10', NULL, 'TICK009'),
('HISTO031', 20, '2023-10-11', NULL, 'TICK009'),
('HISTO032', 30, '2023-10-11', 3.25, 'TICK009'),

-- TICKET010 (Terminé - 30) - Durée réelle: 7.10 (Est. 7.00)
('HISTO033', 10, '2023-11-01', NULL, 'TICK010'),
('HISTO034', 20, '2023-11-04', NULL, 'TICK010'),
('HISTO035', 30, '2023-11-08', 7.10, 'TICK010'),

-- TICKET011 (En cours - 20)
('HISTO036', 10, '2023-12-01', NULL, 'TICK011'),
('HISTO037', 20, '2023-12-05', NULL, 'TICK011'),

-- TICKET012 (En cours - 20)
('HISTO038', 10, '2023-12-15', NULL, 'TICK012'),
('HISTO039', 20, '2023-12-16', NULL, 'TICK012'),

-- TICKET013 (En cours - 20)
('HISTO040', 10, '2024-01-05', NULL, 'TICK013'),
('HISTO041', 20, '2024-01-15', NULL, 'TICK013'),

-- TICKET014 (En cours - 20)
('HISTO042', 10, '2024-01-20', NULL, 'TICK014'),
('HISTO043', 20, '2024-01-20', NULL, 'TICK014'),

-- TICKET015 (En cours - 20)
('HISTO044', 10, '2024-02-01', NULL, 'TICK015'),
('HISTO045', 20, '2024-02-08', NULL, 'TICK015'),

-- TICKET016 (Attribué - 10)
('HISTO046', 10, '2024-02-15', NULL, 'TICK016'),

-- TICKET017 (Attribué - 10)
('HISTO047', 10, '2024-02-20', NULL, 'TICK017'),

-- TICKET018 (Attribué - 10)
('HISTO048', 10, '2024-03-01', NULL, 'TICK018'),

-- TICKET019 (Attribué - 10)
('HISTO049', 10, '2024-03-05', NULL, 'TICK019'),

-- TICKET020 (Attribué - 10)
('HISTO050', 10, '2024-03-10', NULL, 'TICK020'),

-- TICKET021 (Attribué - 10)
('HISTO051', 10, '2024-03-15', NULL, 'TICK021'),

-- TICKET022 (Attribué - 10)
('HISTO052', 10, '2024-03-20', NULL, 'TICK022'),

-- TICKET023 (Attribué - 10)
('HISTO053', 10, '2024-04-01', NULL, 'TICK023'),

-- TICKET024 (Attribué - 10)
('HISTO054', 10, '2024-04-05', NULL, 'TICK024'),

-- TICKET025 (Attribué - 10)
('HISTO055', 10, '2024-04-10', NULL, 'TICK025'),

-- TICKET026 (Fermé - 40) - Durée réelle: 0.80 (Est. 0.75)
('HISTO056', 10, '2024-04-15', NULL, 'TICK026'),
('HISTO057', 20, '2024-04-16', NULL, 'TICK026'),
('HISTO058', 30, '2024-04-16', 0.70, 'TICK026'),
('HISTO059', 40, '2024-04-16', 0.10, 'TICK026'),

-- TICKET027 (En cours - 20)
('HISTO060', 10, '2024-04-20', NULL, 'TICK027'),
('HISTO061', 20, '2024-04-25', NULL, 'TICK027'),

-- TICKET028 (Terminé - 30) - Durée réelle: 3.75 (Est. 4.00)
('HISTO062', 10, '2024-05-01', NULL, 'TICK028'),
('HISTO063', 20, '2024-05-03', NULL, 'TICK028'),
('HISTO064', 30, '2024-05-06', 3.75, 'TICK028'),

-- TICKET029 (Fermé - 40) - Durée réelle: 18.50 (Est. 19.00)
('HISTO065', 10, '2024-05-10', NULL, 'TICK029'),
('HISTO066', 20, '2024-05-15', NULL, 'TICK029'),
('HISTO067', 30, '2024-05-25', 17.50, 'TICK029'),
('HISTO068', 40, '2024-05-26', 1.00, 'TICK029'),

-- TICKET030 (Fermé - 40) - Durée réelle: 6.00 (Est. 5.50)
('HISTO069', 10, '2024-06-01', NULL, 'TICK030'),
('HISTO070', 20, '2024-06-03', NULL, 'TICK030'),
('HISTO071', 30, '2024-06-05', 5.00, 'TICK030'),
('HISTO072', 40, '2024-06-06', 1.00, 'TICK030'),

-- TICKET DIVERS (Fermé - 40)
('HISTO073', 10, '2025-01-01', NULL, 'TICK017'), -- TICKET017 a été Attribué en 2024, il est maintenant Fermé
('HISTO074', 20, '2025-01-01', NULL, 'TICK017'),
('HISTO075', 30, '2025-01-01', 0.45, 'TICK017'),
('HISTO076', 40, '2025-01-01', 0.05, 'TICK017'),

-- TICKET DIVERS (Fermé - 40)
('HISTO077', 10, '2025-02-01', NULL, 'TICK018'),
('HISTO078', 20, '2025-02-02', NULL, 'TICK018'),
('HISTO079', 30, '2025-02-04', 5.50, 'TICK018'),
('HISTO080', 40, '2025-02-05', 0.50, 'TICK018'),

-- TICKET DIVERS (Terminé - 30)
('HISTO081', 10, '2025-03-01', NULL, 'TICK019'),
('HISTO082', 20, '2025-03-10', NULL, 'TICK019'),
('HISTO083', 30, '2025-03-25', 21.00, 'TICK019'),

-- TICKET DIVERS (Terminé - 30)
('HISTO084', 10, '2025-04-01', NULL, 'TICK020'),
('HISTO085', 20, '2025-04-02', NULL, 'TICK020'),
('HISTO086', 30, '2025-04-03', 1.80, 'TICK020');

-- DOMAINES DE COMPETENCE
CREATE TABLE competence_domaine (
    id_domaine_competence INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(150),
    description TEXT
);

-- NIVEAUX DE COMPETENCE (ex : 1-Bas .. 5-Expert)
CREATE TABLE competence_niveau (
    id_niveau INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50),
    libelle VARCHAR(50),
    poids INT -- pour pondération si besoin
);

-- COMPETENCES
CREATE TABLE competence (
    id_competence INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(150),
    description TEXT,
    id_domaine_competence INT,
    FOREIGN KEY (id_domaine_competence) REFERENCES competence_domaine(id_domaine_competence)
);

-- FORMATIONS (catalogue)
CREATE TABLE formation (
    id_formation INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(150),
    description TEXT,
    duree_heures INT
);

-- LIAISON FORMATION <-> COMPETENCE (quelle formation travaille quelle compétence)
CREATE TABLE formation_competence (
    id_formation INT,
    id_competence INT,
    PRIMARY KEY(id_formation, id_competence),
    FOREIGN KEY (id_formation) REFERENCES formation(id_formation),
    FOREIGN KEY (id_competence) REFERENCES competence(id_competence)
);

-- POSTE (déjà existant, on suppose id_poste)
-- ASSOCIATION poste <-> competence (compétences requises pour le poste et niveau attendu)
CREATE TABLE poste_competence (
    id_poste VARCHAR(50),
    id_competence INT,
    niveau_attendu INT, -- référence à competence_niveau.id_niveau
    PRIMARY KEY(id_poste, id_competence),
    FOREIGN KEY (id_poste) REFERENCES poste(id_poste),
    FOREIGN KEY (id_competence) REFERENCES competence(id_competence)
);

-- EMPLOYEES COMPETENCES (clé composite)
CREATE TABLE employe_competence (
    id_employe VARCHAR(50),
    id_competence INT,
    niveau_obtenu INT, -- référence competence_niveau.id_niveau
    date_attribution DATE,
    PRIMARY KEY(id_employe, id_competence),
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe),
    FOREIGN KEY (id_competence) REFERENCES competence(id_competence)
);

-- TABLE EVALUATION (pour stocker évaluations périodiques)
CREATE TABLE evaluation (
    id_evaluation INT AUTO_INCREMENT PRIMARY KEY,
    id_employe VARCHAR(50),
    periode VARCHAR(20),
    productivite DECIMAL(10,2),
    efficacite DECIMAL(10,2),
    qualite DECIMAL(10,2),
    score_global DECIMAL(10,2),
    date_evaluation DATE,
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe)
);

-- TABLE RAPPORT PERFORMANCE
CREATE TABLE rapport_performance (
    id_rapport VARCHAR(50) PRIMARY KEY,
    id_employe VARCHAR(50),
    periode VARCHAR(20),
    score_productivite DECIMAL(10,2),
    score_efficacite DECIMAL(10,2),
    score_qualite DECIMAL(10,2),
    commentaire TEXT,
    date_generation DATE,
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe)
);



INSERT INTO competence_domaine (libelle, description) VALUES
('Developpement logiciel', 'Competences liees au developpement, frameworks et langages'),
('Reseaux & Infrastructure', 'Administration systeme, reseau et securite'),
('Gestion de projet', 'Methodologies, organisation, pilotage'),
('Data & Analytics', 'Analyse de donnees, BI, statistiques'),
('Design & UI/UX', 'Conception d interfaces et experience utilisateur');

-- Niveaux de compétence
INSERT INTO competence_niveau (code, libelle, poids) VALUES
('N1', 'Debutant', 1),
('N2', 'Intermediaire', 2),
('N3', 'Confirme', 3),
('N4', 'Avance', 4),
('N5', 'Expert', 5);

-- Compétences
INSERT INTO competence (libelle, description, id_domaine_competence) VALUES
('Java', 'Developpement backend Java', 1),
('Spring Boot', 'Creation d\'API et microservices', 1),
('JavaScript', 'Developpement frontend', 1),
('Administration Linux', 'Gestion serveurs Linux', 2),
('Securite reseau', 'Pare-feu, audits de securite', 2),
('Scrum', 'Methodologie Agile', 3),
('Gestion des risques', 'Analyse et controle des risques en projet', 3),
('SQL', 'Requetage SQL et modelisation', 4),
('Power BI', 'Reporting et dashboards', 4),
('Figma', 'Prototypage UX/UI', 5),
('Design System', 'Creation et gestion de systemes de design', 5);

-- Formations
INSERT INTO formation (titre, description, duree_heures) VALUES
('Formation Java Avance', 'Approfondissement du langage Java', 40),
('Initiation a Spring Boot', 'Creer des API avec Spring Boot', 30),
('JavaScript Moderne', 'ES6+, notions avancees', 25),
('Administration Linux', 'Basics + administration systeme', 45),
('Securite reseau', 'Principes fondamentaux et outils', 40),
('Scrum Master', 'Maitriser la methodologie Agile Scrum', 20),
('SQL Intensif', 'Requetes complexes, optimisation', 35),
('Power BI Complet', 'Analyse et visualisation de donnees', 30),
('UX/UI avec Figma', 'Prototypage et design d\'interfaces', 25);

ALTER TABLE formation_competence
ADD COLUMN poids INT;

-- Lien formation ↔ compétence
INSERT INTO formation_competence (id_competence, id_formation, poids) VALUES
-- JAVA + SPRING
(1, 1, 3),
(2, 2, 3),
(1, 2, 2),
-- JS
(3, 3, 3),
-- RESEAUX
(4, 4, 3),
(5, 5, 3),
-- GESTION PROJET
(6, 6, 3),
(7, 6, 1),
-- DATA
(8, 7, 3),
(9, 8, 3),
-- DESIGN
(10, 9, 3),
(11, 9, 1);

INSERT INTO formation_competence (id_competence, id_formation, poids) VALUES
(1, 1, 3),
(2, 2, 3),
(1, 2, 2),
(3, 3, 3),
(4, 4, 3),
(5, 5, 3),
(6, 6, 3),
(7, 6, 1),
(8, 7, 3),
(9, 8, 3),
(10, 9, 3),
(11, 9, 1);

INSERT INTO employe_competence (id_employe, id_competence, niveau_obtenu, date_attribution) VALUES
('EMP001', 1, 3, '2025-01-15'),
('EMP001', 2, 2, '2025-01-20'),
('EMP002', 3, 2, '2025-02-01'),
('EMP003', 4, 4, '2025-03-05'),
('EMP004', 5, 3, '2025-03-10');

INSERT INTO poste_competence (id_poste, id_competence, niveau_attendu) VALUES
('P-DEVJR', 1, 3),
('P-DEVJR', 2, 3),
('P-DEVSR', 1, 4),
('P-DEVSR', 2, 4),
('P-RH', 6, 4);

CREATE TABLE role (
    id_role VARCHAR(50) PRIMARY KEY,
    libelle VARCHAR(50) -- ex: Employe, Manager, RH, Admin
);

CREATE TABLE utilisateur (
    id_utilisateur VARCHAR(50) PRIMARY KEY,
    id_employe VARCHAR(50),
    login VARCHAR(50) UNIQUE,
    mot_de_passe VARCHAR(255),
    id_role VARCHAR(50),
    date_creation DATETIME DEFAULT NOW(),
    actif BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe),
    FOREIGN KEY (id_role) REFERENCES role(id_role)
);

CREATE TABLE audit_log (
    id_audit VARCHAR(50) PRIMARY KEY,
    id_utilisateur VARCHAR(50),
    action VARCHAR(255),        -- Ex: Création contrat, Modification employé
    table_affectee VARCHAR(50), -- Nom de la table affectée
    id_record VARCHAR(50),      -- ID du record affecté
    date_action DATETIME DEFAULT NOW(),
    details TEXT,               -- JSON ou texte pour détails complémentaires
    FOREIGN KEY (id_utilisateur) REFERENCES utilisateur(id_utilisateur)
);


CREATE TABLE document_type (
    id_doc_type VARCHAR(50) PRIMARY KEY,
    libelle VARCHAR(100),        -- ex: Contrat, Certificat, Attestation
    obligatoire BOOLEAN DEFAULT TRUE -- si le document est obligatoire pour un employé
);

CREATE TABLE document_archive (
    id_doc_archive VARCHAR(50) PRIMARY KEY,
    id_employe VARCHAR(50),
    id_doc_type VARCHAR(50),
    chemin_fichier VARCHAR(255), -- chemin physique ou URL du fichier
    date_sauvegarde DATETIME DEFAULT NOW(), -- date de sauvegarde dans le système
    date_expiration DATE,                    -- si applicable (ex: certificat)
    version INT DEFAULT 1,                   -- version du document
    commentaire TEXT,                        -- info complémentaire
    sauvegarde_legale BOOLEAN DEFAULT TRUE,  -- indique si c'est archivé légalement
    FOREIGN KEY (id_employe) REFERENCES employe(id_employe),
    FOREIGN KEY (id_doc_type) REFERENCES document_type(id_doc_type)
);

INSERT INTO role (id_role, libelle) VALUES
('R-EMP', 'Employe'),
('R-MGR', 'Manager'),
('R-RH', 'RH'),
('R-ADM', 'Admin');

INSERT INTO utilisateur (id_utilisateur, id_employe, login, mot_de_passe, id_role, actif)
VALUES
('U-001', 'EMP001', 'alice', '1234', 'R-EMP', TRUE),
('U-002', 'EMP002', 'benoit', '1234', 'R-MGR', TRUE),
('U-003', 'EMP003', 'claire', '1234', 'R-RH', TRUE),
('U-004', 'EMP004', 'admin', 'admin', 'R-ADM', TRUE);

INSERT INTO audit_log (id_audit, id_utilisateur, action, table_affectee, id_record, date_action)
VALUES
('AUD-001', 'U-003', 'Creation d un employe', 'employe', 'EMP010', NOW()),
('AUD-002', 'U-002', 'Modification fiche employe', 'employe', 'EMP002', NOW()),
('AUD-003', 'U-004', 'Suppression contrat', 'contrat', 'CT-005', NOW()),
('AUD-004', 'U-001', 'Consultation formations', 'formation', NULL, NOW());

