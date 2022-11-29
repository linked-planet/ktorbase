# KtorBase
[![Build Status](https://github.com/linked-planet/ktorbase/actions/workflows/ktorbase.yml/badge.svg)](https://github.com/linked-planet/ktorbase/actions/workflows/ktorbase.yml)
[![KtorBase-Example](https://github.com/linked-planet/ktorbase/actions/workflows/ktorbase-example.yml/badge.svg)](https://github.com/linked-planet/ktorbase/actions/workflows/ktorbase-example.yml)
[![GitHub License](https://img.shields.io/badge/license-CC0%201.0%20Universal-blue.svg?style=flat)](https://creativecommons.org/publicdomain/zero/1.0/legalcode)

A template for a Kotlin fullstack web application.


## Features
- Docker
  - [Dockerfile](Dockerfile)
- AWS ECS Fargate Deployment via CloudFormation
  - [ktorbase.yml](aws/templates/ktorbase.yml)
- [AWS Container Insights Prometheus Metrics Monitoring][aws-prometheus]
  - [jmx-exporter.yml](docker-build/jmx-exporter.yml)
  - [start.sh](docker-build/start.sh)
- GitHub Actions CI/CD
  - [.github/workflows](.github/workflows)
- [Ktor OneLogin SAML Integration][ktor-onelogin-saml]
- [Typesafe Config][tsconfig]
  - [application.conf](backend/src/main/resources/application.conf)
- [Atlaskit Frontend Widgets][atlaskit]

You can still use this template perfectly fine if you don't want any of these - just remove the corresponding parts
after project generation.

See also official [Kotlin Fullstack Sample][kotlin-fullstack-sample].


## Usage

### Generate a new project

#### On-the-fly & interactive initialization
Download and execute the script `init.sh`:
```
bash <(curl -s https://raw.githubusercontent.com/linked-planet/ktorbase/master/init.sh)
```

The script will prompt for the required parameters.

#### Clone-based generation
Clone the repository and use the script `generate.sh`:
```
# <destination-folder> <group-id> <artifact-id>
./generate.sh ~/tmp com.linked-planet example-project
```

### Update an existing project

If you want to update an existing ktorbase-based project, simply download and run the script `update.sh`: 
```
bash <(curl -s https://raw.githubusercontent.com/linked-planet/ktorbase/master/update.sh)
```

The script will prompt for the required parameters.

### Running
*Note: This template can be run directly, which is useful to try it or to
test changes.  
The following commands work the same regardless.*

#### via Gradle
```
export APPLICATION_SECRET=0000000000000000000000000000000
./gradlew -t -Dio.ktor.development=true backend:run
```
```
./gradlew -t frontend:browserDevelopmentRun
```
**Important: Make sure to create your own `APPLICATION_SECRET` for deployments.
It is used to encrypt the data stored within the session cookie.**

To access the application:
- http://localhost:9090 (backend)
- http://localhost:8080 (frontend)

#### via IntelliJ
Two run configurations are available automatically after the project is imported:
- backend:run
- frontend:run

Make sure to delegate IntelliJ *build and run* to Gradle via the IntelliJ settings.


## Overview
The generated project consists of:

- A `frontend` based on [React][react]
- A `backend` based on [ktor][ktor]
- A `common` module containing shared code

[Gradle][gradle] is used to manage the build for both backend and frontend,
as is standard in Kotlin projects.


### Backend
The initial HTML is delivered to the client via the
[IndexRoute](backend/src/main/kotlin/com/linkedplanet/ktorbase/routes/IndexRoute.kt).
This will cause the client to load the JavaScript frontend
application, which is bundled by the Gradle build into the single
file `frontend-BUILD_VERSION.js`.

*Note: The `BUILD_VERSION` is appended to the file name to force
Browsers to download the latest JavaScript code after a redeployment,
making sure that there is no outdated JavaScript code on clients.*

From there, the client can login / logout via the endpoints declared
in [SessionRoute](backend/src/main/kotlin/com/linkedplanet/ktorbase/routes/SessionRoute.kt).

Configuration is done via [application.conf](backend/src/main/resources/application.conf)
(see [Typesafe Config][tsconfig]).

There is also a mechanism to send configuration to the frontend
application, whereby the config data is sent as part of the JSON
response of [SessionRoute](backend/src/main/kotlin/com/linkedplanet/ktorbase/routes/SessionRoute.kt)
endpoints.

Note that we are using [Jetty][jetty] as [ktor][ktor] engine, because we usually also
need [SAML Authentication][ktor-onelogin-saml], which introduces this
limitation.


### Frontend
The frontend shows a simple login form. If you provide credentials
`admin` / `admin`, the login will succeed, causing the form to
disappear, and the main page content to be displayed.

If you refresh the page after successful login, you will still be
logged in, as the generated backend is fully configured for
session cookie-based authentication.

To render widgets, the [Atlaskit][atlaskit] frontend
components library is used. Of course, you can replace these if you
want to use something else.

The build is configured to use [Sassy CSS][sass].  
(*see [Sass vs. SCSS: which syntax is better?][sassy-vs-scss]*)

There are some utility classes
that can be used to enable developer features like letting specific
routes fail, or introducing random HTTP delay. We find these
useful for testing how our applications behave in error situations.
There might be more elegant ways or tools for doing this - use them
at your own discretion, or delete them if you don't want them:
- [DevOptions](frontend/src/main/kotlin/com/linkedplanet/ktorbase/DevOptions.kt)
- [GlobalOptions](frontend/src/main/kotlin/com/linkedplanet/ktorbase/GlobalOptions.kt)
- [Async](frontend/src/main/kotlin/com/linkedplanet/ktorbase/util/Async.kt)

We don't claim that these are the best ways to handle these situations. So far,
they worked for us very reliably. If you know how to do things in a better way,
please tell us :-)


## Deployment
### Overview
[GitHub Actions][github-actions] is used to build a
[Docker][docker] image and deploy the application on
[AWS Elastic Container Service][aws-ecs] via [AWS CloudFormation][aws-cloudformation].

- If you don't use GitHub Actions, delete [.github](.github)
- If you don't use AWS (or you don't want to use our template), delete
  the [aws](aws) folder
- If you don't use Docker either, delete the [docker-build](docker-build) directory
  and the [Dockerfile](Dockerfile)

### Java Version
We are running on JRE 11, but the application is compiled with JDK 8 due to the following
ktor issues:
- https://github.com/ktorio/ktor/issues/1137
- https://github.com/ktorio/ktor/issues/321

### Configuration
AWS configuration parameters are stored within the repository in JSON files per
environment (see [ktorbase-test.json](aws/templates/ktorbase-test.json)).

These variables are passed as parameter values for the cloud formation template
via script to the AWS CLI (see [deploy.sh](pipelines/deploy.sh)).

Some of these variables directly affect the resources created by CloudFormation,
others are picked up by the application. Those that are relevant for the application
will be passed as environment variables into the Docker container, such that they will
then be picked up by [application.conf](backend/src/main/resources/application.conf).


## Try it
We are also publishing the Docker image for the template itself:  
[Docker Hub - linkedplanet/ktorbase](https://hub.docker.com/repository/docker/linkedplanet/ktorbase)  
```
docker run -it \
  -p 9090:9090 \
  -e APPLICATION_SECRET=0000000000000000000000000000000 \
  linkedplanet/ktorbase:latest
```

**Important: Make sure to create your own `APPLICATION_SECRET` for deployments.
It is used to encrypt the data stored within the session cookie.**



## Template License
Written in 2020-2022 by [linked-planet GmbH](https://www.linked-planet.com).

To the extent possible under law, the author(s) have dedicated all copyright and related
and neighboring rights to this template to the public domain worldwide.
This template is distributed without any warranty. See <http://creativecommons.org/publicdomain/zero/1.0/>.


[g8]: http://www.foundweekends.org/giter8/
[react]: https://reactjs.org/
[ktor]: https://ktor.io/
[gradle]: https://gradle.org/
[tsconfig]: https://github.com/lightbend/config/
[atlaskit]: https://atlaskit.atlassian.com/
[sass]: https://sass-lang.com/
[sassy-vs-scss]: http://thesassway.com/editorial/sass-vs-scss-which-syntax-is-better
[ktor-onelogin-saml]: https://github.com/linked-planet/ktor-onelogin-saml
[github-actions]: https://github.com/features/actions
[docker]: https://www.docker.com/
[aws-ecs]: https://aws.amazon.com/ecs/
[aws-cloudformation]: https://aws.amazon.com/cloudformation/
[collectd]: https://collectd.org/
[kotlin-fullstack-sample]: https://github.com/Kotlin/kotlin-full-stack-application-demo
[jetty]: https://www.eclipse.org/jetty/
[aws-prometheus]: https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/ContainerInsights-Prometheus.html
