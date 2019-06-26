@echo off

rem Update to last version & build bin

call hg pull -u
call jc product -u
