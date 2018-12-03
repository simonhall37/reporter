@echo off

echo Starting dock-wait command with initial sleep %2 and incremental sleep %1
echo Going to sleep for %2 seconds...

ping localhost -n %2 > nul

echo Waking up and checking status

:loop
for /f %%i in ('docker inspect --format="{{json .State.Health.Status}}" docker_reporter_1') do set STATUS=%%i
if "%STATUS%" == ""unhealthy"" (
	echo Docker container is %STATUS% - exit with error
	goto ohdear
) else if "%STATUS%" == ""starting"" (
	echo Container is %STATUS%, try again in %1 seconds...
) else (
	echo Container is %STATUS%, continue
	goto carryon
)
ping localhost -n %1 > nul
goto loop

:ohdear
exit /b 1

:carryon
exit /b 0
