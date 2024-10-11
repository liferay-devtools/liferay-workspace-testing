setupProfile="${1-local}"

echo "Running functional tests with the '${setupProfile}' profile"

if [[ "${setupProfile}" = "local" ]]
then
  echo "Running initBundle"

  ./gradlew initBundle
fi

./gradlew runFunctionalTests -PsetupProfile="${setupProfile}" --info