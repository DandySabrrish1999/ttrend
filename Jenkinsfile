def registry = 'https://devopsudemy.jfrog.io'
def imageName = 'devopsudemy.jfrog.io/testdocker-docker-local/ttrend'
def version   = '2.1.2'
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

        stage('Unit test') {
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

        stage("Quality Gate"){
            steps{
                script{
        timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
        def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
        if (qg.status != 'OK') {
        error "Pipeline aborted due to quality gate failure: ${qg.status}"
    }
  }
}
            }
        }

      
        stage("Jar Publish") {
            steps {
                script {
                    echo '<--------------- Jar Publish Started --------------->'
                     def server = Artifactory.newServer url:registry+"/artifactory" ,  credentialsId:"Jfrog_Jenkins_Token"
                     def properties = "buildid=${env.BUILD_ID},commitid=${GIT_COMMIT}";
                     def uploadSpec = """{
                          "files": [
                            {
                              "pattern": "jarstaging/(*)",
                              "target": "maven_projudemy-libs-release-local/{1}",
                              "flat": "false",
                              "props" : "${properties}",
                              "exclusions": [ "*.sha1", "*.md5"]
                            }
                         ]
                     }"""
                     def buildInfo = server.upload(uploadSpec)
                     buildInfo.env.collect()
                     server.publishBuildInfo(buildInfo)
                     echo '<--------------- Jar Publish Ended --------------->'  
            
            }
        }   
    }   

        stage(" Docker Build ") {
            steps {
        script {
           echo '<--------------- Docker Build Started --------------->'
           sh 'export DOCKER_BUILDKIT=1'             // Enables docker buildkit
           app = docker.build(imageName+":"+version)
           echo '<--------------- Docker Build Ends --------------->'
        }
      }
    }

            stage (" Docker Publish "){
        steps {
            script {
               echo '<--------------- Docker Publish Started --------------->'  
                docker.withRegistry(registry, 'Jfrog_Jenkins_Token'){
                    app.push()
                }    
               echo '<--------------- Docker Publish Ended --------------->'  
            }
        }
    }
    
    stage(" Deploy ") {
       steps {
         script {
            echo '<--------------- Helm Deploy Started --------------->'
            sh 'helm install ttrend-v1.0 ttrend-0.1.0.tgz'
            echo '<--------------- Helm deploy Ends --------------->'
         }
       }
     }

    
}
}
 
