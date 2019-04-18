@echo off

pushd %~dp0

set BLD1=no

set JCDIR=..\..\_jc
set ROOTDIR=..\..

if not exist %JCDIR% mkdir %JCDIR%

set UPD1=%JCDIR%\upd1
set UPD2=%JCDIR%\upd2

set UPD_CM_DEPEND=
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\p-commons\jandcode-commons\src %ROOTDIR%\p-commons\jandcode-commons\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\p-commons\jandcode-commons-ansifer\src %ROOTDIR%\p-commons\jandcode-commons-ansifer\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\p-commons\jandcode-commons-groovy\src %ROOTDIR%\p-commons\jandcode-commons-groovy\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\p-commons\jandcode-commons-moduledef\src %ROOTDIR%\p-commons\jandcode-commons-moduledef\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\p-jc\jandcode-jc\src %ROOTDIR%\p-jc\jandcode-jc\project.jc
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\lib\lib.gradle
set UPD_CM_DEPEND=%UPD_CM_DEPEND% %ROOTDIR%\jc-libs.txt

set UPD_CM=ls -l -R --full-time --fast -n %UPD_CM_DEPEND%

%UPD_CM% > %UPD2%

fc %UPD1% %UPD2% >nul 2>&1
if errorlevel 1 set BLD1=yes

if "%BLD1%"=="yes" (
    call gradle copyLibs
    if errorlevel 1 exit /b 1
    call ant build.all
    if errorlevel 1 exit /b 1
    %UPD_CM% > %UPD1%
)

popd
