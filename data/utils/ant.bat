@echo off
rem minimalistic ant
set CP=%~dp0*
java -cp %CP% org.apache.tools.ant.launch.Launcher %*
