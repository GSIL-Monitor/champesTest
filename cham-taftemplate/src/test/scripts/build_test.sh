#!/usr/bin/env bash
ant -f /usr/local/app/taf/cham.TafTemplateServer/bin/apps/ROOT/WEB-INF/build_test.xml
taskId=$(cat taskId)
responseUrl="http://self-test.et.wsd.com/selftest/task/${taskId}"
curl -X PUT -H "Content-Type: application/json" "${responseUrl}"
