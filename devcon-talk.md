# Devcon Talk - Liferay Workspace Testing

## Key takeaways

- you should be doing automated functional testing of your liferay extensions (osgi modules, themes, client extensions)
- it is possible to have automated functional testing for your liferay extensions with a little bit of effort
- is possible to use liferay workspace to orchestrate the infrasture needed to run your functional test (e.g. deploying dependencies, starting server, etc)
- it is possible to script all of these in a CI/CD pipeline
- 

## Introduction

- unit testing (java code, library code), 
-- this is covered by normal java junit testing support by gradle OOTB
- integration testing
-- this involves the liferay osgi runtime (aka tomcat server) co-deployed with your modules 
-- this requires special testing instrumentation of the liferay server
-- liferay workspace integrates all necessary plugins to handle this
- functional testing
-- this is user-acceptance, or functional end-to-end testing
-- now this testing may go beyond the scope of a single server + your modules
-- it may invole multiple external services or external processed

## Problem statement

- i want to know how to perform an end-to-end functional test of my liferay extensions that include Client Extensions
- i want to be able to run these tests locally as a developer during development, even if I have to set these up manually
- i want to be able to run these tests in a CI/CD pipeline, e.g. in a Jenkins pipeline that guards pull requests 
- i want to be able to run these tests as part of a release or user-accepance testing process (before anything goes to production)

## Testing is a Process
[show the evolution ape -> human photo]

- first its all manual test, run this, run this, deploy this, click this, click this, look at that, type this, click that
- manual setup, and then automate the "clicking and typing" part, e.g.playwright
- then automate the setup/teardown around the execution of the functional tests
- then integrate that into a CI/CD pipeline and collect necessasry metrics and reports

## Describe the application components
- two etc client extensions (springboot and nodejs)
- custom element 1 (single frontend CX)
- custom element 2 (1 frontend CX talking to two backend CXs (springboot and nodejs))
- names
-- liferay-sample-custom-element-1
-- liferay-sample-custom-element-2
-- liferay-sample-etc-spring-boot
-- liferay-sample-etc-node

- What do these microservices represent?
-- whatever they are, they can be thought of as "external services" something that your liferay application (frontend) depends on
-- they have to be there and be in a "connected" state, for your functional tests to run

## Walkthrough a Working Example

- manual test procedure
1. start liferay
2. deploy your customizations
3. confirm that liferay has seen your CX deployment
4. for each microservice do
5. start it
6. confirm that it is running and can talk to Liferay
7. run the functional test
8. stop the microservices
9. undeploy or stop liferay

- automating the setup/teardown + functional tests
1. automate the setup with gradle
2. gradle task to run your tests (we are gonna use playwright)
3. Btw if you have a package.json, all the scripts because a gradle task that you can execute or orchestrate
4. teardown with gradle

- Some interesting code
1. server start like this (point to code) (show video demo)
2. dependencies (and extensions) deployed here (point to code) (show video demo)
3. checking for the Routes folder (things from DXP that are needed by your CX) (point to code) (show video demo)
4. orchestrating the starting of CX (point to code)
5. waiting for the CX to be ready (point to code)
6. then show where we execute functional tests after all of that is ready (point to code)
7. teardown, show the gradle finalizers or stopping liferay

