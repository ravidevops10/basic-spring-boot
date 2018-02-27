pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  parameters {
    string(name: 'IMAGE_REPO_NAME', defaultValue: 'jamessmith52963/basic-spring-boot', description: '')
    string(name: 'LATEST_BUILD_TAG', defaultValue: 'build-latest', description: '')
    string(name: 'DOCKER_COMPOSE_FILENAME', defaultValue: 'docker-compose.yml', description: '')
    string(name: 'DOCKER_STACK_NAME', defaultValue: 'spring_boot_stack', description: '')
    string(name: 'SONAR_TOKEN', defaultValue: '57fa1ba76f2c6ebae895284c269b7f4f1c0d2935', description: '')
    string(name: 'SONAR_URL', defaultValue: 'http://sonar:9000', description: '')
    string(name: 'MAVEN_IMAGE', defaultValue: 'maven:3.5.2-jdk-8-alpine', description: '')
    booleanParam(name: 'SKIP_MAVEN_TESTS', defaultValue: true, description: '')
    booleanParam(name: 'PUSH_DOCKER_IMAGES', defaultValue: true, description: '')
    booleanParam(name: 'DOCKER_STACK_RM', defaultValue: false, description: 'Remove previous stack.  This is required if you have updated any secrets or configs as these cannot be updated. ')
  }
  stages { 
    stage('sonar test'){
      agent {
          docker { 
		     image "${params.MAVEN_IMAGE}"
		     customWorkspace "$JENKINS_HOME/workspace/$BUILD_TAG"
		  }
      }
      steps{
	      sh "mvn sonar:sonar -Dsonar.host.url=${params.SONAR_URL} -Dsonar.login=${params.SONAR_TOKEN}"
		script{
			 def pom = readMavenPom(file: 'pom.xml')
			 def groupId = pom.groupId;
			 def artifactId = pom.artifactId;
			 def qualityGateJson = sh(returnStdout: true, script: "curl ${params.SONAR_URL}/api/project_branches/list?project=${groupId}:${artifactId}").trim()
			 def qualityGate = readJSON(text: qualityGateJson)
			
			 for(branch in qualityGate.branches){
				 if(branch.name == env.BRANCH_NAME){
					 if(branch.status.qualityGateStatus == "ERROR"){
					 	error("Quality Gate Test FAILED. go to ${params.SONAR_URL}/dashboard?id=${groupId}:${artifactId}")
					 }
					 break;
				 }
			 }
		}
      }
    }
    stage('maven build'){
      agent {
          docker { 
			  image "${params.MAVEN_IMAGE}"
		     customWorkspace "$JENKINS_HOME/workspace/$BUILD_TAG"
		  }
      }
      steps{
	 	 sh "mvn clean package -f pom.xml -Dmaven.test.skip=${params.SKIP_MAVEN_TESTS}"
		  
      }
    }
    stage('docker build'){
      environment {
        COMMIT_TAG = sh(returnStdout: true, script: 'git rev-parse HEAD').trim().take(7)
        BUILD_IMAGE_REPO_TAG = "${params.IMAGE_REPO_NAME}:${env.BUILD_TAG}"
      }
      steps{
		dir("$JENKINS_HOME/workspace/$BUILD_TAG"){	      
		  sh "pwd"
		  sh "docker build . -t $BUILD_IMAGE_REPO_TAG"
		  sh "docker tag $BUILD_IMAGE_REPO_TAG ${params.IMAGE_REPO_NAME}:$COMMIT_TAG"
		  sh "docker tag $BUILD_IMAGE_REPO_TAG ${params.IMAGE_REPO_NAME}:${readMavenPom(file: 'pom.xml').version}"
		  sh "docker tag $BUILD_IMAGE_REPO_TAG ${params.IMAGE_REPO_NAME}:${params.LATEST_BUILD_TAG}"
		  sh "docker tag $BUILD_IMAGE_REPO_TAG ${params.IMAGE_REPO_NAME}:$BRANCH_NAME-latest"
		}
      }
    }
    stage('docker push'){
      when{
        expression {
          return params.PUSH_DOCKER_IMAGES
        }
      }
      environment {
        COMMIT_TAG = sh(returnStdout: true, script: 'git rev-parse HEAD').trim().take(7)
        BUILD_IMAGE_REPO_TAG = "${params.IMAGE_REPO_NAME}:${env.BUILD_TAG}"
      }
      steps{
        sh "docker push $BUILD_IMAGE_REPO_TAG"
        sh "docker push ${params.IMAGE_REPO_NAME}:$COMMIT_TAG"
        sh "docker push ${params.IMAGE_REPO_NAME}:${readMavenPom(file: 'pom.xml').version}"
        sh "docker push ${params.IMAGE_REPO_NAME}:${params.LATEST_BUILD_TAG}"
        sh "docker push ${params.IMAGE_REPO_NAME}:$BRANCH_NAME-latest"
      }
    }
    stage('Remove Previous Stack'){
      when{
        expression {
	  return params.DOCKER_STACK_RM
        }
      }
      steps{
        sh "docker stack rm ${params.DOCKER_STACK_NAME}"
	      
		      
      }
    }
    stage('Docker Stack Deploy'){
      steps{
        sh "docker stack deploy -c ${params.DOCKER_COMPOSE_FILENAME} ${params.DOCKER_STACK_NAME}"
      }
    }
  }
  post {
    always {
      sh 'echo "This will always run"'
    }
  }
}
