#!/bin/sh
# Builds mini-bank.jar without Maven (needs JDK 17+).
set -e
rm -rf out && mkdir -p out
javac --release 17 -d out $(find src/main/java -name "*.java")
printf 'Main-Class: MiniBank\n' > manifest.txt
jar --create --file mini-bank.jar --manifest manifest.txt -C out .
rm manifest.txt
echo "Built mini-bank.jar"
