#!/bin/sh
mvn clean package -DskipTests
mvn compile jib:dockerBuild
