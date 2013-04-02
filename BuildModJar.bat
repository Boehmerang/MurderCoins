::UNIVERSAL ELECTRICITY BUILD SCRIPT
@echo off
echo Promotion Type? x bugged * recommended @stable
set /p PROMOTION=

set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

if %PROMOTION%==* (
	echo %MODVERSION% >recommendedversion.txt
)

set FILE_NAME=MurderCoins_v%MODVERSION%.%BUILD_NUMBER%.jar

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "murder"
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "universalelectricity"
cd ..\..\
cd src\minecraft\
cd resources\mods\
"..\..\..\7za.exe" a "..\..\builds\%FILE_NAME%" "*"
cd ..\..\
::UPDATE INFO FILE
echo %PROMOTION% %FILE_NAME% %API_NAME%>>info1.5.1.txt

echo Done building %FILE_NAME%

pause