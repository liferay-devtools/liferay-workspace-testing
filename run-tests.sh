#!/usr/bin/env bash

set -e

for host in "localhost:8080"; do
	# if not equal to 000, then there is a server running
	if [[ $(curl -s -o /dev/null -w "%{http_code}" http://${host}) != "000" ]]; then
		echo "There is a server running on ${host}"
		echo "Please stop the server before running the functional tests"
		echo 'sudo lsof -i -n -P | grep TCP | grep "8080"'
		exit 1
	fi
done

./gradlew cleanInitBundle initBundle

./gradlew clean build
