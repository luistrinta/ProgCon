#! /bin/bash

. $(dirname $0)/env.sh
export CLASSPATH
$(dirname $0)/cooperari-0.3/bin/cjavac src

