#!/bin/bash
if [[ "$TRAVIS_REPO_SLUG" != "itsgabbooo/VaultReloaded" ]]
then
        echo 'Travis can only publish docs for release builds.'
        return 0
fi
mvn test -DrepoToken=$COVERALLS_REPO_TOKEN
exit $?
