# Assumptions

- you have a unix-y system (linux, macos, wsl, etc))
- you have curl installed and several JDKs on your system (sdkman is the way to go here if you dont)
- in your `/etc/hosts` you have a line like this that helps with hostname resonlution in the JVM

  ```
  127.0.0.1 localhost myhost
  ```

# Notes

- When you run the 'run-functional-tests.sh' script, if it detects servers on ports 8080, 3001, 58081 it will fail
- When you run the 'run-functional-tests.sh' script, if it detects previou docker containers that have not been removed you will need to remove them

```bash
docker container rm -f $(docker container ls -aq)
```

- to show test report run
- `npx playwright show-report`