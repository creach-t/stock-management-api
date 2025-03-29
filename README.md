# Stock Management API

Une API REST de gestion de stock en Java/Spring Boot, évolutive et modulaire. Cette API permet de gérer un inventaire simple (produits, catégories, quantités) et est conçue pour être extensible pour intégrer facilement des fonctionnalités futures.

## Technologies

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- Lombok
- H2 Database (base de données intégrée)
- Bean Validation
- OpenAPI/Swagger pour la documentation des API
- Docker et Docker Compose pour la conteneurisation

## Fonctionnalités

- Gestion complète des catégories (CRUD)
- Gestion complète des produits (CRUD)
- Gestion des stocks (ajout, retrait, mise à jour)
- Recherche de produits par nom ou description
- Filtrage des produits par catégorie
- Identification des produits à faible stock
- Documentation API intégrée via Swagger UI
- Tests unitaires pour les services et contrôleurs
- Conteneurisation avec Docker

## Structure du projet

```
src
├── main
│   ├── java
│   │   └── com/inventory/stockmanagementapi
│   │       ├── config
│   │       │   └── DataInitializer.java
│   │       ├── controller
│   │       │   ├── CategoryController.java
│   │       │   └── ProductController.java
│   │       ├── domain
│   │       │   ├── Category.java
│   │       │   └── Product.java
│   │       ├── dto
│   │       │   ├── CategoryDTO.java
│   │       │   ├── ProductDTO.java
│   │       │   └── StockUpdateDTO.java
│   │       ├── exception
│   │       │   ├── BusinessException.java
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── repository
│   │       │   ├── CategoryRepository.java
│   │       │   └── ProductRepository.java
│   │       ├── service
│   │       │   ├── CategoryService.java
│   │       │   └── ProductService.java
│   │       └── StockManagementApiApplication.java
│   └── resources
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-prod.properties
└── test
    ├── java
    │   └── com/inventory/stockmanagementapi
    │       ├── controller
    │       │   └── CategoryControllerTest.java
    │       └── service
    │           └── CategoryServiceTest.java
    └── resources
        └── application-test.properties
```

## Installation et exécution

### Prérequis

- Java 17 ou supérieur
- Maven 3.8+ ou Gradle 7+
- Docker et Docker Compose (pour l'utilisation containerisée)

### Compilation et exécution

#### Avec Maven

```bash
# Cloner le dépôt
git clone https://github.com/creach-t/stock-management-api.git
cd stock-management-api

# Compiler le projet
./mvnw clean package

# Exécuter l'application (mode développement par défaut)
./mvnw spring-boot:run
```

#### Avec Gradle

```bash
# Cloner le dépôt
git clone https://github.com/creach-t/stock-management-api.git
cd stock-management-api

# Compiler le projet
./gradlew clean build

# Exécuter l'application (mode développement par défaut)
./gradlew bootRun
```

#### Avec Docker

```bash
# Cloner le dépôt
git clone https://github.com/creach-t/stock-management-api.git
cd stock-management-api

# Construction de l'image Docker
docker build -t stock-management-api .

# Exécution du conteneur (mode production par défaut)
docker run -p 8080:8080 stock-management-api
```

#### Avec Docker Compose

```bash
# Cloner le dépôt
git clone https://github.com/creach-t/stock-management-api.git
cd stock-management-api

# Démarrer l'application avec Docker Compose
docker-compose up -d

# Pour arrêter l'application
docker-compose down
```

### Profils de configuration

L'application dispose de plusieurs profils pour s'adapter à différents environnements :

- `dev` (développement, par défaut) : Logging détaillé, données de test chargées automatiquement
- `prod` (production) : Logging minimal, pas de données de test, optimisations de performances

Pour lancer l'application avec un profil spécifique :

```bash
# Avec Maven
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod

# Avec Gradle
./gradlew bootRun --args='--spring.profiles.active=prod'

# Avec le JAR
java -jar target/stock-management-api-0.1.0.jar --spring.profiles.active=prod

# Avec Docker
docker run -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=prod" stock-management-api
```

## Accès à l'application

Une fois l'application lancée, vous pouvez y accéder via les URLs suivantes :

- API REST : http://localhost:8080/api/
- Documentation Swagger UI : http://localhost:8080/swagger-ui.html
- Console H2 (en mode dev) : http://localhost:8080/h2-console
  - JDBC URL : `jdbc:h2:mem:stockdb`
  - Utilisateur : `sa`
  - Mot de passe : `password`

## Déploiement en production

Le projet est configuré pour être déployé en production avec Docker. Le fichier `docker-compose.yml` inclut:
- Le service API Spring Boot
- Une configuration optionnelle pour utiliser PostgreSQL au lieu de H2
- Une configuration optionnelle pour Adminer (administration de la base de données)

Pour passer de la base de données H2 intégrée à PostgreSQL :
1. Décommentez les sections correspondantes dans le fichier `docker-compose.yml`
2. Décommentez les variables d'environnement pour la connexion à la base de données
3. Ajoutez la dépendance PostgreSQL dans le `pom.xml`
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

## Endpoints API

### Catégories

| Méthode | URL                     | Description                       |
|---------|-------------------------|-----------------------------------|
| GET     | /api/categories         | Liste toutes les catégories       |
| GET     | /api/categories/{id}    | Récupère une catégorie par son ID |
| POST    | /api/categories         | Crée une nouvelle catégorie       |
| PUT     | /api/categories/{id}    | Met à jour une catégorie          |
| DELETE  | /api/categories/{id}    | Supprime une catégorie            |

### Produits

| Méthode | URL                             | Description                                  |
|---------|----------------------------------|----------------------------------------------|
| GET     | /api/products                   | Liste tous les produits (paginée)            |
| GET     | /api/products/all               | Liste tous les produits (sans pagination)    |
| GET     | /api/products/{id}              | Récupère un produit par son ID               |
| GET     | /api/products/category/{id}     | Liste les produits d'une catégorie           |
| GET     | /api/products/search?term=xyz   | Recherche des produits                       |
| GET     | /api/products/low-stock         | Liste les produits à faible stock            |
| POST    | /api/products                   | Crée un nouveau produit                      |
| PUT     | /api/products/{id}              | Met à jour un produit                        |
| DELETE  | /api/products/{id}              | Supprime un produit                          |
| PATCH   | /api/products/stock             | Met à jour le stock d'un produit             |

## Exemples d'utilisation

### Créer une catégorie

```bash
curl -X POST "http://localhost:8080/api/categories" \
  -H "Content-Type: application/json" \
  -d '{"name":"Électronique","description":"Produits électroniques"}'
```

### Créer un produit

```bash
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XY",
    "description": "Dernier modèle de smartphone",
    "price": 799.99,
    "quantity": 50,
    "sku": "ELEC-SP-001",
    "categoryId": 1
  }'
```

### Mettre à jour le stock d'un produit

```bash
curl -X PATCH "http://localhost:8080/api/products/stock" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantityChange": 10,
    "operationType": "ADD"
  }'
```

## Données de test

En mode développement (`dev`), l'application charge automatiquement des données de test avec :

- 4 catégories : Electronics, Clothing, Food & Beverages, Office Supplies
- 10 produits répartis dans ces catégories

## Évolution et extensions

Ce projet est conçu pour être facilement étendu avec des fonctionnalités supplémentaires comme :

- Authentification et autorisation (JWT, OAuth2)
- Gestion des utilisateurs et des rôles
- Historique et traçabilité des mouvements de stock
- Gestion des fournisseurs et des commandes
- Rapports et analytics
- Interface utilisateur (client web/mobile)

## Contribution

Les contributions sont bienvenues ! N'hésitez pas à soumettre des pull requests pour améliorer ce projet.

## Licence

Ce projet est sous licence MIT.
