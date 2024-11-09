---
theme: gaia
marp: true
class: invert
paginate: true
---

<!-- _class: lead -->

# Liferay Workspace Testing

Date: 2024-11-12

---

## Goals

- Showcase New Testing Repository
  - Integration for OSGi modules
  - Functional tests for Frontend Apps and Microservices
- Orchestration
  - local process execution
  - docker(-compose)
- CI
  - Github Actions
  - Jenkinsfile
- Get Feedback on Test Repo

---

<!-- _class: lead -->

## Github Repo

- [liferay-workspace-testing](https://github.com/liferay-devtools/liferay-workspace-testing)
- (Show screenshot of repo contents)

---

## Integration Tests - Code

- (Show code snippet)

---

## Integration Tests - Overview

- (Show diagram of the tasks and the components)

---

## Integration Tests - Demo

- (Show ascii cinema recording)

---

## Integration Tests - Results

- (Show screenshot of results)

---

## Functional Tests - Code

- (Show functional test code snippet)

---

## Functional Tests - Overview

- (Show diagram of the tasks and the components)

---

## Functional Tests - Demo

- (Show ascii cinema recording)

---

## Functional Tests - Results

- (Show screenshot of results)

---

## Github Actions - Code

- (Show code snippet of the workflow file)

---

## Github Actions - Results

- (Show gihub actions results)

---

## Jenkinsfile - Code

- (Show code snippet of the Jenkinsfile file)

---

## Jenkinsfile - Results

- (Show Jenkinsfile results)
- Mention my talk tomorrow

---

## Functional Tests Lifecycle

- Show top level buckets
  - Set Up
  - Run Tests
  - Tear Down
- Show profile script inclusion (only need one based on our use-case)

---

## Functional Tests Lifecycle - Code

- show code snippet of top level lifecycle tasks

---

## Set Up Task (Local)

- Show Set up code snippet
- preparing the server (compiling, deploying, etc)
- starting the server
- starting your processes
- show example in ascii cinema recording

---

## Run Tests Task

- Show Run Tests code snippet (package.json)
- show example in ascii cinema recording

---

## Tear Down Task (Local)

- Show Tear Down code snippet
- show example in ascii cinema recording

---

## Set Up - Docker Profile

- Show Docker profile code snippet
- show task ordering
- createDockerContainer spec
- using buildDockerImage from workspace (based on upstream version + config + apps)

---

## Set Up - Docker Compose Profile

- Show Docker Compose profile code snippet
- using buildDockerImage from workspace (based on upstream version + config + apps)

---

## Test Development workflow

- go to the profile and add what you need (add your own module and apps)
- go to functional tests folder and add playwright scripts
- show command to start everything but not shut down
- show how to invoke playwright tests via (gradle packageRunAllHeadlessTests)
- then shutdown and run all at once to see if it passes

---
