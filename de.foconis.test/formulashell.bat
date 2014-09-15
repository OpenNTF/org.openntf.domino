@echo off
cd %~dp0
rem set DOMINO_PROGDIR to your server-progdir

set DOMINO_PROGDIR=D:\Programme\Lotus\Domino_9
ansicon\%PROCESSOR_ARCHITECTURE%\ansicon.exe -p
path %DOMINO_PROGDIR%;%DOMINO_PROGDIR%\jvm\bin;%PATH%
echo %PATH%
:start
rem java.exe -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin de/foconis/test/formula/FormulaShell
java.exe -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8001 -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin;../org.openntf.formula/bin;%DOMINO_PROGDIR%/ndext/icu4j.jar de/foconis/test/formula/FormulaShell 
java.exe -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin;../org.openntf.formula/bin;../com.ibm.icu/bin de/foconis/test/formula/FormulaShell
../com.ibm.icu/bin
pause
goto start
