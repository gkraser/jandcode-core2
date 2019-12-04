#!/bin/sh
WD=`dirname $0`
P1=${WD}/_jc/ajc-prepare.sh
if [ ! -e ${P1} ]; then
    jc prepare
fi
. ${P1} $*
java ${JVM} ${JC_JVM} ${MAIN} $*
