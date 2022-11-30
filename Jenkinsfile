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
                aumentarVersion()
            }
            
        }
        stage('Building') {
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
            }
            
        }
        stage('uploadNexus') { 
            steps {
                
                script{
                    def tag = extraeTag()
                    stg = "uploadNexus"

                    echo 'Uploading Nexus'
                    echo "${env.WORKSPACE}/build/DevOpsUsach2020-${tag}.jar"
                    if(params.Build_Tool == 'maven'){
			nexusPublisher nexusInstanceId: 'nsx01', nexusRepositoryId: 'Lab4-msiclab', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/DevOpsUsach2020-${tag}.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: "${tag}"]]]
                    }
                    if(params.Build_Tool == 'gradle'){
                        nexusPublisher nexusInstanceId: 'nsx01', nexusRepositoryId: 'Lab4-msiclab', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: "${env.WORKSPACE}/build/DevOpsUsach2020-${tag}.jar"]], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: "${tag}"]]]
                    }
                }
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
    def branch = env.BRANCH_NAME
    def AUTHOR = obtenerAutor()
    def JOB_NAME = env.JOB_NAME
    def BUILD_ID= env.BUILD_ID
    def version = extraeTag()
    def MSG= "[GRUPO-4 - ${AUTHOR}] [BRANCH: ${branch}] [VERSION: ${version}]"
    return MSG
}

def extraeTag()
{   
    sh "git pull"
    sh "ls ${env.WORKSPACE}/.git/refs/tags/ > ${env.WORKSPACE}/trabajo/tag.txt"
    def tag = sh(script: "cat ${env.WORKSPACE}/trabajo/tag.txt", returnStdout: true).toString().trim()
	echo tag
    largo = tag.length()

    def resultado = tag.substring(largo-5, largo)
    return resultado
}
def tagAntiguo()
{   
    def resultado
    sh "git pull"
    sh "ls ${env.WORKSPACE}/.git/refs/tags/ > ${env.WORKSPACE}/trabajo/tag.txt"
    def tag = sh(script: "cat ${env.WORKSPACE}/trabajo/tag.txt", returnStdout: true).toString().trim()
    largo = tag.length()
	echo tag
    script{
        if(largo >= 6){
            resultado = tag.substring(largo-11, largo-6)
        }
        if(largo == 5){
            resultado = tag.substring(largo-5, largo)
        }

    }
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
    def vActual = tagAntiguo()
    vActual = "${vActual}"
    def vNuevo = "${tg}"
    sh "${env.WORKSPACE}/trabajo/cambioTag.sh ${vActual} ${vNuevo} ${env.WORKSPACE}"
    return vNuevo
}

