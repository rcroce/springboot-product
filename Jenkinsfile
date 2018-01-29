pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        bat(script: 'mvn clean', encoding: 'UT-8')
      }
    }
  }
}