<<<<<<< HEAD
[architecture.txt](https://github.com/user-attachments/files/20626841/architecture.txt)
# Gestion de stock et de vente


🇧🇫 ECOLE POLYTECHNIQUE DE OUAGADOUGOU


🧾 Informations générales

Nom du projet : GestionStockVente
Technologies utilisées : Java, JDBC, PostgreSQL, JUnit 5
Architecture : MVC (Model ↔ DAO ↔ Service ↔ UI)
Type de livrable : Application console + génération de rapports + tests unitaires


🧠 Objectifs pédagogiques

Ce projet a pour ambition de développer une solution complète de gestion de stocks et de ventes, en respectant les bonnes pratiques de développement logiciel :

1-Conception d’un schéma relationnel

---Modélisation d’une base de données PostgreSQL

---Définition des tables, clés primaires/étrangères et contraintes d’intégrité

2-Application Java modulaire

---Accès aux données via JDBC

---Organisation du code en couches : DAO, services, couche métier et interface utilisateur

3-Principes de la Programmation Orientée Objet

---Encapsulation, héritage et polymorphisme

---Classes métier (Produit, Stock, Commande, etc.)

4-Gestion sécurisée des transactions

---Démarrage, validation et rollback

---Garantie de la cohérence des données en cas d’erreur

5-Tests unitaires robustes

---JUnit 5 pour valider chaque méthode critique

---Scénarios de création, lecture, modification et suppression (CRUD)

6-Rapports exploitables

---Génération de factures et de rapports de ventes au format texte

---Sorties claires et formatées pour une utilisation immédiate


⚙️ Prérequis

| Logiciel       | Version recommandée     |
|----------------|--------------------------|
| Java           | 17 ou plus               |
| PostgreSQL     | 14 ou plus               |
| Maven (optionnel) | 3.x (si utilisé)      |
| IDE conseillé  | IntelliJ IDEA / NetBeans / Eclipse |


💾 Configuration JDBC

private static final String URL = "jdbc:postgresql://localhost:5432/gestion_stock";
private static final String USER = "postgres";
private static final String PASSWORD = "root";


Diagrammes de classe(Base de donnees)

![Diagramme de classe projet1](https://github.com/user-attachments/assets/c56a6e75-fd5d-452b-930e-b81498ca4eb5)



🏗️ Structure du projet

![Capture d'écran 2025-06-06 111328](https://github.com/user-attachments/assets/6bee9a69-ddb0-40ae-a60a-3576063b2772)



✅ Interfaces implémentées

Login

![Capture d'écran 2025-06-04 181034](https://github.com/user-attachments/assets/d1aab80d-2de6-4434-a6ba-c4869139ebc0)

Home/Dashbord

![Capture d'écran 2025-06-11 091253](https://github.com/user-attachments/assets/e6201b7a-9a3c-48e4-bf63-471277fb82bc)

Gestion des produits

![Capture d'écran 2025-06-04 182727](https://github.com/user-attachments/assets/18cbc61a-2503-400d-817a-4022da4d7619)

Gestion des commandes

![Capture d'écran 2025-06-11 195926](https://github.com/user-attachments/assets/5f6dcb71-036a-4700-8793-f5c3a84d9561)

Traitement des ventes

![Capture d'écran 2025-06-06 105041](https://github.com/user-attachments/assets/292ddf0e-f064-4bcc-a327-05ba8ade3669)

Génération de factures

![Capture d'écran 2025-06-11 091049](https://github.com/user-attachments/assets/5fe6fc3f-d20e-46fa-97c1-0fefcb16f2e2)

Reporting quotidien

![Capture d'écran 2025-06-11 090820](https://github.com/user-attachments/assets/a4918b27-ec49-4800-afc2-7f88ea589779)


🧪 Jeu de données d'exemple

Situés dans test/, les tests couvrent :

Création de produit

Vérification de stock

Calculs de totaux

Génération de factures

💡  Execution de l'application

cd "C:...\Gestion_vente-main\GestionStockVente"

mvn clean compile exec:java

👥 Auteurs

POUYA BORIS HECTOR – développeur Java

ZONGO DJAMEL STEEPHEN – développeur Java

=======
# Gestion_vente
>>>>>>> 4e13b46cec39e64c60119a87036582dabc0623de
