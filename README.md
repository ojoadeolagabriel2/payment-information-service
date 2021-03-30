# Pokemon Information Service
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

:star: send a star our way

A light weight, containerized pokemon information service for you favourite pokemon

* fetch pokemon information
* presents their description in shakespearean text
* caches responses to improve performance
* provides easy to use startup script for running app in a java 11 based docker container

## Table of content

- [Installation](#installation)
- [Setup](#setup)
    - [Parameters](#parameters)
- [How to use utility](#usage)
- [Sample Output](#license)

## Installation

Service requires that maven 3+ be installed. It also requires a running docker instance locally on your unix machine

1. install mvn here to your unix machine: https://www.baeldung.com/install-maven-on-windows-linux-mac#installing-maven-on-mac-os-x
2. install docker to your machine here: https://docs.docker.com/docker-for-mac/install/

## Setup

Getting service ready for execution

### Parameters

This service uses the https://pokeapi.co api to fetch pokemon formation and then calls https://api.funtranslations.com to translate the pokemon description to shakespearean text.
The following configuration represent the default and need not be changed to start the service.

```yaml
app:
  game_version_name: "${GAME_VERSION:red}"
  max_cache_expiry_in_minutes: 12
  pokemon_io_host_url: "https://pokeapi.co"
  shakespeare_translator_host_url: "https://api.funtranslations.com"
```
### How to use utility

There are 2 ways to run this tool

1. navigate to the 'deployment' folder and execute the following command:

```bash
chmod +x run-locally.sh && ./run-locally.sh
```
###### note: command above will a. run automated tests b. build jar c. build and execute app image in docker (really simple)

2. confirm application's docker container is running:

```bash
docker ps -a 
```
###### note: should show image called 'truelayer/pokemon-information-service:latest' running under the name 'pokemon-information-service'

3. call the api endpoint as so:

```bash
curl --location --request GET 'http://localhost:50000/pokemon/pikachu' --header 'Content-Type: application/json'
```

## Sample Output

```json
{
  "name": "pikachu",
  "description": "Whenever pikachu cometh across something new,  't blasts 't with a jolt of electricity. If 't be true thee cometh across a blackened berry,  't?s evidence yond this pok?mon did misprision the intensity of its charge."
}
```

### Things to know

1. application calls the free endpoint https://api.funtranslations.com for text translation to shakespearean form (which is rate-limited to 5 calls per hour after which it returns status code 429), as such we cache responses from http://localhost:50000/pokemon/pikachu to limit calls to https://api.funtranslations.com.
2. to get pokemon specie information we restrict the game filter to 'ruby' by default. this can be changed in the application yaml (see 'game_version_name' config above) or set as an environment variable.
3. we return the untranslated text where too many calls to the unlicensed endpoint https://api.funtranslations.com has occurred. 

### Things to do in the future

1. get paid license to https://api.funtranslations.com and update HttpClient to pass ACCESS_TOKEN via headers for unlimited calls to this endpoint.
2. use configurable timeouts for httpclients.