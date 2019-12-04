#!/bin/bash

WD=`realpath $(dirname $0)`
JC_ENV_FILE="jc-env.sh"
JC_RUN=
JC_ENV_FILE_LIST=

findup_result=""

findup() {
    local curdir=`realpath .`
    while true
    do
        for p1 in $@; do
            local f1="${curdir}/${p1}"
            if [ -e $f1 ]; then
                findup_result="${f1} ${findup_result}"
            fi
        done
        curdir=`realpath ${curdir}/..`
        if [ $curdir = "/" ]; then
            break
        fi
    done
}

# find jc-env.sh suite
findup _$JC_ENV_FILE $JC_ENV_FILE _jc/$JC_ENV_FILE
f1="${WD}/${JC_ENV_FILE}"
if [ -e $f1 ]; then
    findup_result="${f1} ${findup_result}"
fi
JC_ENV_FILE_LIST="${findup_result}"

# call all jc-env.sh
for p1 in $JC_ENV_FILE_LIST ; do
    # set dir path forscript
    WD=`dirname $p1`
    # call jc-env script
    . $p1
done

# run
if [ "x${1}" = "x@" ]; then
    shift
    exec $*
else
    if [ -z $JC_RUN ]; then
        echo ERROR: JC_RUN variable not set in $JC_ENV_FILE_LIST from folder $(pwd)
        exit 1
    fi
    sh ${JC_RUN} $*
fi
