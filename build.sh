#!/usr/bin/bash
#Script to clone all dependencies then update / create required local mvn repos for build
git clone https://github.com/Realmm/RealmCommons.git
cd RealmCommons
mvn clean install
cd ..
git clone https://github.com/Realmm/RealmLib.git
cd RealmLib
mvn clean install