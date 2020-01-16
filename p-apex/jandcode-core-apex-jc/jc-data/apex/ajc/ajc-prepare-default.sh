#!/bin/sh

CP=?
JVM=
JVM="${JVM} -cp ${CP}"
JVM="${JVM} -Djandcode.app.appdir=${WD}"
JVM="${JVM} -Dfile.encoding=UTF-8"
MAIN=?
