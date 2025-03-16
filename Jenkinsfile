pipeline {
    agent any

    environment {
        REGISTRY = "2022bcd0019" // Docker Hub or registry username
        TAG = "latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Rajeshwar0019/assignment2.git'
            }
        }
        stage('Build User Service') {
            steps {
                dir('assignment2/user-service') {
                    nodejs('nodejs') {
                        bat "npm install"
                        bat "npm test"
                    }
                    // Additional build steps can be added here if needed
                }
            }
        }
        stage('Build Order Service') {
            steps {
                dir('assignment2/order-service') {
                    bat "mvn clean package"
                }
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    // Ensure your SonarQube server is set up in Jenkins Global Configuration with the name "SonarQube"
                    withSonarQubeEnv('SonarQube') {
                        // Analyze the user-service (Node.js project)
                        dir('assignment2/user-service') {
                            bat "sonar-scanner -Dsonar.projectKey=user-service -Dsonar.sources=. -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_AUTH_TOKEN%"
                        }
                        // Analyze the order-service (Maven project)
                        dir('assignment2/order-service') {
                            bat "mvn sonar:sonar -Dsonar.projectKey=order-service -Dsonar.host.url=%SONAR_HOST_URL% -Dsonar.login=%SONAR_AUTH_TOKEN%"
                        }
                    }
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Build Docker images using the correct build contexts
                    docker.build("${REGISTRY}/assignment2/user-service:${TAG}", "assignment2/user-service")
                    docker.build("${REGISTRY}/assignment2/order-service:${TAG}", "assignment2/order-service")
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Remove any existing containers if they exist
                    bat "docker rm -f user-service || echo 'No existing user-service container'"
                    bat "docker rm -f order-service || echo 'No existing order-service container'"

                    // Run the containers with the mapped ports
                    bat "docker run -d -p 7101:7001 --name user-service ${REGISTRY}/assignment2/user-service:${TAG}"
                    bat "docker run -d -p 7102:7002 --name order-service ${REGISTRY}/assignment2/order-service:${TAG}"
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar, **/build/**/*', fingerprint: true
        }
    }
}
