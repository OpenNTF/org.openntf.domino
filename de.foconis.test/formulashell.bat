@echo off
cd %~dp0

path %DOMINO_PROGDIR%;%DOMINO_PROGDIR%\jvm\bin;%PATH%
:start
java.exe -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin de/foconis/test/formula/FormulaShell
pause
goto start
