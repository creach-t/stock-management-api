@echo off
echo Demarrage du test de deploiement a %date% %time%

REM Nettoyage forcé des conteneurs existants
echo Suppression des anciens conteneurs...
docker rm -f stock-api >nul 2>&1

REM Suppression forcée des ressources docker-compose
echo Arret des services docker-compose existants...
docker-compose down -v --remove-orphans >nul 2>&1

echo Simulation: Recuperation des dernieres modifications...
REM git pull origin main

echo Simulation: Construction et demarrage avec Docker...
docker-compose down -v
docker-compose build --pull
docker-compose up -d

REM Vérifier le déploiement
echo Test de deploiement termine a %date% %time%
