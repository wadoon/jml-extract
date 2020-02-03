#!/usr/bin/env bash

TOOL=$(ls ./exe/build/libs/exe-*-all.jar | head -n 1)
echo "FOUND: $TOOL"
[[ ! -f $TOOL ]] && exit 1

# abort on error
set -e

## basics
java -jar $TOOL -h
java -jar $TOOL --help
java -jar $TOOL --version
java -jar $TOOL --show-compiler-options


## Test --in
echo "2+2" | java -jar $TOOL --in --expr
echo "int a = 2+2; int b = 2*a;" | java -jar $TOOL --in --stmt



