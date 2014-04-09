@echo off
cd %~dp0
rem set DOMINO_PROGDIR to your server-progdir
ansicon\%PROCESSOR_ARCHITECTURE%\ansicon.exe -p
path %DOMINO_PROGDIR%;%DOMINO_PROGDIR%\jvm\bin;%PATH%
echo %PATH%
:start
rem java.exe -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin de/foconis/test/formula/FormulaShellAlt
java.exe -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8001 -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin de/foconis/test/formula/FormulaShellAlt 
pause
goto start
