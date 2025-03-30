#!/bin/bash

echo "Démarrage du déploiement à $(date)"

# Aller dans le répertoire du projet
cd "$(dirname "$0")"

echo "Récupération des dernières modifications..."
git pull origin main

echo "Construction de l'image Docker..."
docker-compose build

echo "Arrêt des conteneurs existants..."
docker-compose down

echo "Démarrage des nouveaux conteneurs..."
docker-compose up -d

echo "Vérification du déploiement..."
sleep 15
if docker ps | grep stock-api > /dev/null; then
    echo "Conteneur en cours d'exécution."
else
    echo "ERREUR: Le conteneur n'est pas en cours d'exécution!"
    exit 1
fi

HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/categories)
if [ "$HTTP_STATUS" -eq 200 ]; then
    echo "API accessible et fonctionnelle!"
else
    echo "ERREUR: L'API n'est pas accessible (code HTTP: $HTTP_STATUS)!"
    exit 1
fi

echo "Déploiement terminé avec succès à $(date)"
