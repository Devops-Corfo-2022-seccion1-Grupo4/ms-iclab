def exec(){
  
    echo 'Building..'
    sh "mvn clean install"
    echo 'SonarQube..'
    withSonarQubeEnv('Sonar'){
		  sh 'mvn clean package sonar:sonar'
    }
}

return this;
