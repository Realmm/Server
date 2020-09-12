#!/usr/bin/bash
#Script to build all local dependencies
cd RealmCommons
mvn clean install
cd ..
cd RealmLib
mvn clean install