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

REM Modifier le port dans le fichier docker-compose.yml
echo Modification du port pour utiliser 9998...
powershell -Command "(Get-Content docker-compose.yml) -replace '\"8080:8080\"', '\"9998:8080\"' | Set-Content docker-compose.yml"

REM Construction et démarrage avec le nouveau port
docker-compose build --pull
docker-compose up -d

echo Test de deploiement termine a %date% %time%
echo Application accessible sur http://localhost:9998
