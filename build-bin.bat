@echo off

rem Update to last version & build bin

call git pull
call jc product -u
