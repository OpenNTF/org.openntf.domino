@echo off
cd %~dp0
rem set DOMINO_PROGDIR to your server-progdir
ansicon\%PROCESSOR_ARCHITECTURE%\ansicon.exe -p
java -version
:start
rem java.exe -cp bin/;jline/jline-1.0.jar;../org.openntf.domino/bin de/foconis/test/formula/FormulaShell
java.exe -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8001 -cp bin/;jline/jline-1.0.jar;../org.openntf.formula/bin;../com.ibm.icu/bin de/foconis/test/formula/FormulaShellJava 
pause
goto start
