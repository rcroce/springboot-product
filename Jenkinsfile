pipeline {
  agent any
	tools {
		maven 'Maven 3.5.2'
	}
	options {
		timeout(time: 10, unit: 'MINUTES') 
	}
  stages {
    stage('Build') {
      steps {
				checkout scm
				sh 'mvn clean build'
      }
    }
  }
}