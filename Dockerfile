# cannot use alpine because collectd-java plugin is missing there :-(
FROM openjdk:11-jre

# install & configure collectd
RUN apt-get update && apt-get install -y \
    collectd \
    && rm -rf /var/lib/apt/lists/*

# add application user
ENV APPLICATION_USER app
RUN useradd -ms /bin/bash $APPLICATION_USER

# create installation directory & assign permissions
RUN mkdir /app
RUN mkdir /app/frontend
RUN mkdir /var/run/collectd
RUN chown -R $APPLICATION_USER /app
RUN chown -R $APPLICATION_USER /var/log
RUN chown -R $APPLICATION_USER /var/run/collectd
RUN chown -R $APPLICATION_USER /var/lib/collectd

# change user to application user
USER $APPLICATION_USER

# install application
COPY docker-build/collectd.conf /app/collectd.conf
COPY backend/build/libs/backend-all.jar /app
COPY frontend/build/bundle/* /app/frontend/
COPY docker-build/start.sh /app

# set working directory into application directory
WORKDIR /app

# start java application
CMD ["sh", "start.sh"]
