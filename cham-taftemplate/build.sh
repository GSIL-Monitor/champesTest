#!/bin/sh
mvn clean --settings ./settings.xml deploy -Djob.id=$JOB_NAME -Dhudson.version.id=$BUILD_NUMBER -Dmaven.install.skip=true
cp  target/checkstyle-result.xml bin/checkstyle-result.xml