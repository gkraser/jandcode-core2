@echo off
set WD=%~dp0
set P1=%WD%/_jc/ajc-prepare.bat
if not exist %P1% (
    call jc prepare
)
call %P1% %*
java %JVM% %JC_JVM% %MAIN% %*

