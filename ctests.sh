#! /bin/bash
$(dirname $0)/cjavac.sh
$(dirname $0)/cjunit.sh pc.bqueue.RunTests $*
