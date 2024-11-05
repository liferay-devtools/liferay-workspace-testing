#!/bin/bash

function _log() {
	echo "[$(date "+%Y.%m.%d %H:%M:%S")] $1" >> health-check.log
}

_log "Checking for license registration..."
if ! grep "License registered for DXP Development" logs/liferay.*.log
then
	_log "License not registered"
	exit 1
fi

_log "Waiting for the server to be reachable..."
if ! curl localhost:8080
then
	_log "Server not reachable"
	exit 1
fi

_log "Waiting for routes for liferay-sample-etc-node..."
if ! ls /opt/liferay/routes/default/liferay-sample-etc-node
then
	_log "Routes not ready for liferay-sample-etc-node"
	exit 1
fi

_log "Waiting for routes for liferay-sample-etc-spring-boot..."
if ! ls /opt/liferay/routes/default/liferay-sample-etc-spring-boot
then
	_log "Routes not ready for liferay-sample-etc-spring-boot"
	exit 1
fi

_log "Ready!"
exit 0