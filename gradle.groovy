def exec(){
    echo 'Building..'
    sh 'gradle build'
    echo 'SonarQube..'
    withSonarQubeEnv('Sonar'){
		  sh 'gradle sonarqube'
    }
  }
return this;