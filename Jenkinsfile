pipeline {
  agent any
	tools {
		maven 'Maven 3.5.2'
	}
	options {
		buildDiscarder(logRotator(numToKeepStr:'10'))
		timeout(time: 5, unit: 'MINUTES')
		skipDefaultCheckout(true)
	}
  stages {
    stage('Build') {
      steps {
				checkout scm
				sh 'mvn clean install'
      }
    }
    stage('Test') {
      steps {
				sh 'mvn --version'
      }
    }
    stage('Deploy') {
      steps {
				sh 'mvn --version'
      }
    }
  }
}