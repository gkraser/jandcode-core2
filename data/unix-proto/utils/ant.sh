#! /bin/sh

WD=`dirname $0`
java -cp ${WD}/ant.jar:${WD}/ant-launcher.jar org.apache.tools.ant.launch.Launcher $*

