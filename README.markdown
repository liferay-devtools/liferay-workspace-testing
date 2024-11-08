# Requirements

- you have a unix-y system (linux, macos, wsl, etc))
- you have curl installed and several JDKs on your system (sdkman is the way to go here if you dont)
- in your `/etc/hosts` you have a line like this that helps with hostname resonlution in the JVM

  ```
  127.0.0.1 localhost myhost
  ```

# How to run

## Integration Tests

- Execute `./run-tests.sh` script

## Functional Tests

- Execute `./run-functional-tests.sh` script