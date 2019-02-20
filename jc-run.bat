@echo off

rem DEV only!

rem in JC_JVM java parameters -Dxxx=yyy
rem in JC_CLI additional cli parameters

call %~dp0data\utils\prepare.bat

set CP=%~dp0_jc\classes-core
set CP=%CP%;%~dp0_jc\_lib
set CP=%CP%;%~dp0_jc\_lib\*

set JVM= 
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.jc.appdir=%~dp0
set JVM=%JVM% -Djandcode.consolecharset=auto
set JVM=%JVM% -Dfile.encoding=UTF-8

java %JVM% %JC_JVM% jandcode.jc.Main %JC_CLI% %*
