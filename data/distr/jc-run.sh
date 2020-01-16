#!/bin/sh

WD=`dirname $0`
WD=`realpath ${WD}`

# in JC_JVM java parameters -Dxxx=yyy
# in JC_CLI additional cli parameters

JCLIBDIR=${WD}/lib
CP=
# CP
JVM= 
JVM="${JVM} -cp ${WD}/lib/jandcode-commons-launcher.jar"
JVM="${JVM} -Djandcode.launcher.classpath=${CP}"
JVM="${JVM} -Djandcode.launcher.main=jandcode.jc.Main"
JVM="${JVM} -Djandcode.jc.appdir=${WD}"
JVM="${JVM} -Dfile.encoding=UTF-8"

java ${JVM} ${JC_JVM} jandcode.jc.Main ${JC_CLI} $*
