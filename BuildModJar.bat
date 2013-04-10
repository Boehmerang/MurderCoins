::UNIVERSAL ELECTRICITY BUILD SCRIPT
@echo off
echo Promotion Type?
set /p PROMOTION=


set /p MODVERSION=<modversion.txt
set /p CurrentBuild=<buildnumber.txt
set /a BUILD_NUMBER=%CurrentBuild%+1
echo %BUILD_NUMBER% >buildnumber.txt

if %PROMOTION%==* (
	echo %MODVERSION% >recommendedversion.txt
)

set FILE_NAME=MurderCoins_v%MODVERSION%.%BUILD_NUMBER%.jar
set DEV_NAME=MCDev_v%MODVERSION%.%BUILD_NUMBER%.jar
set BACKUP_NAME=MurderCoins_v%MODVERSION%.%BUILD_NUMBER%_backup.zip

echo Starting to build %FILE_NAME%

::BUILD
runtime\bin\python\python_mcp runtime\recompile.py %*
runtime\bin\python\python_mcp runtime\reobfuscate.py %*

::ZIP-UP
cd reobf\minecraft\
7z a "..\..\builds\%FILE_NAME%" "MurderCoins\"
cd ..\..\

cd eclipse\Minecraft\bin\
7z a "..\..\..\builds\%DEV_NAME%" "MurderCoins"
cd ..\..\..\

cd resources\
7z a "..\builds\%FILE_NAME%" "*"
7z a "..\builds\%API_NAME%" "*"
7z a "..\builds\%DEV_NAME%" "*"
7z a "..\builds\%BACKUP_NAME%" "*"
cd ..\
cd src\
7z a "..\builds\%API_NAME%" "*\MurderCoins\"

7z a "..\builds\%BACKUP_NAME%" "*\MurderCoins\"
cd ..\

::UPDATE INFO FILE
echo %PROMOTION% %API_NAME% %FILE_NAME% %DEV_NAME%>>info.txt



echo Done building %FILE_NAME%

pause