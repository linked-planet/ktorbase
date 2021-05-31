#!/usr/bin/env sh

# java: run
# shellcheck disable=SC2086
java $JAVA_OPTS \
    -javaagent:/opt/jmx_exporter/jmx_prometheus_javaagent-$JMX_PROMETHEUS_VERSION.jar=$JMX_PROMETHEUS_PORT:/opt/jmx_exporter/config.yml \
    -jar backend-all.jar
