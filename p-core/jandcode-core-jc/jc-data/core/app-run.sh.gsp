<%@ page import="jandcode.commons.*; jandcode.jc.*;" %>
<%
  GspScript th = this
  //
  boolean prod = th.args.prod
  String mainClass = th.args.mainClass
  String java = "java"
  if (!prod) {
    java = "jc @ ${java}"
  }
  def v = { s -> '${' + s + '}' }
%>
#!/bin/sh

WD=`dirname $0`

<% if (prod) { %>
CP=${v('WD')}/lib:${v('WD')}/lib/*
<% } else { %>
P1=${v('WD')}/_jc/app-run-classpath.sh
if [ ! -e ${v('P1')} ]; then
    jc prepare
fi
. ${v('P1')} $*
<% } %>

JVM=
JVM="${v('JVM')} -cp ${v('CP')}"
JVM="${v('JVM')} -Djandcode.app.appdir=${v('WD')}"
JVM="${v('JVM')} -Dfile.encoding=UTF-8"
MAIN=${mainClass}

${java} ${v('JVM')} ${v('MAIN')} $*
