pipeline {
  agent any
	tools {
		maven 'Maven 3.5.2'
	}
	options {
		buildDiscarder(logRotator(numToKeepStr:'10'))
		timeout(time: 30, unit: 'MINUTES')
		skipDefaultCheckout(true)
	}
  stages {
    stage('Build') {
      steps {
				checkout scm
				sh 'mvn clean package'
      }
    }
    stage('Test') {
      steps {
				sh 'mvn verify'
      }
    }
    stage('Deploy') {
      steps {
				sh 'mvn --version'
      }
    }
  }
	post {
		always {
			deleteDir()
		}
    
		success {
			mail(to: "rodrigo.croce@gmail.com", 
				 subject: "SUCCESS: ${currentBuild.fullDisplayName}",
				 body: "Build passed! ${env.BUILD_URL}")
		}

		failure {
			mail(to: "rodrigo.croce@gmail.com", 
				 subject: "FAILURE: ${currentBuild.fullDisplayName}",
				 body: "Build failed! ${env.BUILD_URL}")
		}
	}
}