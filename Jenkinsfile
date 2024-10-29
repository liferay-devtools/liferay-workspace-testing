pipeline {
	agent any

	options {
		disableConcurrentBuilds()
	}

	stages {
		stage("Initialize Bundle") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: "./gradlew clean :initBundle --console=plain --no-daemon")
						}
						else {
							bat(script: "./gradlew clean :initBundle --console=plain --no-daemon")
						}
					}
					catch (Exception exception) {
						echo "Exception: " + exception.toString()
					}
					finally {
						echo "Ran Initialize Bundle"
					}
				}
			}
		}

		stage("Tests") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: "./gradlew clean build --console=plain --info --no-daemon")
						}
						else {
							bat(script: "./gradlew clean build --console=plain --info --no-daemon")
						}
					}
					catch (Exception exception) {
						echo "Exception: " + exception.toString()
					}
					finally {
						echo "Ran Unit Tests"
					}
				}
			}
		}

		stage("Functional Tests") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: './gradlew clean runFunctionalTests -PsetupProfile="local" --console=plain --info --no-daemon')
						}
						else {
							bat(script: './gradlew clean runFunctionalTests -PsetupProfile="local" --console=plain --info --no-daemon')
						}
					}
					catch (Exception exception) {
						echo "Exception: " + exception.toString()
					}
					finally {
						echo "Ran Integration Tests"
					}
				}
			}
		}
	}
}