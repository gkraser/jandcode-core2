#!/bin/sh

CP=?
JVM=
JVM=${JVM} -cp ${CP}
JVM=${JVM} -Djandcode.app.appdir=${WD}
JVM=${JVM} -Djandcode.consolecharset=auto
JVM=${JVM} -Dfile.encoding=UTF-8
MAIN=?
