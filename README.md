# KtorBase
[![Build Status](https://github.com/linked-planet/ktorbase/workflows/CI%20Pipeline/badge.svg)](https://github.com/linked-planet/ktorbase/actions/workflows/ci.yml)
[![GitHub License](https://img.shields.io/badge/license-CC0%201.0%20Universal-blue.svg?style=flat)](https://creativecommons.org/publicdomain/zero/1.0/legalcode)

A template for a Kotlin fullstack web application.


## Features
- Docker - [Dockerfile](Dockerfile)
- AWS ECS Fargate Deployment via CloudFormation - [ktorbase.yml](aws/templates/ktorbase.yml)
- Bitbucket Pipelines CI/CD - [bitbucket-pipelines.yml](bitbucket-pipelines.yml)
- collectd Metrics - [collectd.conf](docker-build/collectd.conf)
- SAML Integration - [ktor-onelogin-saml][ktor-onelogin-saml]
- [Typesafe Config][tsconfig]
- Atlaskit Frontend Widgets - [Atlaskit by Atlassian][atlaskit]

You can still use this template perfectly fine if you don't want any of these - just remove the corresponding parts
after project generation.

See also official [Kotlin Fullstack Sample][kotlin-fullstack-sample].


## Usage

### Generate a new project
Use `generate.sh`:
```
# <destination-folder> <group-id> <artifact-id>
./generate.sh ~/tmp com.linkedplanet ktorbase
```

### Running
*Note: This template can be run directly, which is useful to try it or to
test changes.  
The following commands work the same regardless.*

#### via Gradle
```
export APPLICATION_SECRET=a38103acb878406bb22c32c12bdfba0b
./gradlew -t backend:run
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


## Overview
The generated project consists of:

- A `frontend` based on [React][react]
- A `backend` based on [ktor][ktor]
- A `common` module containing shared code

[Gradle][gradle] is used to manage the build for both backend and frontend,
as is standard in Kotlin projects.


## Backend
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


## Frontend
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
[Bitbucket Pipelines][bitbucket-pipelines] is used to build a
[Docker][docker] image and deploy the application on
[AWS Elastic Container Service][aws-ecs] via [AWS CloudFormation][aws-cloudformation].

- If you don't use Bitbucket Pipelines, delete [bitbucket-pipelines.yml](bitbucket-pipelines.yml)
- If you don't use AWS (or you don't want to use our template), delete
  the [aws](aws) folder
- If you don't use Docker either, delete the [docker-build](docker-build) directory
  and the [Dockerfile](Dockerfile)

Note that the [Dockerfile](Dockerfile) is installing [collectd][collectd].
Adapt accordingly if you don't want it.

We are running on JRE 11, but the application is compiled with JDK 8 due to the following
ktor issues:
- https://github.com/ktorio/ktor/issues/1137
- https://github.com/ktorio/ktor/issues/321

AWS configuration parameters are stored within the repository in JSON files per
environment (see [ktorbase-test.json](aws/templates/ktorbase-test.json)).

Note that we are also publishing the Docker image for the template itself:  
[Docker Hub - linkedplanet/ktorbase](https://hub.docker.com/repository/docker/linkedplanet/ktorbase)  

```
docker run -it \
  -p 9090:9090 \
  -e APPLICATION_SECRET=a38103acb878406bb22c32c12bdfba0b \
  linkedplanet/ktorbase:latest
```

**Important: Make sure to create your own `APPLICATION_SECRET` for deployments.
It is used to encrypt the data stored within the session cookie.**


## Integration Test
[JMeter][jmeter] is used for the testing of the rest endpoints of the backend.

### Run JMeter via Gradle

#### Headless
`./gradlew jmRun`

#### GUI
`./gradlew jmGui`

#### Environment
If nothing else is specified, the default value for `env` is `local` and the
[local.env](backend/src/test/resources/local.env) within the repository is used.

Change the environment to run tests against by setting the `env` parameter:  
`./gradlew jmRun -Denv=qa`

By convention the environment files are expected to be stored in:  
`~/.env/<project_name>/<env>.env`

### Example JMeter Test
Included is an example test that will perform a login via the session REST
endpoint.  
To prove that everything works, this test is also run by the template project
itself (see [GitHub Actions CI Workflow](https://github.com/linked-planet/ktorbase/actions/workflows/ci.yml)).

## Template License
Written in 2020-2021 by [linked-planet GmbH](https://www.linked-planet.com).

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
[ktor-onelogin-saml]: https://github.com/link-time/ktor-onelogin-saml
[bitbucket-pipelines]: https://bitbucket.org/product/features/pipelines
[docker]: https://www.docker.com/
[aws-ecs]: https://aws.amazon.com/ecs/
[aws-cloudformation]: https://aws.amazon.com/cloudformation/
[collectd]: https://collectd.org/
[jmeter]: https://jmeter.apache.org/index.html
[jmeter-plugin]: https://github.com/jmeter-gradle-plugin/jmeter-gradle-plugin
[kotlin-fullstack-sample]: https://github.com/Kotlin/kotlin-full-stack-application-demo
[jetty]: https://www.eclipse.org/jetty/
