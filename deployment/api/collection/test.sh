#!/usr/bin/env sh

set -ex

lsof -ti tcp:8080 | xargs kill

java -cp wiremock-velocity-transformer-standalone-2.3.jar:/var/wiremock/extensions/wiremock-velocity-transformer-standalone-2.3.jar \
    com.github.tomakehurst.wiremock.standalone.WireMockServerRunner --verbose -global-response-templating