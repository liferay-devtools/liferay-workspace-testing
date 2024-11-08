#!/bin/bash

function _log() {
	message="[$(date "+%Y.%m.%d %H:%M:%S")] $1"

	echo "${message}" >> health-check.log
	echo "${message}"
}

_log "Checking for license file..."
if ! stat data/license/*.li
then
	_log "License file not found"
	exit 1
fi

_log "Waiting for the server to be reachable..."
if ! curl localhost:8080
then
	_log "Server not reachable"
	exit 1
fi

route_folders=(
	liferay-sample-etc-node
	liferay-sample-etc-spring-boot
)

for route_folder in "${route_folders[@]}"
do
	_log "Waiting for routes for ${route_folder}..."
	if ! ls /opt/liferay/routes/default/${route_folder}
	then
		_log "Routes not ready for ${route_folder}"
		exit 1
	fi
done

_log "Ready!"
exit 0