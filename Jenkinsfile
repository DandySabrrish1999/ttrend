pipeline {
    agent{
        node{
            label 'maven-agent'
        }
    }

environment {
    PATH="/opt/apache-maven-3.9.8/bin:$PATH"
}
    stages {
        stage('build') {
            steps {
                sh 'mvn clean deploy'
            }
        }
 
         stage('SonarQube analysis') {
            environment {
            scannerHome = tool 'SonarScanner_devopsudemy'
            }
            steps {
            withSonarQubeEnv('SonarQube_Server_devopsudemy') { // If you have configured more than one global server connection, you can specify its name
            sh "${scannerHome}/bin/sonar-scanner"
        }
    }
  }
}
}
 
