@echo off
set WD=%~dp0
if not exist %~dp0_jc\ajc-prepare.bat (
    call jc prepare
)
call %~dp0_jc\ajc-prepare.bat %*
java %JVM% %JC_JVM% %MAIN% %*

