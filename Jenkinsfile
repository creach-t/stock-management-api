pipeline {
    agent any
    
    options {
        disableConcurrentBuilds()
        timeout(time: 10, unit: 'MINUTES')
    }
    
    stages {
        stage('Pull Latest Code') {
            steps {
                bat '''
                    cd C:\\chemin\\vers\\stock-management-api
                    git pull origin main
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                bat '''
                    cd C:\\chemin\\vers\\stock-management-api
                    docker-compose build
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                bat '''
                    cd C:\\chemin\\vers\\stock-management-api
                    docker-compose down
                    docker-compose up -d
                '''
            }
        }
        
        stage('Verify Deployment') {
            steps {
                bat '''
                    @REM Attendre que le conteneur soit prêt
                    timeout /t 15
                    
                    @REM Vérifier si le conteneur est en cours d'exécution
                    docker ps | findstr stock-api
                    
                    @REM Test simple pour vérifier que l'API répond
                    curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/categories | findstr 200
                '''
            }
        }
    }
    
    post {
        success {
            echo 'Déploiement réussi!'
        }
        failure {
            echo 'Le déploiement a échoué!'
        }
    }
}
