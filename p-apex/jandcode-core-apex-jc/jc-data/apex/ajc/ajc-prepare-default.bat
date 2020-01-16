@echo off

set CP=?
set JVM=
set JVM=%JVM% -cp %CP%
set JVM=%JVM% -Djandcode.app.appdir=%WD%
set JVM=%JVM% -Dfile.encoding=UTF-8
set MAIN=?
