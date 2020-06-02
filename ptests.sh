#! /bin/bash
$(dirname $0)/cjavac.sh
$(dirname $0)/cjunitp.sh pc.bqueue.RunTests $*
