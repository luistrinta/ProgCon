
#! /bin/bash

CLASSPATH=''

COOPERARI_LIB_DIR="$(dirname $0)/cooperari-0.3/lib"
for file in $(ls $COOPERARI_LIB_DIR); do
  # echo $file
  CLASSPATH+="$COOPERARI_LIB_DIR/$file:"
done

LIB_DIR="$(dirname $0)/lib"

for file in $(ls $LIB_DIR); do
  # echo $file
  CLASSPATH+="$LIB_DIR/$file:"
done

CLASSPATH+="$(dirname $0)/classes"
