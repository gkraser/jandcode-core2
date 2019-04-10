@echo off
wscript //NoLogo %~dp0activate-idea.vbs
start idea -l %2 %1

