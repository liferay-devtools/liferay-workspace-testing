pipeline {
	agent any
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
				sh './jenkins-test.sh'
			}
		}
		stage('Run integration tests') {
			steps {
				echo "./run-tests.sh"
			}
			when {
				branch 'PR-*'
			}
		}
	}
}