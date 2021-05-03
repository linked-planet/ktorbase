#!/usr/bin/env sh

# collectd: modify hostname in config to include app name and timestamp
#           to make monitoring possible
appName="ktorbase"
timestamp=$(date +"%s")
random=$(tr -cd 0-9 </dev/urandom | head -c 3)
hostname="$appName-$timestamp-$random"
echo "Hostname: $hostname"
sed -i "s/{APP_NAME}/$hostname/g" /app/collectd.conf

# collectd: modify server ip in config as it is dependent on environment
echo "Collectd server: $COLLECTD_SERVER_IP"
sed -i "s/{SERVER_IP}/$COLLECTD_SERVER_IP/g" /app/collectd.conf

# collectd: run
collectd -C /app/collectd.conf

# java: run
java $JAVA_OPTS \
    -Dcom.sun.management.jmxremote \
    -Dcom.sun.management.jmxremote.port=3333 \
    -Dcom.sun.management.jmxremote.rmi.port=3333 \
    -Dcom.sun.management.jmxremote.ssl=false \
    -Dcom.sun.management.jmxremote.authenticate=false \
    -jar backend-all.jar
