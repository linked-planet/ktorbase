# link-time ktor base
[![Build Status](https://travis-ci.com/link-time/ktorbase.svg?branch=master)](https://travis-ci.com/link-time/ktorbase)
[![GitHub License](https://img.shields.io/badge/license-CC0%201.0%20Universal-blue.svg?style=flat)](https://creativecommons.org/publicdomain/zero/1.0/legalcode)

A template for a Kotlin fullstack web application.


## Usage
To generate a new project based on this template, use `generate.sh`.

In the past we used [Giter8][g8], but we stepped away from
it as we found that the benefits don't  warrant the additional complexity
in tooling. 


## Overview
The generated project consists of:

- A `frontend` based on [React][react]
- A `backend` based on [ktor][ktor]
- A `common` module containing shared code

[Gradle][gradle] is used to manage the build for both backend and frontend,
as is standard in Kotlin projects.


## Backend
The backend contains two packages:

- `gateway`: All code for calling HTTP APIs of other services
- `routes`: HTTP API provided by the backend to the frontend

The initial HTML is delivered to the client via the `IndexRoute`.
This will cause the client to load the JavaScript frontend
application, which is bundled by the Gradle build into the single
file `frontend/frontend.bundle.js`.

From there, the client can login / logout via the endpoints declared
in `SessionRoute`.

Configuration is done via `application.conf`
(see [Typesafe Config][tsconfig]).

There is also a mechanism to send configuration to the frontend
application, whereby the config data is send as part of the JSON
response of `SessionRoute` endpoints.

Note that we are using `Jetty` as `ktor` engine, because we usually also
need [SAML Authentication][ktor-onelogin-saml].


## Frontend
The frontend shows a simple login form. If you provide credentials
`admin` / `admin`, the login will succeed, causing the form to
disappear and the main page content to be displayed.

If you refresh the page after successful login, you will still be
logged in, as the generated backend is fully configured for
session cookie-based authentication.

To render widgets, the [Atlaskit by Atlassian][atlaskit] frontend
components library is used. Of course, you can replace these if you
want to use something else.

The build is configured to use [Sassy CSS][sass].  
Also see [Sass vs. SCSS: which syntax is better?][sassy-vs-scss]

There are some utility classes `DevOptions` and `GlobalOptions`
that can be used to enable developer features like letting specific
routes fail, or introducing random HTTP delay. We find these
useful for testing how our applications behave in error situations.
There might be more elegant ways or tools for doing this - use them
at your own discretion, or delete them if you don't want them.

The `Async` utility object can be used to asynchronously send
HTTP requests, while only executing callbacks on success for the
latest request of that type.

Consider a dropdown that causes an HTTP request to be sent on selection
change. If the user changes the selection in quick succession, multiple
HTTP requests will be in flight. The responses for these requests are
not guaranteed to arrive in order. But to be consistent, the UI must
only update in accordance with the latest selection. Thus, the `complete`
function takes care of discarding success responses of obsolete requests.

We don't claim that this is the best way to handle these situations. But
so far, it works for us very reliably. If you know how to do things in a
better way, please tell us :-)


## Deployment
[Bitbucket Pipelines][bitbucket-pipelines] is used to build a
[Docker][docker] image and deploy the application on
[AWS Elastic Container Service][aws-ecs].

- If you don't use Bitbucket Pipelines, delete `bitbucket-pipelines.yml`
- If you don't use AWS (or you don't want to use our template), delete
  the `aws` folder
- If you don't use Docker either, delete the `docker-build` directory
  as well as the `Dockerfile`, too

Note that the `Dockerfile` is installing [collectd][collectd].
Adapt accordingly, if you don't want it.

We are running on OpenJDK8, as ktor is not yet ready for Java >= 9.
See these issues on GitHub:

- https://github.com/ktorio/ktor/issues/1137
- https://github.com/ktorio/ktor/issues/321


## Template license
Written in 2019 by [link-time GmbH](https://www.link-time.com).

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
[collectd]: https://collectd.org/