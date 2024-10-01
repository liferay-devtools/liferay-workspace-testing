pipeline {
	agent {label "linux"}
	options {
		buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5')
		disableConcurrentBuilds()
	}
	stages {
		stage('Say Hello') {
			steps {
				echo "Hello"
			}
		}
		stage('Run test script') {
			steps {
				sh 'jenkins-test.sh'
			}
		}
	}
}