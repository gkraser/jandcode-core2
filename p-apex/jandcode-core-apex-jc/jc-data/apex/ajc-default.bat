@echo off
if not exist %~dp0_jc\ajc-prepare.bat (
    call jc prepare
)
call %~dp0_jc\ajc-prepare.bat %*

rem CP defined in ajc-prepare.bat
set JVM=
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.jc.appdir=%~dp0
set JVM=%JVM% -Djandcode.consolecharset=auto
set JVM=%JVM% -Dfile.encoding=UTF-8
set MAIN=?

java %JVM% %JC_JVM% %MAIN% %*

if errorlevel 1 exit /b 1
