#!/bin/sh
/usr/local/app/maven/bin/mvn -U clean package -Dmaven.test.skip=true -DisSkipAssemblyTest=true -DisAppendAssemblyId=false
