test -d /opt/liferay/routes/default/liferay-sample-etc-node || exit 1

test -d /opt/liferay/routes/default/liferay-sample-etc-spring-boot || exit 1

curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/web/guest | grep 200 &>/dev/null