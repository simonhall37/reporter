@echo off

mvn clean initialize -Pinitial
mvn clean install -Pconint -Dtest.ip=192.168.99.100