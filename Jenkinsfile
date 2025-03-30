pipeline {
    agent any
    
    options {
        disableConcurrentBuilds()
        timeout(time: 10, unit: 'MINUTES')
    }
    
    stages {
        stage('Pull Latest Code') {
            steps {
                sh '''
                    cd /path/to/stock-management-api
                    git pull origin main
                '''
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh '''
                    cd /path/to/stock-management-api
                    docker-compose build
                '''
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                    cd /path/to/stock-management-api
                    docker-compose down
                    docker-compose up -d
                '''
            }
        }
        
        stage('Verify Deployment') {
            steps {
                sh '''
                    # Attendre que le conteneur soit prêt
                    sleep 15
                    
                    # Vérifier si le conteneur est en cours d'exécution
                    docker ps | grep stock-api
                    
                    # Test simple pour vérifier que l'API répond
                    curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/categories | grep 200
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
