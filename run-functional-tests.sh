#!/usr/bin/env bash

set -e

echo "Checking to see if there are still servers running from previous runs"

# if $1 is docker or docker-compose
if [[ "${1}" = "docker" || "${1}" = "docker-compose" ]]; then
  # Check if specific Docker containers are running
  containers=("liferay-sample-etc-spring-boot-docker-container" "liferay-sample-etc-node-docker-container" "liferay-workspace-testing-liferay")

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
    echo 'sudo lsof -i -n -P | grep TCP | grep -E "3001|8080|58081"'
    exit 1
  fi
done

setupProfile="${1-local}"

echo "Running functional tests with the '${setupProfile}' profile"

if [[ "${setupProfile}" = "local" ]]; then
  echo "Downloading trial license..."

  mkdir -p configs/common/deploy
  docker container rm -f liferay-dxp-latest
  docker create --name liferay-dxp-latest liferay/dxp:latest
  docker export liferay-dxp-latest | tar -xv --strip-components=3 -C configs/common/deploy opt/liferay/deploy

  echo "Running initBundle..."

  ./gradlew cleanInitBundle initBundle
fi

./gradlew runFunctionalTests -PsetupProfile="${setupProfile}"
