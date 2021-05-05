FROM openjdk:11-jre

# install collectd
RUN apt-get update && apt-get install -y \
    collectd \
    && rm -rf /var/lib/apt/lists/*

# install cloudwatch-agent
RUN wget --no-verbose "https://s3.eu-central-1.amazonaws.com/amazoncloudwatch-agent-eu-central-1/debian/amd64/latest/amazon-cloudwatch-agent.deb" \
    && dpkg -i -E amazon-cloudwatch-agent.deb \
    && rm amazon-cloudwatch-agent.deb

# configure cloudwatch-agent
RUN mkdir -p /opt/aws/amazon-cloudwatch-agent/etc/
COPY docker-build/amazon-cloudwatch-agent.json /opt/aws/amazon-cloudwatch-agent/etc/

# add application user
ENV APPLICATION_USER app
RUN useradd -ms /bin/bash $APPLICATION_USER

# add possibility to change java command line arguments
ENV JAVA_OPTS=""

# create installation directory & assign permissions
RUN mkdir /app
RUN mkdir /app/frontend
RUN mkdir /var/run/collectd
RUN chown -R $APPLICATION_USER /app
RUN chown -R $APPLICATION_USER /var/log
RUN chown -R $APPLICATION_USER /var/run/collectd
RUN chown -R $APPLICATION_USER /var/lib/collectd

# install application
COPY docker-build/collectd.conf /app/collectd.conf
COPY backend/build/libs/backend-all.jar /app
COPY frontend/build/distributions/*.js frontend/build/distributions/*.png frontend/build/distributions/*.jpg /app/frontend/
COPY docker-build/start.sh /app

# change user to application user
USER $APPLICATION_USER

# set working directory into application directory
WORKDIR /app

# start java application
CMD ["sh", "start.sh"]
