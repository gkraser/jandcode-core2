#!/bin/sh

WD=`dirname $0`
WD=`realpath ${WD}`

# in JC_JVM java parameters -Dxxx=yyy
# in JC_CLI additional cli parameters

JCLIBDIR=${WD}/lib
CP=
# CP
JVM= 
JVM="${JVM} -cp ${CP}"
JVM="${JVM} -Djandcode.jc.appdir=${WD}"
JVM="${JVM} -Djandcode.consolecharset=auto"
JVM="${JVM} -Dfile.encoding=UTF-8"

java ${JVM} ${JC_JVM} jandcode.jc.Main ${JC_CLI} $*
