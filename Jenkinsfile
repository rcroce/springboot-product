pipeline {
  agent any
	tools {
		maven 'Maven 3.5.2'
	}
  stages {
    stage('Version') {
      steps {
				checkout scm
				sh 'mvn --version'
      }
    }
  }
}