#!/bin/bash

javac main.java
sleep 1s

java main show salaries sum
sleep 1s
java main delete employee "$1"
sleep 1s
java main add employee Michael Kennedy Finance 1990-09-09 M 20000
sleep 1s
#java main show employees department Finance

