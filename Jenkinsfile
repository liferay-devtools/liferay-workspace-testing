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

		stage("Unit Tests") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: "./gradlew clean test --console=plain --no-daemon")
						}
						else {
							bat(script: "./gradlew clean test --console=plain --no-daemon")
						}

						junit "**/build/test-results/test/*.xml"
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

		stage("Integration Tests") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: "./gradlew clean testIntegration --console=plain --no-daemon")
						}
						else {
							bat(script: "./gradlew clean testIntegration --console=plain --no-daemon")
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

		stage("Playwright Tests") {
			steps {
				script {
					try {
						if (isUnix()) {
							sh(script: "./gradlew clean :playwright:packageRunTestAll --console=plain --no-daemon")
						}
						else {
							bat(script: "./gradlew clean :playwright:packageRunTestAll --console=plain --no-daemon")
						}
					}
					catch (Exception exception) {
						echo "Exception: " + exception.toString()
					}
					finally {
						echo "Ran Playwright Tests"
					}
				}
			}
		}
	}
}