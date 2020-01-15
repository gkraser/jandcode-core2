#!/bin/sh

WD=`realpath $(dirname $0)`

# DEV only!

# in JC_JVM java parameters -Dxxx=yyy
# in JC_CLI additional cli parameters

sh ${WD}/data/utils/prepare.sh

CP="${WD}/_jc/classes-core"

while read p; do 
    CP=${CP}:${WD}/_jc/_lib/${p}.jar
done < "${WD}/jc-libs.txt"

JVM= 
JVM="${JVM} -cp ${WD}/_jc/classes-launcher"
JVM="${JVM} -Djandcode.launcher.classpath=${CP}"
JVM="${JVM} -Djandcode.launcher.main=jandcode.jc.Main"
JVM="${JVM} -Djandcode.jc.appdir=${WD}"
JVM="${JVM} -Dfile.encoding=UTF-8"

java ${JVM} ${JC_JVM} jandcode.commons.launcher.Launcher ${JC_CLI} $*
