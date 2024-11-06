#!/usr/bin/env bash

set -e

echo "Checking to see if there are still servers running from previous runs"

# if $1 is docker or docker-compose
if [[ "${1}" = "docker" || "${1}" = "docker-compose" ]]; then
# Check if specific Docker containers are running
containers=("liferay-sample-etc-spring-boot-docker-container-name" "liferay-sample-etc-node-docker-container-name" "liferay-workspace-testing-liferay")

for container in "${containers[@]}"; do
	echo "Checking if Docker container '${container}' is in use"
	if [[ $(docker ps -a --filter "name=${container}" --format "{{.Names}}") == "${container}" ]]; then
	  echo "Docker container '${container}' is in use."
	  echo "Please stop and remove that container before running the functional tests"
	  echo 'docker container rm -f $(docker container ls -aq)'
	  exit 1
	fi
done

fi

# check curl command to localhost and if it returns a 200
#

for host in "localhost:8080" "localhost:3001" "localhost:58081"; do
# if not equal to 000, then there is a server running
if [[ $(curl -s -o /dev/null -w "%{http_code}" http://${host}) != "000" ]]; then
	echo "There is a server running on ${host}"
	echo "Please stop the server before running the functional tests"
	echo "> sudo lsof -i -n -P | grep TCP | grep 8080"
	exit 1
fi
done

# Fail this script if the java version is not 11
java_version=$(java -version 2>&1 | awk -F[\"_] 'NR==1{print $2}')

# Check if the Java version is 11
if [[ "$java_version" != 1.8* ]] && [[ "$java_version" != 11* ]]; then
echo "Java version is greater than 11. Current version: $java_version"
exit 1
else
echo "Java version must be 8 or 11."
fi

setupProfile="${1-local}"

echo "Running functional tests with the '${setupProfile}' profile"

if [[ "${setupProfile}" = "local" ]]; then
echo "Running initBundle"

./gradlew cleanBundle initBundle
fi

./gradlew runFunctionalTests -PsetupProfile="${setupProfile}"