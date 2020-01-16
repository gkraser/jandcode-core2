@echo off

SetLocal EnableExtensions EnableDelayedExpansion

rem DEV only!

rem in JC_JVM java parameters -Dxxx=yyy
rem in JC_CLI additional cli parameters

call %~dp0data\utils\prepare.bat
if errorlevel 1 exit /b 1

set CP=%~dp0_jc\classes-core

for /F %%P in (%~dp0jc-libs.txt) do (
   set CP=!CP!;%~dp0_jc\_lib\%%P.jar
)

set JVM= 
set JVM=%JVM% -cp %~dp0_jc\classes-launcher
set JVM=%JVM% -Djandcode.launcher.classpath=%CP%
set JVM=%JVM% -Djandcode.launcher.main=jandcode.jc.Main
set JVM=%JVM% -Djandcode.jc.appdir=%~dp0
set JVM=%JVM% -Dfile.encoding=UTF-8

java %JVM% %JC_JVM% jandcode.commons.launcher.Launcher %JC_CLI% %*
