@echo off

set CP=%~dp0lib;%~dp0lib\*

set JVM= 
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.app.appdir=%~dp0
set JVM=%JVM% -Djandcode.consolecharset=auto
set JVM=%JVM% -Dfile.encoding=UTF-8

java %JVM% jandcode.web.WebRunMain %*
