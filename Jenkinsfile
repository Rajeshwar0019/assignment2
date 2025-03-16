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
                    // Retrieve the SonarQube Scanner installation.
                    // Make sure that in Jenkins Global Tool Configuration you have set up a SonarQube Scanner
                    // with the name 'SonarQubeScanner'
                    def sonarHome = tool name: 'SonarQubeScanner', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    
                    // withSonarQubeEnv will inject the necessary environment variables (like SONAR_HOST_URL and SONAR_AUTH_TOKEN)
                    withSonarQubeEnv('SonarQube') {
                        // Analyze the Node.js project
                        dir('assignment2/user-service') {
                            bat "\"${sonarHome}\\bin\\sonar-scanner\" -Dsonar.projectKey=assignment2 -Dsonar.sources=. -Dsonar.login=%SONAR_AUTH_TOKEN%"
                        }
                        // Analyze the Maven project
                        dir('assignment2/order-service') {
                            bat "mvn sonar:sonar -Dsonar.projectKey=order-service -Dsonar.login=%SONAR_AUTH_TOKEN%"
                        }
                    }
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("${REGISTRY}/assignment2/user-service:${TAG}", "assignment2/user-service")
                    docker.build("${REGISTRY}/assignment2/order-service:${TAG}", "assignment2/order-service")
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    bat "docker rm -f user-service || echo 'No existing user-service container'"
                    bat "docker rm -f order-service || echo 'No existing order-service container'"
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
