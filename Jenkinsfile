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
                echo '-----------Build Started-------------'
                sh 'mvn clean deploy -Dmaven.test.skip=true' //-D explict to specify a parameter
                echo '-----------Build Completed-------------'
            }
        }

        stage('Unit test'){
            steps{
                echo '-----------Unit Test Started-------------'
                sh 'mvn surefire-report:report'            // To run the test cases
                echo '-----------Unit Test Completed-------------'
            }
        }
 
         stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'SonarScanner_devopsudemy'
            }
            steps {
                echo '-----------Sonar analysis Started-------------'
                withSonarQubeEnv('SonarQube_Server_devopsudemy') { // If you have configured more than one global server connection, you can specify its name
                sh "${scannerHome}/bin/sonar-scanner"
        }
                echo '-----------Sonar analysis Completed-------------'
    }
  }
}
}
 
