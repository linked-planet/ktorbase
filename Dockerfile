FROM openjdk:11-jre

# install prometheus jmx exporter
ENV JMX_PROMETHEUS_VERSION="0.17.2"
ENV JMX_PROMETHEUS_PORT=9404
RUN mkdir -p /opt/jmx_exporter
RUN wget --no-verbose \
    https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/$JMX_PROMETHEUS_VERSION/jmx_prometheus_javaagent-$JMX_PROMETHEUS_VERSION.jar \
    -O /opt/jmx_exporter/jmx_prometheus_javaagent-$JMX_PROMETHEUS_VERSION.jar
COPY docker-build/jmx-exporter.yml /opt/jmx_exporter/config.yml
RUN chmod -R o+x /opt/jmx_exporter

# add application user
ENV APPLICATION_USER app
RUN useradd -ms /bin/bash $APPLICATION_USER

# add possibility to change java command line arguments
ENV JAVA_OPTS=""

# create installation directory & assign permissions
RUN mkdir /app
RUN mkdir /app/frontend
RUN chown -R $APPLICATION_USER /app
RUN chown -R $APPLICATION_USER /var/log

# install application
COPY backend/build/libs/backend-all.jar /app
COPY frontend/build/distributions/*.js frontend/build/distributions/*.png frontend/build/distributions/*.jpg /app/frontend/
COPY docker-build/start.sh /app

# change user to application user
USER $APPLICATION_USER

# set working directory into application directory
WORKDIR /app

# start java application
CMD ["sh", "start.sh"]
