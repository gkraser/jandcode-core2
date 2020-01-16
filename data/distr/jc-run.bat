@echo off

rem in JC_JVM java parameters -Dxxx=yyy
rem in JC_CLI additional cli parameters

set JCLIBDIR=%~dp0lib
set CP=
rem CP
set JVM= 
set JVM=%JVM% -cp %~dp0lib\jandcode-commons-launcher.jar
set JVM=%JVM% -Djandcode.launcher.classpath=%CP%
set JVM=%JVM% -Djandcode.launcher.main=jandcode.jc.Main
set JVM=%JVM% -Djandcode.jc.appdir=%~dp0
set JVM=%JVM% -Dfile.encoding=UTF-8

java %JVM% %JC_JVM% jandcode.commons.launcher.Launcher %JC_CLI% %*
