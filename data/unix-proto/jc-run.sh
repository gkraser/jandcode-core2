#!/bin/sh

WD=`dirname $0`

# DEV only!

# in JC_JVM java parameters -Dxxx=yyy
# in JC_CLI additional cli parameters

sh ${WD}/data/utils/prepare.sh

CP="${WD}/_jc/classes-core:${WD}_jc/_lib:${WD}/_jc/_lib/*"

JVM= 
JVM="${JVM} -cp ${CP}"
JVM="${JVM} -Djandcode.jc.appdir=${WD}"
JVM="${JVM} -Djandcode.consolecharset=auto"
JVM="${JVM} -Dfile.encoding=UTF-8"

java ${JVM} ${JC_JVM} jandcode.jc.Main ${JC_CLI} $*
