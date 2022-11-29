def mvn
def grdl
def stg = ""

pipeline {
    agent any
    tools{
		gradle 'gradle'
		maven 'maven'
	}
	parameters{
		choice(name: 'Build_Tool', choices:['maven', 'gradle'], description: '')
	}
    
	stages {
        stage('Version') { 
            steps {
                script{
                    stg = "Version"
                }
                echo "${stg}"
                aumentarVersion()
            }
            
        }
        stage('Building..') {
            steps{
                script{
                    stg == 'Building'
                    if(params.Build_Tool == 'maven'){
                        mvn = load 'maven.groovy'
                        mvn.exec()
                    }
                    if(params.Build_Tool == 'gradle'){
                        grdl = load 'gradle.groovy'
	                    grdl.exec()
                    }
                    stg = "QualityGate"
                }
                echo 'QualityGate..'
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
                echo "${stg}"
            }
            
        }
        stage('uploadNexus') { 
            steps {
                
                script{
                    def tag = extraeTag()
                    stg = "uploadNexus"

                    echo 'Uploading Nexus'
                    if(params.Build_Tool == 'maven'){
				        nexusPublisher nexusInstanceId: 'nsx01', nexusRepositoryId: 'EjercicioUnificar-maven', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "/var/jenkins_home/workspace/ejemplo-gradle_maven-gradle/build/DevOpsUsach2020-${tag}.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: "${tag}"]]]
                    }
                    if(params.Build_Tool == 'gradle'){
                        nexusPublisher nexusInstanceId: 'nsx01', nexusRepositoryId: 'EjercicioUnificar-gradle', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "/var/jenkins_home/workspace/ejemplo-gradle_maven-gradle/build/DevOpsUsach2020-${tag}.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: "${tag}"]]]
                    }
                }
                echo "${stg}"
            }
            
        }
        stage ('Testing Artifact'){
            steps
                {
                    script{
                        stg = "Testing Artifact"
                    }
                    echo 'Testing Artifact'
                    sh 'curl -X GET -u admin:admin http://nexus:8081/repository/EjercicioUnificar/com.devopsusachs2020.DevOpsUsach2020.0.0.1.jar -O'
                    echo "${stg}"
                }
                
        }
           
    }
    post{
            failure{
                slackSend channel: 'C044C4RDF26', message: "${custom_msg()} [STAGE: ${stg}][RESULTADO: ERROR]", teamDomain: 'diplomadodevo-izc9001', tokenCredentialId: 'slack'
            }
            success{
                slackSend channel: 'C044C4RDF26', message: "${custom_msg()} [STAGE: ${stg}][RESULTADO: EXITO]", teamDomain: 'diplomadodevo-izc9001', tokenCredentialId: 'slack'
            }
        }  

}

def custom_msg()
{
    def AUTHOR = obtenerAutor()
    def JOB_NAME = env.JOB_NAME
    def BUILD_ID= env.BUILD_ID
    def version = extraeTag()
    def MSG= "[GRUPO-4 - ${AUTHOR}] [BRANCH: ${JOB_NAME}] [VERSION: ${version}]"
    return MSG
}

def extraeTag()
{   
    sh "git pull"
    sh "ls ${env.WORKSPACE}/.git/refs/tags/ > ${env.WORKSPACE}/trabajo/tag.txt"
    def tag = sh(script: "cat ${env.WORKSPACE}/trabajo/tag.txt", returnStdout: true).toString().trim()
    largo = tag.length()
    def resultado = tag.substring(largo-5, largo)
    return resultado
}
def tagAntiguo()
{   
    sh "git pull"
    sh "ls ${env.WORKSPACE}/.git/refs/tags/ > ${env.WORKSPACE}/trabajo/tag.txt"
    def tag = sh(script: "cat ${env.WORKSPACE}/trabajo/tag.txt", returnStdout: true).toString().trim()
    largo = tag.length()
    def resultado = tag.substring(largo-11, largo-6)
    return resultado
}
def obtenerAutor()
{   
    sh "git pull"
    def autor = sh(script: "git log -p -1 | grep Author", returnStdout: true).toString().trim()
    echo "${autor}"
    largo = autor.length()
    def resultado = autor.substring(8, largo)
    return resultado
}

def aumentarVersion()
{
    def tg = extraeTag()
    def branch = env.BRANCH_NAME
    def vActual = tagAntiguo()
    vActual = "${vActual}"
    def vNuevo = "${tg}"
    sh "${env.WORKSPACE}/trabajo/cambioTag.sh ${vActual} ${vNuevo} ${env.WORKSPACE}"

    return vNuevo
}


