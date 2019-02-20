@echo off

pushd %~dp0

set BLD1=no

set JCDIR=..\..\_jc
set ROOTDIR=..\..

if not exist %JCDIR% mkdir %JCDIR%

set UPD1=%JCDIR%\upd1
set UPD2=%JCDIR%\upd2

set UPD_CM_DEPEND=
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jandcode-commons\src %ROOTDIR%\jandcode-commons\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jandcode-commons-ansifer\src %ROOTDIR%\jandcode-commons-ansifer\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jandcode-commons-groovy\src %ROOTDIR%\jandcode-commons-groovy\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jandcode-commons-moduledef\src %ROOTDIR%\jandcode-commons-moduledef\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jandcode-jc\src %ROOTDIR%\jandcode-jc\project.jc

set UPD_CM=ls -l -R --full-time --fast -n %UPD_CM_DEPEND%

%UPD_CM% > %UPD2%

fc %UPD1% %UPD2% >nul 2>&1
if errorlevel 1 set BLD1=yes

if "%BLD1%"=="yes" (
    call gradle copyLibs
    call ant build.all
    if errorlevel 1 exit /b 1
    %UPD_CM% > %UPD1%
)

popd
