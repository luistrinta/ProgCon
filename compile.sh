#! /bin/bash

. $(dirname $0)/env.sh
javac -cp $CLASSPATH -d classes $(find src -name '*.java')

