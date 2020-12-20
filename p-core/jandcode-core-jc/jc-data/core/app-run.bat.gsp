<%@ page import="jandcode.commons.*; jandcode.jc.*;" %>
<%
  GspScript th = this
  //
  boolean prod = th.args.prod
  String mainClass = th.args.mainClass
  String java = "java"
  if (!prod) {
    java = "call jc @ ${java}"
  }
%>                     
@echo off

rem in JC_JVM java parameters -Dxxx=yyy

<% if (prod) { %>
set CP=%~dp0lib;%~dp0lib\*
<% } else { %>
set P1=%~dp0_jc/app-run-classpath.txt
if not exist %P1% (
    call jc prepare
)
set CP=@%P1%
<% } %>

set JVM=
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.app.appdir=%~dp0
set JVM=%JVM% -Dfile.encoding=UTF-8
set MAIN=${mainClass}

${java} %JVM% %JC_JVM% %MAIN% %*
