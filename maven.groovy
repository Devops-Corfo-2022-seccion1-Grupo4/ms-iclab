def llamarPipeline()
{

stage('TestApp') {

 sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
}
}
stage('Compile') {
      sh './mvnw clean compile -e'
     }

     stage('Test') {
      sh './mvnw clean test -e'
    }
    stage('Jar') {
      sh './mvnw clean package -e'
    }
  stage('Run') {
    sh 'nohup bash mvnw spring-boot:run &'
  sleep 20
   }
  
   
  stage ('Sonarqube Analisis') {
    withSonarQubeEnv('Sonar'){
		sh 'mvn clean package sonar:sonar'
    }
  }
    stage('SonarQualityGate') {
    timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
    }
    }
    

	




return this

