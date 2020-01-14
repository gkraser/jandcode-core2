@echo off

rem in JC_JVM java parameters -Dxxx=yyy
rem in JC_CLI additional cli parameters

set JCLIBDIR=%~dp0lib
set CP=
rem CP
set JVM= 
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.jc.appdir=%~dp0
set JVM=%JVM% -Dfile.encoding=UTF-8

java %JVM% %JC_JVM% jandcode.jc.Main %JC_CLI% %*
