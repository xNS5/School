#!/usr/bin/bash


javac Renumber.java
Sleep 2s
echo ""
jar -cvfm Renumber.jar MANIFEST.mf *.class
echo ""
echo "Compiled Renumber.jar"

sleep 2s

cp Renumber.jar /usr/local/bin/java
cp br_config /usr/local/bin/java

echo "Moved Renumber to /usr/local/bin/java"
