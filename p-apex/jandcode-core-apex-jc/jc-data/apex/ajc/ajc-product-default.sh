#!/bin/sh

WD=`dirname $0`

CP=${WD}/lib:${WD}/lib/*

JVM=
JVM=${JVM} -cp %CP%
JVM=${JVM} -Djandcode.app.appdir=${WD}
JVM=${JVM} -Djandcode.consolecharset=auto
JVM=${JVM} -Dfile.encoding=UTF-8
MAIN=?

java ${JVM} ${MAIN} $*

