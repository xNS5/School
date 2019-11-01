#!/usr/bin/bash

jar -cvfm Renumber.jar MANIFEST.mf *.class br_config/

echo "Compiled Renumber.jar"

sleep 2s

cp Renumber.jar /usr/local/bin/java

echo "Moved Renumber to /usr/local/bin/java"
