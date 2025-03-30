@echo off
echo Démarrage du test de déploiement à %date% %time%

cd %~dp0
echo Simulation: Récupération des dernières modifications...
rem git pull origin main

echo Simulation: Construction et démarrage avec Docker...
docker-compose build
docker-compose down --remove-orphans
docker-compose up -d

echo Test de déploiement terminé à %date% %time%