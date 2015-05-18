@echo off
cd /d %~dp0

mkdir ci-setup\temp
mkdir ci-setup\download
set SERVER_DIR=
for /f "tokens=2* delims=	 " %%a in ('reg query "hklm\SOFTWARE\Lotus\Domino" /v Path') do set SERVER_DIR=%%b
echo This script bundles all neccessary binaries into a zip file
echo.
echo Server Dir: %SERVER_DIR%
echo.
echo Is this correct? Press any key to continue or CTRL+C to abort
pause
xcopy "%SERVER_DIR%\*.dll" 			ci-setup\temp\ /Y
rem I know there are no .so files on a windows machine
xcopy "%SERVER_DIR%\*.so" 			ci-setup\temp\ /Y
xcopy "%SERVER_DIR%\icudt*.dat" 	ci-setup\temp\ /Y
xcopy "%SERVER_DIR%\*.tlb" 			ci-setup\temp\ /Y
xcopy "%SERVER_DIR%\N"				ci-setup\temp\N\ /S /Y
xcopy "%SERVER_DIR%\XmlSchemas" 	ci-setup\temp\XmlSchemas\ /S /Y
"%JAVA_HOME%\bin\jar" cvfM ci-setup/download/server-binaries.zip  -C ci-setup/temp .
echo.
echo Zip-File should be located in download/ directory.
echo The next step will try to invoke maven.
pause
call mvn clean install -Ddomino.binary.url=http://localhost/do-not-download
echo Opening the generated updatesite
explorer domino\updatesite\target\repository
pause

